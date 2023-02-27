package com.hiring.assignment.dto;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import org.apache.commons.collections4.MultiValuedMap;

public class DtoCSVNonFormatted {

	@CsvBindByName(column = "Province/State", required = false)
	private String provinceState;
	@CsvBindByName(column = "Country/Region", required = true)
	private String countryRegion;
	@CsvBindByName(column = "Lat", required = false)
	private Double latitute;
	@CsvBindByName(column = "Long", required = false)
	private Double longitude;
	@CsvBindAndJoinByName(column = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$", elementType = String.class)
	private MultiValuedMap<String, String> reportedDates;

	public String getProvinceState() {
		return provinceState;
	}

	public void setProvinceState(String provinceState) {
		this.provinceState = provinceState;
	}

	public String getCountryRegion() {
		return countryRegion;
	}

	public void setCountryRegion(String countryRegion) {
		this.countryRegion = countryRegion;
	}

	public Double getLatitute() {
		return latitute;
	}

	public void setLatitute(Double latitute) {
		this.latitute = latitute;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public MultiValuedMap<String, String> getReportedDates() {
		return reportedDates;
	}

	public void setReportedDates(MultiValuedMap<String, String> reportedDates) {
		this.reportedDates = reportedDates;
	}

}
