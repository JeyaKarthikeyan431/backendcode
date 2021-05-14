package com.erp.farwood.portal.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;

/**
 * A Container to take care of pagination issues to avoid browser compatibility
 * errors. This will help to set up default page and size to avoid pagination
 * issues
 * 
 * And to get Current Unix TimeStamp instance.
 * 
 */

public class Util {

	private Util() {
	}

	/**
	 * A Function to initialize the page and size when its null Method to avoid
	 * pagination issues bu initializing default values to it.
	 * 
	 * @param page is to set pagination length
	 * @param size is to set pagination size
	 * @return page and size
	 */
	public static PageRequest getPagination(Integer page, Integer size) {
		Integer pageNumber = page != null ? page : 1;
		Integer sizeNumber = size != null ? size : 10;
		return PageRequest.of(pageNumber - 1, sizeNumber);
	}

	/**
	 * A method to get Current Unix TimeStamp Function to return current Unix
	 * TimeStamp information
	 * 
	 * @return current instance.
	 */
	public static Long getCurrentUnixTimestamp() {
		return Instant.now().getEpochSecond();
	}

	public static LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

	public static LocalDate getCurrentDate() {
		return LocalDate.now();
	}

}
