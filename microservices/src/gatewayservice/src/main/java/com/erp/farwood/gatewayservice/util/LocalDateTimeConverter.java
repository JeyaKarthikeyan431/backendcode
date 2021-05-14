/*
 * @category LocalDateTime Converter.
 * @copyright Copyright (C) 2018 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 
package com.erp.farwood.gatewayservice.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

*//**
 * A container class to convert DateTime to the desire format. 
 * Based on the requirement data format can be changed.
 *//*
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	*//** Function to convert Data and Time format to the user required format. *//*
	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		return Optional.ofNullable(localDateTime)

				.map(Timestamp::valueOf)

				.orElse(null);
	}
		*//** Function to convert to Entity Attribute TimeStamp. *//*
	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return Optional.ofNullable(timestamp)

				.map(Timestamp::toLocalDateTime)

				.orElse(null);
	}
}
*/