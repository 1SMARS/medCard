package com.medcard.services.impl;

import com.medcard.entities.Statistics;
import com.medcard.repositories.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.YearMonth;

@Service
@AllArgsConstructor
public class StatisticsImpl {

    private final StatisticsRepository statisticsRepository;

    public void updateRegistrationStatistics() {
        YearMonth currentYearMonth = YearMonth.now();

        // Check if statistics already exist for the current month
        Statistics existingStats = statisticsRepository.findByYearMonth(currentYearMonth);

        if (existingStats != null) {
            // Increment user count
            existingStats.setUserCount(existingStats.getUserCount() + 1);
            statisticsRepository.save(existingStats);
        } else {
            // Create new statistics entry
            Statistics newStats = new Statistics();
            newStats.setYearMonth(currentYearMonth);
            newStats.setUserCount(1L);
            statisticsRepository.save(newStats);
        }
    }
}
