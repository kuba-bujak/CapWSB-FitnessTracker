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

    /**
     * Retrieves a statistics based on their ID.
     * If the statistics with the given ID are not found, then {@link Optional#empty()} will be returned.
     *
     * @param statisticsId id of the statistics to be searched
     * @return An {@link Optional} containing the located Statistics, or {@link Optional#empty()} if not found
     */
    @Override
    public Optional<Statistics> getStatistics(Long statisticsId) {
        return Optional.empty();
    }

    /**
     * Retrieves all statistics available.
     *
     * @return A list of all {@link Statistics} objects
     */
    @Override
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll().stream().toList();
    }

    /**
     * Creates a new statistics entry.
     *
     * @param statistics the statistics object to be created
     * @return The created {@link Statistics} object
     */
    @Override
    public Statistics createStatictics(Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    /**
     * Updates the statistics for a given user ID.
     * If the statistics for the given user ID are not found, an exception will be thrown.
     *
     * @param userId the user ID whose statistics are to be updated
     * @param newStatistics the new statistics data
     * @return The updated {@link Statistics} object
     */
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

    /**
     * Retrieves the statistics for a given user ID.
     * If the statistics for the given user ID are not found, an exception will be thrown.
     *
     * @param userId the user ID whose statistics are to be retrieved
     * @return The located {@link Statistics} object
     */
    public Statistics getStatisticsByUserId(Long userId) {
        return statisticsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Statistics for user ID " + userId + " not found"));
    }

    /**
     * Deletes the statistics for a given user ID.
     * If the statistics for the given user ID are not found, an exception will be thrown.
     *
     * @param userId the user ID whose statistics are to be deleted
     */
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
