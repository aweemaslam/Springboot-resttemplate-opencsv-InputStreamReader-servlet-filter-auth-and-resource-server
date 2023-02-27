package com.hiring.assignment.assignment;

import com.hiring.assignment.controller.ControllerCOVIDAnalyticProcessor;
import com.hiring.assignment.oauth2.service.UserEntity;
import com.hiring.assignment.singleton.SingletonLoggedInUsers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AssignmentApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	ControllerCOVIDAnalyticProcessor controllerCOVIDAnalyticProcessor;
	@Autowired
	PasswordEncoder encoder;
	private String accessToken;

	@Test
	public void getToken() throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", "client");
		params.add("username", "junitTest");
		params.add("password", "junitTest");

		ResultActions result = mvc
				.perform(post("/oauth/token").params(params).with(httpBasic("client", "client"))
						.accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		String resultString = result.andReturn().getResponse().getContentAsString();

		JSONParser parser = new JSONParser();
		JSONObject parse = (JSONObject) parser.parse(resultString);
		accessToken = parse.get("access_token").toString();
		UserEntity entity = new UserEntity();
		entity.setUsername("junitTest");
		entity.setPassword(encoder.encode("junitTest"));
		entity.setAccessToken(accessToken);
		SingletonLoggedInUsers.getInstance().getUserCredentials().put("junitTest", entity);
		assertNotNull(accessToken);
	}

	@Test
	public void getCurrentLoggedInUsers() throws Exception {
		mvc.perform(get("/analytics/getCurrentLoggedInUsers")).andExpect(status().isUnauthorized());
	}

	@Test
	public void getAllCasesReportedToday() throws Exception {
		mvc.perform(get("/analytics/getAllCasesReportedToday")).andExpect(status().isUnauthorized());
	}

	@Test
	public void getAllCasesReportedTodayCountryWise() throws Exception {
		mvc.perform(get("/analytics/getAllCasesReportedTodayCountryWise")).andExpect(status().isUnauthorized());
	}

	@Test
	public void getReportedCasesByCountryName() throws Exception {
		mvc.perform(get("/analytics/getReportedCasesByCountryName").param("countryName", "Pakistan"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void getTopNReportedCases() throws Exception {
		mvc.perform(get("/analytics/getTopNReportedCases").param("limit", "10")).andExpect(status().isUnauthorized());
	}

	@Test
	public void getCasesCountByCountryAndDate() throws Exception {
		mvc.perform(get("/analytics/getCasesCountByCountryAndDate").param("countryName", "Pakistan").param("date",
				"05/05/20")).andExpect(status().isUnauthorized());
	}

	// with jwt token
	@Test
	public void getCurrentLoggedInUsersWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getCurrentLoggedInUsers").header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}

	@Test
	public void getAllCasesReportedTodayWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getAllCasesReportedToday").header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}

	@Test
	public void getAllCasesReportedTodayCountryWiseWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getAllCasesReportedTodayCountryWise").header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}

	@Test
	public void getReportedCasesByCountryNameWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getReportedCasesByCountryName").param("countryName", "Pakistan")
						.header("Content-Type", "application/json").header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}

	@Test
	public void getTopNReportedCasesWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getTopNReportedCases").param("limit", "10")
						.header("Content-Type", "application/json").header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}

	@Test
	public void getCasesCountByCountryAndDateWithJWT() throws Exception {
		getToken();
		mvc.perform(get("/analytics/getCasesCountByCountryAndDate").param("countryName", "Pakistan")
				.param("date", "05/05/20").header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}
}
