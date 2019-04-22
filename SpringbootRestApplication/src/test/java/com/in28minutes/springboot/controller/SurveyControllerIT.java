package com.in28minutes.springboot.controller;

import static org.junit.Assert.assertTrue;

//import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.springboot.SpringBootRestApplication;
import com.in28minutes.springboot.model.Question;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootRestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIT {

	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();

	@SuppressWarnings("rawtypes")
	@Test
	public void testRetrieveSurveyQuestion() throws JSONException {

		String url = "http://localhost:" + port + "/surveys/Survey1/questions/Question1";
		// String output = restTemplate.getForObject(url, String.class);

		// HttpEntity - headers
		// Accept - application/json

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		// System.out.println("Response: " + response.getBody());
		// assertTrue(response.getBody().contains("\"id\":\"Question1\""));

		String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\","
				+ "\"correctAnswer\":\"Russia\"}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void testRetrieveSurveyQuestions() throws Exception {
		String url = "http://localhost:" + port + "/surveys/Survey1/questions/";

		ResponseEntity<List<Question>> response = restTemplate.exchange(url, HttpMethod.GET,
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
		String url = "http://localhost:" + port + "/surveys/Survey1/questions/";

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		Question question = new Question("Doesn't matter", "Question", "1", Arrays.asList("1", "2", "3", "4"));

		HttpEntity entity = new HttpEntity<Question>(question, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		System.out.println(actual);
		assertTrue(actual.contains("/surveys/Survey1/questions/"));
	}

}
