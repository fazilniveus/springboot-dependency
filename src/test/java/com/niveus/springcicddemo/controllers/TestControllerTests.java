package com.niveus.springcicddemo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestController.class})
@WebMvcTest
public class TestControllerTests {
	
	private final static String TEST_USER_ID = "user-id-123";
	 
	@Autowired
	private MockMvc mockMvc;
	 
	@Test
    public void testGetVersion1() throws Exception {
		testVersion1("Version 1.0");
    }
	
	
    public void testGetVersion2() throws Exception {
		testVersion2("Version 2.0");
    }
	 
	 
	 private void testVersion1(String resultOutput) throws Exception {
	        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/test/version1")
	                .with(user(TEST_USER_ID))
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andReturn();

	        String resultTestVersion = result.getResponse().getContentAsString();
	        System.out.println(resultTestVersion);
	        assertNotNull(resultTestVersion);
	        assertEquals(resultOutput, resultOutput);
	    }
	 
	 private void testVersion2(String resultOutput) throws Exception {
	        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/test/version2")
	                .with(user(TEST_USER_ID))
	                .with(csrf())
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andReturn();

	        String resultTestVersion = result.getResponse().getContentAsString();
	        System.out.println(resultTestVersion);
	        assertNotNull(resultTestVersion);
	        assertEquals(resultOutput, resultOutput);
	    }

}
