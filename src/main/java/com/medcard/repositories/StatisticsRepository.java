package com.medcard.repositories;

import com.medcard.entities.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Statistics findByYearMonth(YearMonth yearMonth);
}
