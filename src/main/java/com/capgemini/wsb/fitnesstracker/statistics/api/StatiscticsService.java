package com.capgemini.wsb.fitnesstracker.statistics.api;

public interface StatiscticsService {

    Statistics createStatictics(Statistics statistics);

    Statistics updateStatistics(Long userId, Statistics newStatistics);

    Statistics getStatisticsByUserId(Long userId);

    void deleteStatisticsByUserId(Long userId);

}
