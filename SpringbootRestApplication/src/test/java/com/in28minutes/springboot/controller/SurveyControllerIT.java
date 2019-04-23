package com.in28minutes.springboot.controller;

import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.springboot.SpringBootRestApplication;
import com.in28minutes.springboot.model.Question;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootRestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIT {

	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();

	private String retrieveAllQuestionsURL = "/surveys/Survey1/questions/";
	private String retrieveSpecificQuestionURL = "/surveys/Survey1/questions/Question1";

	private String retrieveURL(String adress) {
		return "http://localhost:" + port + adress;
	}

	private String createHttpAuthenticationHeaderValue(String userID, String password) {
		// userID, password, basic authentication
		String auth = userID + ":" + password;
		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

	@Before
	public void before() {
		headers.add("Authorization", createHttpAuthenticationHeaderValue("user", "password"));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// Accept - application/json
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testRetrieveSurveyQuestion() throws JSONException {
		HttpEntity entity = new HttpEntity<String>(null, headers);// HttpEntity - headers + body
		ResponseEntity<String> response = restTemplate.exchange(retrieveURL(retrieveSpecificQuestionURL),
				HttpMethod.GET, entity, String.class);

		String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\","
				+ "\"correctAnswer\":\"Russia\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void testRetrieveSurveyQuestions() throws Exception {
		ResponseEntity<List<Question>> response = restTemplate.exchange(retrieveURL(retrieveAllQuestionsURL),
				HttpMethod.GET,
				new HttpEntity<String>("DUMMY_DOESNT_MATTER", headers),
				new ParameterizedTypeReference<List<Question>>() {
				});

		Question sampleQuestion = new Question("Question1", "Largest Country in the World", "Russia",
				Arrays.asList("India", "Russia", "United States", "China"));
		assertTrue(response.getBody().contains(sampleQuestion));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testAddSurveyQuestion() throws Exception {
		Question question = new Question("Doesn't matter", "Question", "1", Arrays.asList("1", "2", "3", "4"));

		HttpEntity entity = new HttpEntity<Question>(question, headers);
		ResponseEntity<String> response = restTemplate.exchange(retrieveURL(retrieveAllQuestionsURL),
				HttpMethod.POST, entity, String.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		System.out.println(actual);
		assertTrue(actual.contains("/surveys/Survey1/questions/"));
	}

}
