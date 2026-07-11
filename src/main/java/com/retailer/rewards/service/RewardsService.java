package com.retailer.rewards.service;

import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.model.RewardsResponse;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class RewardsService {

    @Autowired
    private TransactionRepository transactionRepository;

   

    public RewardsResponse getRewards(String customerId, int monthsLimit) {
        log.info("Calculating points dataset tracking pipeline init for client token ID: {}", customerId);

        if (monthsLimit <= 0) {
            log.error("Violated minimum timeframe parameters logic setup checking: {}", monthsLimit);
            throw new IllegalArgumentException("Time frame must be greater than 0 months.");
        }

        LocalDate cutoffDate = LocalDate.now().minusMonths(monthsLimit);
        List<Transaction> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateGreaterThanEqual(customerId, cutoffDate);

        Map<String, Integer> monthlyPoints = new LinkedHashMap<>();
        int totalPoints = 0;

        for (Transaction t : transactions) {
            String month = t.getTransactionDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());

            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            totalPoints += points;
        }

        return new RewardsResponse(customerId, monthlyPoints, totalPoints);
    }
    
    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50; 
        } else if (amount > 50) {
            points += (amount - 50) * 1;
        }
        return points;
    }
}