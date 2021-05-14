package com.erp.farwood.gatewayservice.token;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.erp.farwood.gatewayservice.model.TokenDetail;

/**
 * A Container class to generate token for authorization purpose. And with a
 * time limit to expire token.
 */

public class GenerateToken {

	private GenerateToken() {
	}

	/**
	 * Function to generate Token for user logged in.
	 * 
	 * @param tokenDetail for login time new token will be generated.
	 * @return generated token
	 */
	public static TokenDetail generateToken(TokenDetail tokenDetail) {
		JWTSigner signer = new JWTSigner("farwood_Gateway");
		Map<String, Object> claims = new HashMap<>();
		claims.put("user", tokenDetail.getUserId());
		JWTSigner.Options option = new JWTSigner.Options();
		option.setAlgorithm(Algorithm.HS256);
		option.setIssuedAt(true);
		option.setJwtId(true);
		// option.setExpirySeconds(10);
		option.setNotValidBeforeLeeway(3);
		tokenDetail.setToken(signer.sign(claims, option));
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 15);
		return tokenDetail;
	}

}
