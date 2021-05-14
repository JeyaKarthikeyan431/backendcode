package com.erp.farwood.gatewayservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.farwood.gatewayservice.dto.SendPasswordDto;
import com.erp.farwood.gatewayservice.model.LoginDetails;
import com.erp.farwood.gatewayservice.response.Response;

@FeignClient(name = "portalservice")
public interface PortalClient {

	@PostMapping("portal/sendPassword")
	public Response<String> sendPassword(@RequestBody SendPasswordDto sendPasswordDto);
	
	@PostMapping("portal/form/saveEmpDtls")
	public Response<String> saveEmpDtls(@RequestBody LoginDetails userDtls);
}
