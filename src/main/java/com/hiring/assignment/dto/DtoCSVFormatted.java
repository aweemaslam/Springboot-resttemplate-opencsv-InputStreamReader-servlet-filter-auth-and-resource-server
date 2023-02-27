package com.hiring.assignment.dto;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DtoCSVFormatted {

	private String provinceState;
	private String countryRegion;
	private Double latitute;
	private Double longitude;
	private Map<LocalDate, Long> reportedDates;

	public DtoCSVFormatted(DtoCSVNonFormatted row) {
		provinceState = row.getProvinceState();
		countryRegion = row.getCountryRegion();
		latitute = row.getLatitute();
		longitude = row.getLongitude();
		reportedDates = new LinkedHashMap<LocalDate, Long>();
	}

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

	public Map<LocalDate, Long> getReportedDates() {
		return reportedDates;
	}

	public void setReportedDates(Map<LocalDate, Long> reportedDates) {
		this.reportedDates = reportedDates;
	}

}
