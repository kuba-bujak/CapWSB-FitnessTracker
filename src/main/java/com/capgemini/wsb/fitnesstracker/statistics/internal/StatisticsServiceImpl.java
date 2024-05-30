package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.StatiscticsService;
import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class StatisticsServiceImpl implements StatisticsProvider, StatiscticsService {

    private final StatisticsRepository statisticsRepository;


    @Override
    public Optional<Statistics> getStatistics(Long statisticsId) {
        return Optional.empty();
    }

    @Override
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll().stream().toList();
    }

    @Override
    public Statistics createStatictics(Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    @Override
    public Statistics updateStatistics(Long userId, Statistics newStatistics) {
        Optional<Statistics> existingStatisticsOpt = statisticsRepository.findByUserId(userId);
        if (existingStatisticsOpt.isPresent()) {
            Statistics existingStatistics = existingStatisticsOpt.get();
            existingStatistics.setTotalTrainings(newStatistics.getTotalTrainings());
            existingStatistics.setTotalDistance(newStatistics.getTotalDistance());
            existingStatistics.setTotalCaloriesBurned(newStatistics.getTotalCaloriesBurned());
            return statisticsRepository.save(existingStatistics);
        } else {
            throw new RuntimeException("Statistics for user ID " + userId + " not found");
        }
    }

    public Statistics getStatisticsByUserId(Long userId) {
        return statisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Statistics for user ID " + userId + " not found"));
    }

    @Override
    public void deleteStatisticsByUserId(Long userId) {
        Optional<Statistics> existingStatisticsOpt = statisticsRepository.findByUserId(userId);
        if (existingStatisticsOpt.isPresent()) {
            statisticsRepository.delete(existingStatisticsOpt.get());
        } else {
            throw new RuntimeException("Statistics for user ID " + userId + " not found");
        }
    }
}
