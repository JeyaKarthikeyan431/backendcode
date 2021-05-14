package com.erp.farwood.gatewayservice.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.erp.farwood.gatewayservice.exception.BadDataException;
import com.erp.farwood.gatewayservice.model.LoginDetails;
import com.erp.farwood.gatewayservice.model.TokenDetail;
import com.erp.farwood.gatewayservice.repo.TokenRepo;
import com.erp.farwood.gatewayservice.repo.UserRepo;

@Service
public class TokenValidation {

	@Autowired
	private TokenRepo tokenRepo;

	@Value("${token_Expiry_Time}")
	private Integer tokenExpiryTime;

	@Autowired
	private UserRepo userRepo;

	/**
	 * A Function to Validate token information with header and expire time limit.
	 * 
	 * 
	 * @param header as Bearer in JSON input type
	 */

	public LoginDetails validateToken(String header) {
		TokenDetail token = tokenRepo.findByToken(header);
		LoginDetails user = new LoginDetails();
		if (token == null)
			return null;
		if (null == token.getIsRemember() || 0 == token.getIsRemember()) {
			if (LocalDateTime.now().isAfter(token.getTokenExpiryTime())) {
				tokenRepo.delete(token);
				return null;
			}
			LocalDateTime tokenExtendTime = token.getTokenExpiryTime().minusMinutes(5);
			if (tokenExtendTime.isBefore(LocalDateTime.now())) {
				token.setTokenExpiryTime(LocalDateTime.now().plusMinutes(tokenExpiryTime));
				tokenRepo.save(token);
			}
		}

		if (token != null && token.getUserId() != null && token.getUserId() != 0)
			user = userRepo.findById(Integer.valueOf(token.getUserId())).orElse(null);

		return user;
	}

	public Boolean deleteToken(String header) {
		TokenDetail token = tokenRepo.findByToken(header);
		if (token != null) {
			tokenRepo.delete(token);
		} else {
			throw new BadDataException(GatewayConstants.USER_NOT_FOUND);
		}
		return true;
	}

}
