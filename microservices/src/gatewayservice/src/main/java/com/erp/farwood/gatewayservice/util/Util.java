
package com.erp.farwood.gatewayservice.util;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A Container to take care of pagination issues to avoid browser compatibility
 * errors. This will help to set up default page and size to avoid pagination
 * issues
 * 
 * And to get Current Unix TimeStamp instance.
 * 
 */

public class Util {

	public static Long getCurrentUnixTimestamp() {
		return Instant.now().getEpochSecond();
	}

	public static LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

	/*
	 * private Util() { }
	 * 
	 *//**
		 * A Function to initialize the page and size when its null Method to avoid
		 * pagination issues bu initializing default values to it.
		 * 
		 * @param page is to set pagination length
		 * @param size is to set pagination size
		 * @return page and size
		 */
	/*
	 * public static PageRequest getPagination(Integer page, Integer size) { Integer
	 * pageNumber = page != null ? page : 1; Integer sizeNumber = size != null ?
	 * size : 10; return PageRequest.of(pageNumber - 1, sizeNumber); }
	 * 
	 *//**
		 * A method to get Current Unix TimeStamp Function to return current Unix
		 * TimeStamp information
		 * 
		 * @return current instance.
		 *//*
			 * public static Long getCurrentUnixTimestamp() { return
			 * Instant.now().getEpochSecond(); }
			 * 
			 * public static LocalDateTime getCurrentDateTime() { return
			 * LocalDateTime.now(); } public static void setOtpAuditingFields(Otp
			 * baseAudit){ //scdConstantId=12, scdConstantDesc=OtpTypeId, scdValueId=1,
			 * scdValueDesc=signup baseAudit.setCreatedAt(LocalDateTime.now());
			 * baseAudit.setCreatedBy(System.getProperty("user.name") );
			 * baseAudit.setUpdatedAt(LocalDateTime.now());
			 * baseAudit.setUpdatedBy(System.getProperty("user.name") );
			 * baseAudit.setStartDate(LocalDateTime.now());
			 * baseAudit.setEndDate(LocalDateTime.now()); }
			 * 
			 * public static void setUserAuditingFields(UserDetail baseAudit){
			 * //scdConstantId=12, scdConstantDesc=OtpTypeId, scdValueId=1,
			 * scdValueDesc=signup baseAudit.setCreatedAt(LocalDateTime.now());
			 * baseAudit.setCreatedBy(System.getProperty("user.name") );
			 * baseAudit.setUpdatedAt(LocalDateTime.now());
			 * baseAudit.setUpdatedBy(System.getProperty("user.name") );
			 * baseAudit.setStartDate(LocalDateTime.now());
			 * baseAudit.setEndDate(LocalDateTime.now()); } public static boolean
			 * isNumeric(String str) { try { double d = Double.parseDouble(str); }
			 * catch(NumberFormatException nfe) { return false; } return true; }
			 */
}
