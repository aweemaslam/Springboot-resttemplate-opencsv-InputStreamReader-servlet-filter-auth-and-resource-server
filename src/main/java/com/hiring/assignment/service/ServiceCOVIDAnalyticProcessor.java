package com.hiring.assignment.service;

import com.hiring.assignment.singleton.SingletonCSVBean;
import com.hiring.assignment.singleton.SingletonLoggedInUsers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ServiceCOVIDAnalyticProcessor {

	public Long getAllCasesReportedToday() throws IllegalStateException, IOException {
		AtomicLong count = new AtomicLong(0);
		SingletonCSVBean.getInstance().getDtoCSVFormatted().stream().forEach(row -> {

			Object[] array = row.getReportedDates().entrySet().parallelStream()
					.sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey())).toArray();
			if (array != null) {
				if (array.length > 2) {
					Long newValue = Long.valueOf(array[0].toString().split(Pattern.quote("="))[1]);
					Long oldValue = Long.valueOf(array[1].toString().split(Pattern.quote("="))[1]);
					count.addAndGet(newValue - oldValue);
				} else if (array.length == 1) {
					count.addAndGet(Long.valueOf(array[0].toString().split(Pattern.quote("="))[1]));
				}
			}
		});
		return count.get();
	}

	public Map<String, Long> getAllCasesReportedTodayCountryWise() throws IllegalStateException, IOException {
		Map<String, Long> reportedCases = new LinkedHashMap<String, Long>();
		SingletonCSVBean.getInstance().getDtoCSVFormatted().stream().forEach(row -> {

			Object[] array = row.getReportedDates().entrySet().parallelStream()
					.sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey())).toArray();
			if (array != null) {
				if (array.length > 2) {
					Long newValue = Long.valueOf(array[0].toString().split(Pattern.quote("="))[1]);
					Long oldValue = Long.valueOf(array[1].toString().split(Pattern.quote("="))[1]);
					reportedCases.put(row.getCountryRegion(), newValue - oldValue);
				} else if (array.length == 1) {
					Long count = Long.valueOf(array[0].toString().split(Pattern.quote("="))[1]);
					reportedCases.put(row.getCountryRegion(), count);
				}
			}
		});
		return reportedCases.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public List<String> getCurrentLoggedInUsers() {
		if (SingletonLoggedInUsers.getInstance().getUserCredentials() != null) {
			return SingletonLoggedInUsers.getInstance().getUserCredentials().entrySet().stream().map(o -> o.getKey())
					.collect(Collectors.toList());
		}
		return null;
	}

	public Map<String, Long> getReportedCasesByCountryName(String countryName)
			throws IllegalStateException, IOException {
		return getAllCasesReportedTodayCountryWise().entrySet().parallelStream()
				.filter(obj -> obj.getKey().toLowerCase().contentEquals(countryName.toLowerCase()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));

	}

	public Map<String, Long> getTopNReportedCases(Long limit) throws IllegalStateException, IOException {
		return getAllCasesReportedTodayCountryWise().entrySet().parallelStream().limit(limit)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	}

	public Map<String, Long> getCasesCountByCountryAndDate(String countryName, LocalDate stringToLoclDate, String date)
			throws IllegalStateException, IOException {
		Map<String, Long> reportedCases = new LinkedHashMap<String, Long>();
		if (SingletonCSVBean.getInstance().getDtoCSVFormatted() != null) {
			SingletonCSVBean.getInstance().getDtoCSVFormatted().stream()
					.filter(o -> o.getCountryRegion().toLowerCase().contentEquals(countryName.toLowerCase()))
					.forEach(row -> {

						Object[] array = row.getReportedDates().entrySet().parallelStream()
								.sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
								.filter(o -> o.getKey().isEqual(stringToLoclDate)
										|| o.getKey().isAfter(stringToLoclDate)
										|| o.getKey().isEqual(stringToLoclDate.minusDays(1)))
								.toArray();
						if (array != null) {
							for (int index = 0; index < array.length - 1; index++) {
								Long count = Long.valueOf(array[index].toString().split(Pattern.quote("="))[1])
										- Long.valueOf(array[index + 1].toString().split(Pattern.quote("="))[1]);

								reportedCases.put(array[index].toString().split(Pattern.quote("="))[0], count);
							}
							reportedCases.put("Total Cases Reporte From " + date + " till today are ",
									reportedCases.entrySet().stream()
											.collect(Collectors.summingLong(o -> o.getValue().longValue())));
						}
					});
			return reportedCases;
		}
		return null;
	}
}
