package com.retailer.rewards.controller;

import com.retailer.rewards.model.RewardsResponse;
import com.retailer.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardsResponse> getCustomerRewards(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "3") int months) {
        
        if (customerId == null || customerId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        RewardsResponse response = rewardsService.getRewards(customerId, months);
        return ResponseEntity.ok(response);
    }
}