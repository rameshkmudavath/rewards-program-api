package com.retailer.rewards.controller;

import com.retailer.rewards.model.RewardsResponse;
import com.retailer.rewards.service.RewardsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardsController.class)
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Spring Boot 4+ standardizes mocking inside the Spring Context using @MockitoBean
    @MockitoBean
    private RewardsService rewardsService;

    @Test
    @DisplayName("GET /api/rewards/{customerId} - Success Lifecycle Execution Flow")
    void testGetCustomerRewards_success() throws Exception {
        // Setup mock response payload
        Map<String, Integer> mockMonthlyBreakdown = new LinkedHashMap<>();
        mockMonthlyBreakdown.put("JULY", 90);
        mockMonthlyBreakdown.put("AUGUST", 150);
        RewardsResponse mockResponse = new RewardsResponse("CUST01", mockMonthlyBreakdown, 240);

        // Stub out the service call behavior
        Mockito.when(rewardsService.getRewards("CUST01", 3)).thenReturn(mockResponse);

        // Perform mock request validation execution
        mockMvc.perform(get("/api/rewards/CUST01")
                .param("months", "3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST01"))
                .andExpect(jsonPath("$.totalPoints").value(240))
                .andExpect(jsonPath("$.monthlyPoints.JULY").value(90))
                .andExpect(jsonPath("$.monthlyPoints.AUGUST").value(150));
    }

    @Test
    @DisplayName("GET /api/rewards/{customerId} - Should return 400 Bad Request if customerId is whitespace")
    void testGetCustomerRewards_badRequest_emptyId() throws Exception {
        mockMvc.perform(get("/api/rewards/ ")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /api/rewards/{customerId} - Should return 400 Bad Request with structured JSON when service throws IllegalArgumentException")
    void testGetCustomerRewards_invalidMonthsAdvice() throws Exception {
        // Force service layer to throw exception
        Mockito.when(rewardsService.getRewards("CUST01", -5))
               .thenThrow(new IllegalArgumentException("Time frame must be greater than 0 months."));

        mockMvc.perform(get("/api/rewards/CUST01")
                .param("months", "-5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Time frame must be greater than 0 months."))
                .andExpect(jsonPath("$.path").value("/api/rewards/CUST01"));
    }
}