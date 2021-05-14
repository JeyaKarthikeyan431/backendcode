package com.erp.farwood.gatewayservice.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.erp.farwood.gatewayservice.client.PortalClient;
import com.erp.farwood.gatewayservice.config.AppProperties;
import com.erp.farwood.gatewayservice.dto.ChangePasswordDto;
import com.erp.farwood.gatewayservice.dto.ForgotPasswordDto;
import com.erp.farwood.gatewayservice.dto.LoginDto;
import com.erp.farwood.gatewayservice.dto.MenuDto;
import com.erp.farwood.gatewayservice.dto.SendPasswordDto;
import com.erp.farwood.gatewayservice.dto.SubMenuDto;
import com.erp.farwood.gatewayservice.dto.UserDto;
import com.erp.farwood.gatewayservice.dto.UserInformation;
import com.erp.farwood.gatewayservice.exception.BadDataException;
import com.erp.farwood.gatewayservice.exception.ResourceAlreadyExistsException;
import com.erp.farwood.gatewayservice.exception.ResourceNotFoundException;
import com.erp.farwood.gatewayservice.model.LoginDetails;
import com.erp.farwood.gatewayservice.model.MenuDetails;
import com.erp.farwood.gatewayservice.model.PasswordProfile;
import com.erp.farwood.gatewayservice.model.RoleGroup;
import com.erp.farwood.gatewayservice.model.TokenDetail;
import com.erp.farwood.gatewayservice.repo.ListOptionsRepo;
import com.erp.farwood.gatewayservice.repo.MenuDetailsRepository;
import com.erp.farwood.gatewayservice.repo.PasswordRepo;
import com.erp.farwood.gatewayservice.repo.RoleGroupRepo;
import com.erp.farwood.gatewayservice.repo.TokenRepo;
import com.erp.farwood.gatewayservice.repo.UserRepo;
import com.erp.farwood.gatewayservice.response.ListResponse;
import com.erp.farwood.gatewayservice.response.Response;
import com.erp.farwood.gatewayservice.token.GenerateToken;
import com.erp.farwood.gatewayservice.util.AESEncryptDecrypt;
import com.erp.farwood.gatewayservice.util.GatewayConstants;
import com.erp.farwood.gatewayservice.util.GenerationUtils;
import com.erp.farwood.gatewayservice.util.TokenValidation;
import com.erp.farwood.gatewayservice.util.Util;

@Service
public class LoginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

	private static final String CUST_TYPE_FQ = "Executive User Profile";

	private static final String KEYS_PATH = "/keys/";

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private TokenRepo tokenRepo;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private TokenValidation tokenValidation;

	@Autowired
	private GenerationUtils generationUtils;

	@Autowired
	private PortalClient portalClient;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordRepo passwordRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ListOptionsRepo listOptionsRepo;

	@Autowired
	private RoleGroupRepo roleGroupRepo;

	@Autowired
	private MenuDetailsRepository menuDtlsRepo;

	private SecretKey secretKey;

	private SecretKey passwordKey;

	@PersistenceContext
	private EntityManager em;

	@Value("${token_Expiry_Time}")
	private Integer tokenExpiryTime;

	@PostConstruct
	public void init() {
		secretKey = this.getSecretKey("key.out");
		passwordKey = this.getSecretKey("pwkey.out");
	}

	private Cipher createCipher(int cipherMode) {

		Cipher cipher = null;
		try {

			cipher = Cipher.getInstance("DESede");
			cipher.init(cipherMode, this.secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipher;
	}

	private SecretKey getSecretKey(String fileName) {

		SecretKey key = null;
		try (InputStream in = this.getClass().getResourceAsStream(KEYS_PATH + fileName)) {

			byte[] bytes = IOUtils.toByteArray(in);
			DESedeKeySpec keyspec = new DESedeKeySpec(bytes);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
			key = keyfactory.generateSecret(keyspec);
		} catch (Exception ie) {
			ie.printStackTrace();
		}
		return key;
	}

	public Response<UserInformation> login(LoginDto login) {

		Response<UserInformation> response = new Response<>();
		LoginDetails userDetail = null;
		if (ObjectUtils.isEmpty(login) || StringUtils.isEmpty(login.getEmailId())) {
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);
		} else {
			userDetail = userRepo.findByEmailId(login.getEmailId());
		}
		if (ObjectUtils.isEmpty(userDetail)) {
			throw new BadDataException(GatewayConstants.INVALID_CREDENTIALS);
		} else {
			validateLogin(login, response, userDetail);
		}
		return response;
	}

	public Response<UserInformation> signOut(String userId) {

		Response<UserInformation> response = new Response<>();
		List<TokenDetail> userTokenDetails = tokenRepo.getUserTokenDetails(Integer.valueOf(userId));
		if (userTokenDetails.isEmpty())
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);

		String token = userTokenDetails.get(0).getToken();
		LoginDetails user = tokenValidation.validateToken(token);
		if (user != null && user.getUserId() != null)
			tokenRepo.deleteUserToken(user.getUserId());
		response.setMessage(GatewayConstants.LOG_OUT_SUCCESS);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	private void validateLogin(LoginDto login, Response<UserInformation> response, LoginDetails userDetail) {

		UserInformation userInfo = new UserInformation();

		if (!"A".equals(userDetail.getUserStatus())) {
			throw new BadDataException(GatewayConstants.ACCOUNT_LOCKED);
		}
		if (!"Y".equals(userDetail.getForcePwdChange())) {

			List<TokenDetail> userTokenDetails = tokenRepo.getUserTokenDetails(userDetail.getUserId());
			if (userTokenDetails != null && !userTokenDetails.isEmpty()
					&& LocalDateTime.now().isBefore(userTokenDetails.get(0).getTokenExpiryTime())) {
				userInfo.setIsAlreadyLoggedIn("Y");
				userInfo.setToken(userTokenDetails.get(0).getToken());
			}
		}
		checkPasswordEquals(login, response, userInfo, userDetail);
	}

	private void checkPasswordEquals(LoginDto login, Response<UserInformation> response, UserInformation userInfo,
			LoginDetails userDetail) {

		String decryptPassword = AESEncryptDecrypt.decrypt(login.getPassword(), appProperties.getAESKey());
		String decryptedPassword = decryptPassword(userDetail.getPassword());
		if (decryptPassword.equals(decryptedPassword)) {
			setLoginSuccess(response, userDetail, userInfo, login);
		} else {
			throw new BadDataException(GatewayConstants.INVALID_CREDENTIALS);
		}
	}

	public void setLoginSuccess(Response<UserInformation> response, LoginDetails userDetail, UserInformation userInfo,
			LoginDto login) {
		LOGGER.info("setLoginSuccess");
		userInfo.setUserType(userDetail.getUserType());
		userInfo.setFirstName(userDetail.getFirstName());
		userInfo.setLastName(userDetail.getLastName());
		userInfo.setEmail(userDetail.getEmailId());
		userInfo.setForcePwdChange(userDetail.getForcePwdChange());
		userInfo.setRole(userDetail.getRole());
		userInfo.setDepartment(userDetail.getDepartment());
		userInfo.setGroupId(userDetail.getGroupId());
		LocalDateTime lastLoggedinTime = userDetail.getLastLoggedIn();
		if (lastLoggedinTime != null) {

			ZonedDateTime withTimeZone = ZonedDateTime.of(lastLoggedinTime, ZoneId.systemDefault());
			userInfo.setLastLoggedIn(
					withTimeZone.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)));
		}
		userInfo.setUserName(userDetail.getUserName());
		userInfo.setUserId(userDetail.getUserId());
		if (userInfo.getIsAlreadyLoggedIn().equalsIgnoreCase("N")) {
			addTokenDetails(userInfo, userDetail);
		}
		if (StringUtils.isNotEmpty(userDetail.getRole())) {
			userInfo.setRoleDesc(listOptionsRepo.findRoleDesc(userDetail.getRole()));
		}
		if (StringUtils.isNotEmpty(userDetail.getDepartment())) {
			userInfo.setDepartmentDesc(listOptionsRepo.findDepartmentDesc(userDetail.getDepartment()));
		}
		ListResponse<List<MenuDto>> res = getMenuList(userInfo.getRoleDesc());
		if (res != null) {
			userInfo.setMenus(res.getData());
		}
		userDetail.setLastLoggedIn(LocalDateTime.now());
		userRepo.save(userDetail);

		response.setData(userInfo);
		response.setMessage(GatewayConstants.LOGIN_SUCCESS);
		response.setStatus(HttpStatus.OK.value());
	}

	/*
	 * public ListResponse<List<MenuDto>> getMenuList(String roleDesc) {
	 * 
	 * ListResponse<List<MenuDto>> response = new ListResponse<List<MenuDto>>();
	 * List<MenuDto> menuList = new ArrayList<>(); List<String> all = new
	 * ArrayList<>(); all.add(roleDesc); all.add("ALL_USER"); List<RoleGroup>
	 * roleGroupList = roleGroupRepo.findRoleGroupByRoleDesc(all); if
	 * (!CollectionUtils.isEmpty(roleGroupList)) { for (RoleGroup role :
	 * roleGroupList) { if (!StringUtils.isEmpty(role.getMenuId()) &&
	 * !StringUtils.isEmpty(role.getSubMenuId())) { MenuDetails menuDtls =
	 * menuDtlsRepo.findMenuDtlsByMenuAndSubMenu(role.getMenuId(),
	 * role.getSubMenuId()); if (!ObjectUtils.isEmpty(menuDtls)) {
	 * menuList.add(setMenuDetailsDto(menuDtls)); } } else if
	 * (!StringUtils.isEmpty(role.getMenuId())) { List<MenuDetails> menuDtls =
	 * menuDtlsRepo.findMenuDtlsByMenuId(role.getMenuId()); if
	 * (!CollectionUtils.isEmpty(menuDtls)) {
	 * menuList.add(setMenuDetailsDto(menuDtls.get(0))); } } }
	 * response.setData(menuList); response.setMessage(GatewayConstants.AVAILABLE);
	 * response.setStatus(HttpServletResponse.SC_OK); } return response; }
	 */
	public ListResponse<List<MenuDto>> getMenuList(String roleDesc) {

		ListResponse<List<MenuDto>> response = new ListResponse<List<MenuDto>>();
		List<MenuDto> menuList = new ArrayList<>();
//		List<SubMenuDto> subMenuDtoList = new ArrayList<>();
		List<String> all = new ArrayList<>();
		all.add(roleDesc);
		all.add("ALL_USER");
		List<RoleGroup> roleGroupList = roleGroupRepo.findRoleGroupByRoleDesc(all);
		if (!CollectionUtils.isEmpty(roleGroupList)) {
			for (RoleGroup role : roleGroupList) {
				if (!ObjectUtils.isEmpty(role)) {
					MenuDetails menu = menuDtlsRepo.findMenuDtlsByMenuId(role.getMenuId()).get(0);
					if (!ObjectUtils.isEmpty(menu)) {
						List<SubMenuDto> subMenuDtoList = new ArrayList<>();
						if (StringUtils.isNotEmpty(role.getSubMenuId())) {
							String[] subMenuList = role.getSubMenuId().split(",");
							List<String> subMenuArray = Arrays.asList(subMenuList);
							if (!CollectionUtils.isEmpty(subMenuArray)) {
								MenuDetails subMenu = menuDtlsRepo.findMenuDtlsBySubMenuId(subMenuArray.get(0)).get(0);
								if (!ObjectUtils.isEmpty(subMenu)) {
									subMenuDtoList.add(setSubMenuDtoList(subMenu));
								}
							}
						}
						menuList.add(setMenuDetailsDto(menu, subMenuDtoList, role.getCategory()));
					}
				}
			}
			response.setData(menuList);
			response.setMessage(GatewayConstants.AVAILABLE);
			response.setStatus(HttpServletResponse.SC_OK);
			return response;
		}
		response.setMessage(GatewayConstants.MENU_MAPPING_NOT_EXIST);
		response.setStatus(HttpServletResponse.SC_OK);
		return response;
	}

	private SubMenuDto setSubMenuDtoList(MenuDetails subMenu) {
		SubMenuDto subMenuDto = new SubMenuDto();
		subMenuDto.setSubMenuId(subMenu.getSubMenuId());
		subMenuDto.setSubMenuName(subMenu.getSubMenuName());
		return subMenuDto;
	}

	private MenuDto setMenuDetailsDto(MenuDetails menu, List<SubMenuDto> subMenuDtoList, String category) {
		MenuDto menuDto = new MenuDto();
		menuDto.setMenuId(menu.getMenuId());
		menuDto.setMenuName(menu.getMenuName());
		menuDto.setMenuCategory(category);
		menuDto.setSubMenu(subMenuDtoList);
		return menuDto;
	}

	@Transactional
	private void addTokenDetails(UserInformation loginDetails, LoginDetails userDetail) {

		tokenRepo.deleteUserToken(userDetail.getUserId());
		TokenDetail tokenDetail = new TokenDetail();
		tokenDetail.setClientIp("");
		tokenDetail.setDevicetype("");
		tokenDetail.setUserId(userDetail.getUserId());
		tokenDetail.setTokenExpiryTime(LocalDateTime.now().plusMinutes(tokenExpiryTime));
		tokenDetail.setIsRemember(0);
		tokenDetail.setCreadtedBy(GatewayConstants.SYSTEM);
		tokenDetail.setUpdatedBy(GatewayConstants.SYSTEM);
		tokenDetail.setUpdatedDate(Util.getCurrentDateTime());
		tokenDetail.setCreadtedDate(Util.getCurrentDateTime());
		GenerateToken.generateToken(tokenDetail);
		tokenRepo.save(tokenDetail);
		loginDetails.setToken(tokenDetail.getToken());
	}

	public Response<List<String>> changePassword(String emailId, ChangePasswordDto changePassword) {

		Response<List<String>> response = new Response<>();
		LoginDetails userDetail = userRepo.findByEmailId(emailId);
		if (!ObjectUtils.isEmpty(userDetail)) {

			String oldPassword = decryptPassword(userDetail.getPassword());
			String oldPasswordDecrypt = AESEncryptDecrypt.decrypt(changePassword.getOldPassword(),
					appProperties.getAESKey());
			String newPasswordDecrypt = AESEncryptDecrypt.decrypt(changePassword.getNewPassword(),
					appProperties.getAESKey());
			if (oldPassword != null && oldPassword.equals(oldPasswordDecrypt)) {

				if (!oldPassword.equals(newPasswordDecrypt)) {
					String encryptedNewPassword = encryptPassword(newPasswordDecrypt);
					List<String> msgs = generationUtils.validateAndChangePassword(userDetail, newPasswordDecrypt,
							encryptedNewPassword);
					if (!msgs.isEmpty()) {
						response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
						response.setData(msgs);
					} else {

						response.setStatus(HttpStatus.OK.value());
						response.setMessage(GatewayConstants.PASS_CHANGE_SUCCESS);

						userDetail.setPassword(encryptedNewPassword);
						userDetail.setForcePwdChange("N");
						userDetail.setLastPwdChangeDate(LocalDate.now());
						userRepo.save(userDetail);
					}
				} else {
					response.setStatus(HttpStatus.NOT_FOUND.value());
					response.setMessage(GatewayConstants.SAME_PASSWORD);
				}
			} else {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				response.setMessage(GatewayConstants.INVALID_OLD_PASSWORD);
			}
		} else {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setMessage(GatewayConstants.USER_NOT_AVAIL);
		}
		return response;
	}

	public Response<String> forgotPassword(ForgotPasswordDto requestDto) {

		Response<String> responseDto = new Response<>();
		String randomPassword = null;
		LoginDetails userDetail = null;
		userDetail = userRepo.findByEmailId(requestDto.getEmailId());
		if (userDetail == null || userDetail.getEmailId() == null) {
			throw new BadDataException(GatewayConstants.INVALID_CREDENTIALS);
		} else {
			randomPassword = generationUtils.generateRandomPassword(CUST_TYPE_FQ);
			randomPassword = removeJunkChars(randomPassword);

			userDetail.setPassword(encryptPassword(randomPassword));
			userDetail.setForcePwdChange("Y");
			userDetail.setLastPwdChangeDate(LocalDate.now());
			userRepo.save(userDetail);

			SendPasswordDto sendPasswordDto = new SendPasswordDto();
			sendPasswordDto.setUserName(userDetail.getUserName());
			sendPasswordDto.setPassword(randomPassword);
			sendPasswordDto.setEmailId(userDetail.getEmailId().toLowerCase());
			portalClient.sendPassword(sendPasswordDto);
			responseDto.setMessage(GatewayConstants.FORGOT_PASS_LINK);
			responseDto.setStatus(HttpStatus.OK.value());
		}
		return responseDto;
	}

	public Response<String> createUser(String userId, UserDto userDto) {

		Response<String> responseDto = new Response<>();
		LoginDetails adminUser = userRepo.getUserById(Integer.valueOf(userId));
		LoginDetails user = new LoginDetails();
		user = userRepo.findByEmailId(userDto.getEmailId());
		if (!ObjectUtils.isEmpty(user)) {
			throw new ResourceAlreadyExistsException(GatewayConstants.USER_ALREADY_EXIST);
		} else {
			user = new LoginDetails();
			String fullName = userDto.getFirstName();
			if (StringUtils.isNotBlank(userDto.getLastName())) {
				fullName = fullName + " " + userDto.getLastName();
			}

			String password = generationUtils.generateRandomPassword(CUST_TYPE_FQ);
			password = removeJunkChars(password);
			String encryptedPassword = encryptPassword(password);

			user.setPassword(encryptedPassword);
			user.setForcePwdChange("Y");
			user.setUserName(fullName);
			user.setUserStatus("A");
			user.setEmailId(userDto.getEmailId());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setMobileNo(userDto.getMobileNo());
			user.setUserName(fullName);
			user.setUserStatus("A");
			user.setUserType("E");
			user.setPasswordProfile("USERS");
			user.setGroupId(userDto.getGroupId());
			user.setRole(userDto.getRole());
			user.setDepartment(userDto.getDepartment());
			user.setUserType("E");
			user.setCreatedBy(adminUser.getUserName());
			user.setCreatedDate(LocalDateTime.now());
			userRepo.save(user);

			portalClient.saveEmpDtls(user);

			if (StringUtils.isNotBlank(userDto.getEmailId())) {
				Map<String, Object> data = new HashMap<>();
				data.put("userName", user.getUserName());
				data.put("password", password);
				data.put("email", userDto.getEmailId());
				LOGGER.info("Creating a new user for " + user.getUserName());
				Map<String, String> mailContent = emailService.getEmailContentByTemplate("USER_CREATION", data);
				emailService.sendMail(mailContent, null, userDto.getEmailId());
			}

			responseDto.setMessage(GatewayConstants.USER_CREATED);
			responseDto.setStatus(HttpStatus.OK.value());
		}
		return responseDto;
	}

	public ListResponse<List<UserDto>> getAllUsers(String userId) {

		ListResponse<List<UserDto>> response = new ListResponse<List<UserDto>>();
		List<UserDto> userDtoList = new ArrayList<>();
		LoginDetails loginDtls = userRepo.getUserById(Integer.valueOf(userId));
		if (ObjectUtils.isEmpty(loginDtls)) {
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);
		}
		if (loginDtls.getUserType().equalsIgnoreCase("A")) {
			List<LoginDetails> users = userRepo.getAllUser();
			users.forEach(user -> userDtoList.add(setUserDetailsDto(user)));
			response.setData(userDtoList);
			response.setMessage(GatewayConstants.AVAILABLE);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setCount((long) (users != null ? users.size() : 0));
		} else {
			response.setMessage(GatewayConstants.USER_NEED_PRIVILEGE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return response;
	}

	public Response<UserDto> getUser(String userId) {

		Response<UserDto> response = new Response<>();
		UserDto userDto = new UserDto();
		LoginDetails loginDtls = userRepo.getUserById(Integer.valueOf(userId));
		if (ObjectUtils.isEmpty(loginDtls)) {
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);
		}
		userDto = setUserDetailsDto(loginDtls);
		response.setData(userDto);
		response.setMessage(GatewayConstants.AVAILABLE);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	private UserDto setUserDetailsDto(LoginDetails loginDetails) {
		UserDto userDto = new UserDto();
		modelMapper.map(loginDetails, userDto);
		if (StringUtils.isNotEmpty(loginDetails.getRole())) {
			userDto.setRoleDesc(listOptionsRepo.findRoleDesc(loginDetails.getRole()));
		}
		if (StringUtils.isNotEmpty(loginDetails.getDepartment())) {
			userDto.setDepartmentDesc(listOptionsRepo.findDepartmentDesc(loginDetails.getDepartment()));
		}
		return userDto;
	}

	public Response<String> updateUser(String userId, UserDto userDto) {

		Response<String> responseDto = new Response<>();
		LoginDetails adminUser = userRepo.getUserById(Integer.valueOf(userId));
		LoginDetails userDtls = new LoginDetails();
		userDtls = userRepo.getUserById(Integer.valueOf(userDto.getUserId()));
		if (ObjectUtils.isEmpty(userDtls)) {
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);
		} else {
			if (!StringUtils.isEmpty(userDto.getFirstName())) {
				userDtls.setFirstName(userDto.getFirstName());
			}
			if (!StringUtils.isEmpty(userDto.getLastName())) {
				userDtls.setLastName(userDto.getLastName());
			}
			if (!StringUtils.isEmpty(userDto.getUserName())) {
				userDtls.setUserName(userDto.getUserName());
			}
			if (!StringUtils.isEmpty(userDto.getEmailId())) {
				userDtls.setEmailId(userDto.getEmailId());
			}
			if (!StringUtils.isEmpty(userDto.getMobileNo())) {
				userDtls.setMobileNo(userDto.getMobileNo());
			}
			if (!StringUtils.isEmpty(userDto.getRole())) {
				userDtls.setRole(userDto.getRole());
			}
			if (!StringUtils.isEmpty(userDto.getDepartment())) {
				userDtls.setDepartment(userDto.getDepartment());
			}
			if (!StringUtils.isEmpty(userDto.getGroupId())) {
				userDtls.setGroupId(userDto.getGroupId());
			}
			if (!StringUtils.isEmpty(userDto.getForcePwdChange())) {
				userDtls.setForcePwdChange(userDto.getForcePwdChange());
			}
			if (!StringUtils.isEmpty(userDto.getUserType())) {
				userDtls.setUserType(userDto.getUserType());
			}
			if (!StringUtils.isEmpty(userDto.getUserStatus())) {
				userDtls.setUserStatus(userDto.getUserStatus());
			}
			userDtls.setUpdatedBy(adminUser.getUserName());
			userDtls.setUpdatedDate(LocalDateTime.now());
			userRepo.save(userDtls);
			responseDto.setMessage(GatewayConstants.USER_UPDATED);
			responseDto.setStatus(HttpStatus.OK.value());
		}
		return responseDto;
	}

	public Response<String> deleteUser(String userId) {

		Response<String> response = new Response<>();
		LoginDetails loginDtls = userRepo.getUserById(Integer.valueOf(userId));
		if (ObjectUtils.isEmpty(loginDtls)) {
			throw new ResourceNotFoundException(GatewayConstants.USER_NOT_FOUND);
		}
		userRepo.delete(loginDtls);
		response.setMessage(GatewayConstants.USER_DELETED);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	private String removeJunkChars(String s) {

		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if (b) {
			s = s.replaceAll("[\u0000-\u001f]", "@");
		}
		return s;
	}

	private String decryptPassword(String password) {

		try {

			byte[] decodedPassword = Base64.getMimeDecoder().decode(password.getBytes());
			Cipher cipher = this.createCipher(Cipher.DECRYPT_MODE);
			byte[] utf16 = cipher.doFinal(decodedPassword);
			String decryptedPassword = new String(utf16, "UTF16");
			return decryptedPassword.split("/")[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String encryptPassword(String password) {

		try {

			password = password + Base64.getEncoder().encodeToString(passwordKey.getEncoded());
			byte[] encodedPassword = password.getBytes("UTF16");
			Cipher cipher = this.createCipher(Cipher.ENCRYPT_MODE);
			byte[] encryptedPassword = cipher.doFinal(encodedPassword);
			return Base64.getEncoder().encodeToString(encryptedPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Response<List<String>> getPasswordGuidelines(String userId) {
		Response<List<String>> response = new Response<>();
		try {
			LoginDetails userDetail = userRepo.getUserById(Integer.valueOf(userId));
			if (userDetail == null) {
				throw new BadDataException(GatewayConstants.INVALID_CREDENTIALS);
			}
			PasswordProfile passwordProfile = passwordRepo.findProfileByProfileName(userDetail.getPasswordProfile());
			if (ObjectUtils.isEmpty(passwordProfile)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.setData(Arrays.asList(GatewayConstants.PASSWORD_GUIDELINES));
				return response;
			}
			List<String> guideLines = getGuidelinesFromProfiling(userDetail, passwordProfile);
			response.setData(guideLines);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setMessage(GatewayConstants.PASSWORD_GUIDELINES);
		}
		return response;
	}

	private List<String> getGuidelinesFromProfiling(LoginDetails userDetail, PasswordProfile passwordProfile) {

		List<String> response = new ArrayList<>();
		if (passwordProfile.getPwdLength() > 0 && passwordProfile.getPwdLength() != null
				&& passwordProfile.getMaxLength() > 0 && passwordProfile.getMaxLength() != null) {
			response.add("Your password must be between " + passwordProfile.getPwdLength() + " and "
					+ passwordProfile.getMaxLength() + " characters.");
		}
		if (passwordProfile.getMinUCaseChars() != null && passwordProfile.getMinUCaseChars() > 0) {
			response.add(
					"Your password must contain at least " + passwordProfile.getMinUCaseChars() + " uppercase letter.");
		}
		if (passwordProfile.getMinLcaseChars() != null && passwordProfile.getMinLcaseChars() > 0) {
			response.add(
					"Your password must contain at least " + passwordProfile.getMinLcaseChars() + " lowercase letter.");
		}
		if (passwordProfile.getMinNumChars() != null && passwordProfile.getMinNumChars() > 0) {
			response.add("Your password must contain at least " + passwordProfile.getMinNumChars() + " number digit.");
		}
		if (passwordProfile.getSpecialCharYN() != null && passwordProfile.getSpecialCharYN().equalsIgnoreCase("Y")
				&& passwordProfile.getMinSplChars() != null && passwordProfile.getMinSplChars() > 0) {
			response.add("Your password must contain at least " + passwordProfile.getMinSplChars()
					+ " special character of set ( " + passwordProfile.getSplCharsAllowed() + " ).");
		}
		if (passwordProfile.getBlankSpaceAllowed() != null
				&& passwordProfile.getBlankSpaceAllowed().equalsIgnoreCase("N")) {
			response.add("Your password should not contain blank space.");
		}
		if (passwordProfile.getDictWordAllowed() != null
				&& passwordProfile.getDictWordAllowed().equalsIgnoreCase("N")) {
			response.add("Your password should not contain dictionary words like ( "
					+ passwordProfile.getDictionaryWords() + " ).");
		}
		if (passwordProfile.getUserNameYN() != null && passwordProfile.getUserNameYN().equalsIgnoreCase("N")) {
			response.add("Your password should not contain username.");
		}
		if (passwordProfile.getStartsWith() != null) {
			if (passwordProfile.getStartsWith().equals("CH")) {
				response.add("Your password should starts with character.");
			} else if (passwordProfile.getStartsWith().equals("CN")) {
				response.add("Your password should starts with character/number.");
			}
		}
		if (passwordProfile.getEndsWith() != null) {
			if (passwordProfile.getEndsWith().equals("CH")) {
				response.add("Your password should ends with character.");
			} else if (passwordProfile.getEndsWith().equals("CN")) {
				response.add("Your password should ends with character/number.");
			}
		}
		if (passwordProfile.getRepeatCharAllowed() != null
				&& passwordProfile.getRepeatCharAllowed().equalsIgnoreCase("N")
				&& passwordProfile.getNumNonRepatedNumAllowed() != null
				&& passwordProfile.getNumNonRepatedNumAllowed() > 0) {
			response.add("Your password should not contain more than " + passwordProfile.getNumNonRepatedNumAllowed()
					+ " repeated characters.");
		}
		if (passwordProfile.getRepeatNumAllowed() != null && passwordProfile.getRepeatNumAllowed().equalsIgnoreCase("N")
				&& passwordProfile.getNumNonRepatedNumAllowed() != null
				&& passwordProfile.getNumNonRepatedNumAllowed() > 0) {
			response.add("Your password should not contain more than " + passwordProfile.getNumNonRepatedNumAllowed()
					+ " repeated numbers.");
		}
//		if (passwordProfile.getNumDaysLeftPwdExp() != null) {
//			response.add("Your password validity is " + passwordProfile.getNumDaysLeftPwdExp() + " days.");
//		}
//		if (passwordProfile.getRepeatNumAllowed() != null && passwordProfile.getNumNonRepatedNumAllowed() != null
//				&& passwordProfile.getNumNonRepatedNumAllowed() > 0) {
//			response.add(
//					"Reuse of the last " + passwordProfile.getNumNonRepatedNumAllowed() + " passwords not allowed.");
//		}
		return response;
	}

}
