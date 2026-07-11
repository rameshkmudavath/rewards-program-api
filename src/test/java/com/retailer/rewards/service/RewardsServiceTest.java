package com.retailer.rewards.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RewardsServiceTest {

    private final RewardsService rewardsService = new RewardsService();

    @Test
    @DisplayName("Should award 0 points when transaction is exactly $50 or less")
    void testCalculatePoints_belowOrEqualFifty() {
        assertEquals(0, rewardsService.calculatePoints(45.0));
        assertEquals(0, rewardsService.calculatePoints(50.0));
    }

    @Test
    @DisplayName("Should award 1 point per dollar between $50 and $100")
    void testCalculatePoints_betweenFiftyAndOneHundred() {
        assertEquals(30, rewardsService.calculatePoints(80.0));
        assertEquals(50, rewardsService.calculatePoints(100.0));
    }

    @Test
    @DisplayName("Should award 2 points per dollar over $100, plus 50 points from lower tier")
    void testCalculatePoints_aboveOneHundred() {
        assertEquals(90, rewardsService.calculatePoints(120.0)); // (20 * 2) + 50 = 90
        assertEquals(150, rewardsService.calculatePoints(150.0)); // (50 * 2) + 50 = 150
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when dynamic time window parameter is invalid")
    void testGetRewards_invalidMonthsParam() {
        assertThrows(IllegalArgumentException.class, () -> rewardsService.getRewards("CUST01", 0));
        assertThrows(IllegalArgumentException.class, () -> rewardsService.getRewards("CUST01", -3));
    }
}