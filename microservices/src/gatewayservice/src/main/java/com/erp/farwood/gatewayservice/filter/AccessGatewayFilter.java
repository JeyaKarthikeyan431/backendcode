package com.erp.farwood.gatewayservice.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;

import com.erp.farwood.gatewayservice.model.LoginDetails;
import com.erp.farwood.gatewayservice.util.TokenValidation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {

	@Autowired
	private TokenValidation tokenValidation;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.debug("check token and user permission....");
		ServerHttpRequest request = exchange.getRequest();

		if (exchange.getRequest().getPath().toString().contains("signIn")
				|| exchange.getRequest().getPath().toString().contains("forgotPassword")
//				|| exchange.getRequest().getPath().toString().contains("createUser")
				|| exchange.getRequest().getPath().toString().contains("refreshToken")) {
			log.debug("Login Method Called");

			ServerHttpRequest.Builder builder = request.mutate();
			return chain.filter(exchange.mutate().request(builder.build()).build());
		} else if (exchange.getRequest().getPath().toString().contains("getDisclaimers")
				|| exchange.getRequest().getPath().toString().contains("redirectUrl")) {
			ServerHttpRequest.Builder builder = request.mutate();
			return chain.filter(exchange.mutate().request(builder.build()).build());
		} else {
			String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			if (header == null || header.isEmpty()) {
				log.debug("Header Not Available");
				return unauthorized(exchange);
			} else {
				LoginDetails user = tokenValidation.validateToken(header);
				if (user == null) {
					log.debug("unauthorized");
					return unauthorized(exchange);
				}
				ServerHttpRequest.Builder builder = request.mutate();
				builder.header("userId", user.getUserId().toString());
				builder.header("emailId", user.getEmailId());
				builder.header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate").header("Pragma",
						"no-cache");

				exchange.getResponse().getHeaders().add("Content-Security-Policy", "default-src 'self'");
				exchange.getResponse().getHeaders().add("Strict-Transport-Security", "max-age=31536000");
				exchange.getResponse().getHeaders().add("Cache-Control",
						"no-cache, no-store, max-age=0, must-revalidate");
				exchange.getResponse().getHeaders().add("Pragma", "no-cache");

				return chain.filter(exchange.mutate().request(builder.build()).build());
			}
		}
	}

	private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
		try {
			serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			String jsonO = "{ \"status\" : 401, \"message\" : \"Token Expired\", \"error\" : true}";
			ObjectMapper mapper = new ObjectMapper();
			JsonNode actualObj = mapper.readTree(jsonO);
			String gsonObject = actualObj.toString();
			DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(gsonObject.getBytes());
			return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
		} catch (IOException e) {
		}
		return null;
	}

}
