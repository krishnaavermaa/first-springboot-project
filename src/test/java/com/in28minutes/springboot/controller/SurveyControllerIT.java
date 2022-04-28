package com.in28minutes.springboot.controller;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.in28minutes.springboot.Application;
import com.in28minutes.springboot.model.Question;


@ExtendWith(SpringExtension.class)//replacement of junit4's RunWith and SpringRunner.class
@SpringBootTest(classes=Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIT{
	
	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = createHttpAuthenticationHeader("admin1", "secret2");
	

	private HttpHeaders createHttpAuthenticationHeader(String userId,
			String password) {

		HttpHeaders headers = new HttpHeaders();
		
		String auth = userId + ":" + password;

		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset
				.forName("US-ASCII")));

		String headerValue = "Basic " + new String(encodedAuth);

		headers.add("Authorization", headerValue);
		
		return headers;
	}
	
	@Before//not working
	public void before() {

		//headers.add("Authorization", createHttpAuthenticationHeader(
		//		"user1", "secret1"));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		System.out.println("***********executed");

	}

	@Test
	public void testRetrieveSurveyQuestion() throws JSONException {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/surveys/survey1/questions/question1"),
				HttpMethod.GET, entity, String.class);

		String expected = "{id:question1,description:\"Largest Country in the World\",correctAnswer:Russia}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void retrieveAllSurveyQuestions() throws Exception {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		ResponseEntity<List<Question>> response = restTemplate.exchange(
				createURLWithPort("/surveys/survey1/questions"),
				HttpMethod.GET, new HttpEntity<String>("DUMMY_DOESNT_MATTER",
						headers),
				new ParameterizedTypeReference<List<Question>>() {
				});

		Question sampleQuestion = new Question("question1",
				"Largest Country in the World", "Russia", Arrays.asList(
						"India", "Russia", "United States", "China"));

		assertTrue((response.getBody()).contains(sampleQuestion));
	}

	@Test
	public void addQuestion() {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setAccessControlExposeHeaders(Collections.singletonList("Location"));
		Question question = new Question("DOESNTMATTER", "Question1", "Russia",
				Arrays.asList("India", "Russia", "United States", "China"));

		HttpEntity entity = new HttpEntity<Question>(question, headers);

		ResponseEntity<String> response = restTemplate.exchange(
		createURLWithPort("/surveys/survey1/questions"),
		HttpMethod.POST, entity, String.class);
		

		System.out.println("************"+response);
		//String actual = String.valueOf(response.getHeaders().getAccessControlExposeHeaders());

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		assertTrue(actual.contains("/surveys/survey1/questions/"));//its coming false after introducing authorization as 
		//location is not returned as part of header

	}

	private String createURLWithPort(final String uri) {
		return "http://localhost:" + port + uri;
	}
	
	
	
	
	
/************UNCLEANED CODE FOR UNDERSTANDING**************
			@Test
			public void test() throws JSONException {
				//fail("Not yet implemented");
				System.out.println("PORTINFO "+port);
				String url="http://localhost:"+port+"/surveys/survey1/questions/question1";
				TestRestTemplate template=new TestRestTemplate();
				String output=template.getForObject(url, String.class);
				//Its returning an xml response by default...but we want json ...so
				//we pass a header Accept: application/json
				//to pass headers we create httpentity
				HttpHeaders headers=new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				HttpEntity entity=new HttpEntity<String>(null, headers);
				ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
				//System.out.println("Response "+response.getBody());
		//		assertTrue(response.getBody().contains("\"id\":\"question1\""));
		//		assertTrue(response.getBody().contains("\"description\":\"Largest Country in the World\""));
				System.out.println("Response "+response.getBody());
				String expected="{id:question1,description:\"Largest Country in the World\"}";
				System.out.println("Expected "+expected);
				
				JSONAssert.assertEquals(expected, response.getBody(), false);
			}
			
			@Test
			public void addQuestion() throws JSONException {
				
				String url="http://localhost:"+port+"/surveys/survey1/questions";
		//		to invoke url we will use TestRestTemplate
				
				TestRestTemplate restTemplate=new TestRestTemplate();
				
		//		now we are defining the header part of request to be sent
				HttpHeaders headers=new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				
		//		preparing a question to be sent as part of body of request
				Question question = new Question("DOESNT MATTER",
						"myDESCRIPTION", "opCORRECT",Arrays.asList(
								"op1", "op2", "opCORRECT", "none"));
				
		//		we have the header and body...now we need a good entity maker to convert this question to a JSON
				HttpEntity entity=new HttpEntity<Question>(question, headers);
				
		//		now we will post this using TestRestTemplate's exchange method....
		//		and get the returned result of type ResponseEntity(eg. 201 CREATED...., 405 Not FOUND.... etc.)<T>
				ResponseEntity<String> response= restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
				
				System.out.println("RESPONSE "+response);
				String actual=response.getHeaders().get(HttpHeaders.LOCATION).get(0);
				
				System.out.println("HEADER "+actual);
		//		now to see if this test passed or fail we use assert with some condition which may determine so 
				assertTrue(actual.contains("/surveys/survey1/questions/"));
			}
//	*************DIRTINESS ENDS HERE**************************/
	
	

}
