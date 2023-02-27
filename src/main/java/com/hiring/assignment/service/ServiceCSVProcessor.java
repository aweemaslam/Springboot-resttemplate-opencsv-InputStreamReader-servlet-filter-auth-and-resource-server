package com.hiring.assignment.service;

import com.hiring.assignment.dto.DtoCSVFormatted;
import com.hiring.assignment.dto.DtoCSVNonFormatted;
import com.hiring.assignment.singleton.SingletonCSVBean;
import com.hiring.assignment.utils.Utils;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ServiceCSVProcessor {
	private final static String CSV_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	public List<DtoCSVNonFormatted> downloadCSV() throws IllegalStateException, IOException {
		URL url = new URL(CSV_URL);
		List<DtoCSVNonFormatted> beans = new CsvToBeanBuilder<DtoCSVNonFormatted>(
				new InputStreamReader(url.openStream())).withType(DtoCSVNonFormatted.class).build().parse();
		return beans;
	}

	@Scheduled(cron = "0 0 0 * * ?") // 12 am midnight
	private void runCSVDownloadScheduleAndUpdate() throws IllegalStateException, IOException {
		List<DtoCSVNonFormatted> downloadedCSVs = downloadCSV();
		SingletonCSVBean.getInstance().setDtoCSV(downloadedCSVs);
	}

	public List<DtoCSVFormatted> formatDtoCSV(List<DtoCSVNonFormatted> dtoCSVNonFormatted) {

		if (dtoCSVNonFormatted != null) {
			List<DtoCSVFormatted> formattedList = new ArrayList<>();
			dtoCSVNonFormatted.stream().forEach(row -> {
				DtoCSVFormatted formatted = new DtoCSVFormatted(row);
				// setting formatted Report Dates map
				row.getReportedDates().entries().stream().forEach(entry -> {
					formatted.getReportedDates().put(Utils.stringToLocalDate(entry.getKey()),
							Long.decode(Arrays.asList(entry.getValue()).stream().findFirst().get()));
				});
				formattedList.add(formatted);
			});
			return formattedList;
		}
		return null;
	}
}
