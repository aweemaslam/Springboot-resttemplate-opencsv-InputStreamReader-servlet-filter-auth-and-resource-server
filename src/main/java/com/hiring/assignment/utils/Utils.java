package com.hiring.assignment.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Utils {

	public static LocalDate stringToLocalDate(String date) {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendOptional(DateTimeFormatter.ofPattern("M/dd/yyyy"))
				.appendOptional(DateTimeFormatter.ofPattern(("MM/dd/yyyy")))
				.appendOptional(DateTimeFormatter.ofPattern(("MM/d/yyyy")))
				.appendOptional(DateTimeFormatter.ofPattern(("MM/dd/yy")))
				.appendOptional(DateTimeFormatter.ofPattern(("M/dd/yy")))
				.appendOptional(DateTimeFormatter.ofPattern(("MM/d/yy")))
				.appendOptional(DateTimeFormatter.ofPattern(("M/d/yy"))).toFormatter();
		// convert String to LocalDate # bad approach , need to fix it
		LocalDate localDate = LocalDate.parse(date, formatter);

		return localDate;
	}
}
