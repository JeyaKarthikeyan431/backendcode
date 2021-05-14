package com.erp.farwood.gatewayservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.DictionarySubstringRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.erp.farwood.gatewayservice.dto.SpecialChar;
import com.erp.farwood.gatewayservice.model.LoginDetails;
import com.erp.farwood.gatewayservice.model.PasswordProfile;
import com.erp.farwood.gatewayservice.repo.PasswordRepo;

@Component
public class GenerationUtils {

	@Autowired
	private PasswordRepo passwordRepo;

	private static final String DEFAULT_MESSAGE_PATH = "/messages.properties";

	private MessageResolver messageResolver;

	private String allowedChars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjklmnpqrstuvwxyz123456789!@#$";

	@PostConstruct
	public void init() {
		Properties props = new Properties();
		try (InputStream in = this.getClass().getResourceAsStream(DEFAULT_MESSAGE_PATH)) {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		messageResolver = new PropertiesMessageResolver(props);
	}

	public boolean checkAlphaNumeric(String s) {

		final char[] chars = s.toCharArray();
		boolean status = false;
		for (int x = 0; x < chars.length; x++) {
			final char c = chars[x];
			if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
				status = true;
				break;
			}
		}
		return status;
	}

	public boolean hasNumeric(String s) {

		final char[] chars = s.toCharArray();
		boolean status = false;
		for (int x = 0; x < chars.length; x++) {
			final char c = chars[x];
			if ((c >= '0') && (c <= '9')) {
				status = true;
				break;
			}
		}
		return status;
	}

	public boolean hasCharOrCharNum(char c, String checkOpt) {

		boolean status = false;
		if (checkOpt.equals("CH")) {
			status = checkAlphaNumeric(c + "");
		} else if (checkOpt.equals("CN")) {
			status = checkAlphaNumeric(c + "");
			if (!status) {
				status = hasNumeric(c + "");
			}
		}
		return status;
	}

	public boolean hasRepeatedAlphaNum(String s, boolean isChar) {

		boolean status = false;
		if (s != null && s.length() > 1) {
			s = s.toLowerCase();
			final char[] chars = s.toCharArray();
			int checkCharLength = chars.length - 1;
			for (int x = 0; x < checkCharLength; x++) {
				if (chars[x] == chars[x + 1]) {
					String str_char = chars[x] + "";
					if (isChar && checkAlphaNumeric(str_char)) {
						status = true;
						break;
					} else if ((!isChar) && hasNumeric(str_char)) {
						status = true;
						break;
					}
				}
			}
		}
		return status;
	}

	public boolean hasRepeatedAlphaNum(String s, boolean isChar, int noOfNRC) {
		// noOfNRC -- no. of non repeat chars
		boolean status = false;
		noOfNRC = noOfNRC + 1;
		if (noOfNRC == 1) {
			status = this.hasRepeatedAlphaNum(s, isChar);
			return status;
		}
		if (s != null && s.length() >= noOfNRC) {
			s = s.toLowerCase();
			for (int x = 0; x < s.length() - noOfNRC + 1; x++) {
				String temp = s.substring(x, x + noOfNRC);
				char[] chars = temp.toCharArray();
				boolean hasSImilarChar = true;
				for (int idx = 0; idx < chars.length - 1; idx++) {
					if (chars[idx] != chars[idx + 1]) {
						hasSImilarChar = false;
						break;
					}
				}
				if (hasSImilarChar) {
					if (isChar && checkAlphaNumeric(temp)) {
						status = true;
						break;
					} else if ((!isChar) && hasNumeric(temp)) {
						status = true;
						break;
					}
					if (status)
						break;
				}
			}
		}
		return status;
	}

	public String generateRandomPassword(String userType) {
		List<CharacterRule> rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1),
				new CharacterRule(new CharacterData() {
					@Override
					public String getErrorCode() {
						return "INVALID_CHARS";
					}

					@Override
					public String getCharacters() {
						return allowedChars;
					}
				}));

		PasswordGenerator generator = new PasswordGenerator();
		return generator.generatePassword(12, rules);
	}

	public List<String> validateAndChangePassword(LoginDetails user, String newPassword, String newPswdEncrypt) {

		List<String> response = new ArrayList<>();
		String[] userId = user.getUserId().toString().toLowerCase().split("\\s+");

		String[] userDetails = ArrayUtils.addAll(userId);
		PasswordProfile passwordProfile = passwordRepo.findProfileByProfileName(user.getPasswordProfile());
		if (ObjectUtils.isEmpty(passwordProfile)) {
			return Collections.emptyList();
		}
		List<Rule> rules = new ArrayList<>();
		if (passwordProfile.getPwdLength() > 0 && passwordProfile.getPwdLength() != null
				&& passwordProfile.getMaxLength() > 0 && passwordProfile.getMaxLength() != null) {
			rules.add(new LengthRule(passwordProfile.getPwdLength(), passwordProfile.getMaxLength()));
		}
		if (passwordProfile.getBlankSpaceAllowed() != null
				&& passwordProfile.getBlankSpaceAllowed().equalsIgnoreCase("N")) {
			rules.add(new WhitespaceRule());
		}
		if (passwordProfile.getMinUCaseChars() != null && passwordProfile.getMinUCaseChars() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, passwordProfile.getMinUCaseChars()));
		}
		if (passwordProfile.getMinLcaseChars() != null && passwordProfile.getMinLcaseChars() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, passwordProfile.getMinLcaseChars()));
		}
		if (passwordProfile.getMinNumChars() != null && passwordProfile.getMinNumChars() > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.Digit, passwordProfile.getMinNumChars()));
		}
		if (passwordProfile.getSpecialCharYN() != null && passwordProfile.getSpecialCharYN().equalsIgnoreCase("Y")
				&& passwordProfile.getMinSplChars() != null && passwordProfile.getMinSplChars() > 0) {
			String[] allowedSpclChar = passwordProfile.getSplCharsAllowed().split(",");
			StringBuilder sb = new StringBuilder();
			for (String str : allowedSpclChar) {
				sb.append(str);
			}
			rules.add(new CharacterRule(new SpecialChar("INSUFFICIENT_SPECIAL", sb.toString()),
					passwordProfile.getMinSplChars()));
		}

		if (passwordProfile.getDictWordAllowed() != null
				&& passwordProfile.getDictWordAllowed().equalsIgnoreCase("N")) {
			String[] dicWords = passwordProfile.getDictionaryWords().toLowerCase().split(",");
			String[] wordsList = ArrayUtils.addAll(dicWords, userDetails);
			List<String> invalidWords = Arrays.asList(wordsList);

			Collections.sort(invalidWords);

			WordListDictionary wordListDictionary = new WordListDictionary(
					new ArrayWordList(invalidWords.toArray(new String[invalidWords.size()]), false));
			rules.add(new DictionarySubstringRule(wordListDictionary));
		}
		if (passwordProfile.getUserNameYN() != null && passwordProfile.getUserNameYN().equalsIgnoreCase("N")) {
			rules.add(new UsernameRule());
		}
		// Check First Char option
		if (passwordProfile.getStartsWith() != null) {
			char c = newPassword.charAt(0);
			if (!hasCharOrCharNum(c, passwordProfile.getStartsWith())) {
				if (passwordProfile.getStartsWith().equals("CH")) {
					response.add("Your password should starts with character.");
				} else if (passwordProfile.getStartsWith().equals("CN")) {
					response.add("Your password should starts with character/number.");
				}
			}
		}
		// Check Last Char option
		if (passwordProfile.getEndsWith() != null) {
			char c = newPassword.charAt(newPassword.length() - 1);
			if (!hasCharOrCharNum(c, passwordProfile.getEndsWith())) {
				if (passwordProfile.getEndsWith().equals("CH")) {
					response.add("Your password should ends with character.");
				} else if (passwordProfile.getEndsWith().equals("CN")) {
					response.add("Your password should ends with character/number.");
				}
			}
		}
		// Check Repeated alpha
		if (passwordProfile.getRepeatCharAllowed() != null
				&& passwordProfile.getRepeatCharAllowed().equalsIgnoreCase("N")
				&& passwordProfile.getNumNonRepatedNumAllowed() != null
				&& passwordProfile.getNumNonRepatedNumAllowed() > 0) {
			int noOfNRC = passwordProfile.getNumNonRepatedNumAllowed();
			if (hasRepeatedAlphaNum(newPassword, true, noOfNRC)) {
				response.add("Your password should not contain more than "
						+ passwordProfile.getNumNonRepatedNumAllowed() + " repeated characters.");
			}
		}
		// Check Repeated Numeric
		if (passwordProfile.getRepeatNumAllowed() != null && passwordProfile.getRepeatNumAllowed().equalsIgnoreCase("N")
				&& passwordProfile.getNumNonRepatedNumAllowed() != null
				&& passwordProfile.getNumNonRepatedNumAllowed() > 0) {
			int noOfNRC = passwordProfile.getNumNonRepatedNumAllowed();
			if (hasRepeatedAlphaNum(newPassword, false, noOfNRC)) {
				response.add("Your password should not contain more than "
						+ passwordProfile.getNumNonRepatedNumAllowed() + " repeated numbers.");
			}
			
		}

		/*
		 * if (passwordProfile.getRepeatNumAllowed() != null &&
		 * passwordProfile.getNumNonRepatedNumAllowed() != null) {
		 * List<UserPasswordHistory> historyList = null; if
		 * ("Y".equalsIgnoreCase(passwordProfile.getRepeatNumAllowed())) { historyList =
		 * getHistoryLastPwds(user.getUserName(),
		 * passwordProfile.getNumNonRepatedNumAllowed()); } else if
		 * ("N".equalsIgnoreCase(passwordProfile.getRepeatNumAllowed())) { historyList =
		 * userPassHistRepo.findByUserId(user.getUserName()); } if
		 * (checkRepeatPswd(historyList, newPswdEncrypt))
		 * response.add("Your password already used in last " +
		 * passwordProfile.getNumNonRepatedNumAllowed() + " repeated times."); }
		 */
		if (user.getUserName() != null) {
			PasswordData passwordData = new PasswordData(newPassword);
			passwordData.setUsername(user.getUserName());

			PasswordValidator validator = new PasswordValidator(messageResolver, rules);
			RuleResult result = validator.validate(passwordData);
			if (!result.isValid() || !response.isEmpty()) {
				response.addAll(validator.getMessages(result));
				return response;
			}
		}

		return Collections.emptyList();

	}

	/*
	 * private boolean checkRepeatPswd(List<UserPasswordHistory> historyList, String
	 * newPassword) { if (historyList.isEmpty()) return false; for
	 * (UserPasswordHistory h : historyList) { if
	 * (newPassword.equals(h.getPassword())) { return true; } } return false; }
	 * 
	 * private List<UserPasswordHistory> getHistoryLastPwds(String userId, Integer
	 * rows) { List<UserPasswordHistory> history = new ArrayList<>(); try { String
	 * sql =
	 * "select AUPH_AU_ID , AUPH_PASSWORD from (select * from ADS_USER_PWD_HIST where  AUPH_AU_ID=:userId order by AUPH_DATE desc) where rownum <= :rows "
	 * ; Query query = em.createNativeQuery(sql).setParameter("userId",
	 * userId).setParameter("rows", rows);
	 * 
	 * List<Object[]> records = query.getResultList(); if (records != null &&
	 * !records.isEmpty()) { for (Object[] row : records) { UserPasswordHistory h =
	 * new UserPasswordHistory(); h.setUserId(row[0].toString());
	 * h.setPassword(row[1].toString()); history.add(h); } } } catch (Exception e) {
	 * System.out.println(e.getMessage()); } return history; }
	 */
}
