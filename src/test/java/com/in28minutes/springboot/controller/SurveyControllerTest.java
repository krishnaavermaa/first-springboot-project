package com.in28minutes.springboot.controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PathVariable;

import com.in28minutes.springboot.model.Question;
import com.in28minutes.springboot.service.SurveyService;


@ExtendWith(SpringExtension.class)//replacement of junit4's RunWith and SpringRunner.class
@WebMvcTest(SurveyController.class)
@WithMockUser(username = "admin1",password = "secret2")
public class SurveyControllerTest{

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SurveyService service;
	
	@Test
	public void retrieveDetailsForQuestion() throws Exception {
		Question question = new Question("question1",
				"Largest Country in the World", "Russia", Arrays.asList(
						"India", "Russia", "United States", "China"));
		Mockito.when(service.retrieveQuestion(Mockito.anyString(), Mockito.anyString())).thenReturn(question);
		
		
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/surveys/survey1/questions/question1").accept(MediaType.APPLICATION_JSON);
		
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		result.getResponse().getContentAsString();
		
		String expected = "{id:question1,description:\"Largest Country in the World\",correctAnswer:Russia}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false); 
		
		
	}
	
	
	 @Test
	    public void retrieveSurveyQuestions() throws Exception {
	        List<Question> mockList = Arrays.asList(
	                new Question("question1", "First Alphabet", "A", Arrays.asList(
	                        "A", "B", "C", "D")),
	                new Question("question2", "Last Alphabet", "Z", Arrays.asList(
	                        "A", "X", "Y", "Z")));

	        Mockito.when(service.retrieveQuestions(Mockito.anyString())).thenReturn(mockList);

	        MvcResult result = mockMvc
	                .perform(
	                        MockMvcRequestBuilders
	                                .get("/surveys/survey1/questions").accept(
	                                        MediaType.APPLICATION_JSON)).andReturn();

	        String expected = "["
	                + "{id:question1,description:\"First Alphabet\",correctAnswer:A,options:[A,B,C,D]},"
	                + "{id:question2,description:\"Last Alphabet\",correctAnswer:Z,options:[A,X,Y,Z]}"
	                + "]";

	        JSONAssert.assertEquals(expected, result.getResponse()
	                .getContentAsString(), false);
	    }
	 
	 
	 @Test
	    public void createSurveyQuestion() throws Exception {
	    		Question mockQuestion = new Question("1", "Smallest Number", "1",
					Arrays.asList("1", "2", "3", "4"));

			String questionJson = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\":[\"1\",\"2\",\"3\",\"4\"]}";
			//service.addQuestion to respond back with mockQuestion
			Mockito.when(
					service.addQuestion(Mockito.anyString(), Mockito
							.any(Question.class))).thenReturn(mockQuestion);

			//Send question as body to /surveys/Survey1/questions
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
					"/surveys/survey1/questions")
					.accept(MediaType.APPLICATION_JSON).content(questionJson)
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.CREATED.value(), response.getStatus());

			assertEquals("http://localhost/surveys/survey1/questions/1", response
					.getHeader(HttpHeaders.LOCATION));
	    
	    }

}
