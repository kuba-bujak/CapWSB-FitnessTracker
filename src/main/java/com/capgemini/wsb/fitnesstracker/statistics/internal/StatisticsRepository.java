package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Optional<Statistics> findByUserId(Long userId);
}
