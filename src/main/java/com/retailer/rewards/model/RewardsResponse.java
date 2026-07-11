package com.retailer.rewards.model;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardsResponse {
    private String customerId;
    private Map<String, Integer> monthlyPoints;
    private int totalPoints;
}