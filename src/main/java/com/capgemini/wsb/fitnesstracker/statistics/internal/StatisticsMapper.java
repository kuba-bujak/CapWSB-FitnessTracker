package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class StatisticsMapper {

    private final UserMapper userMapper;

    StatisticsDto toStatisticsDto(Statistics statistics) {
        return new StatisticsDto(
                statistics.getId(),
                userMapper.toDto(statistics.getUser()),
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned()
        );
    }

    Statistics toEntity(StatisticsDto statisticsDto) {
        return new Statistics(
                userMapper.toEntity(statisticsDto.getUser()),
                statisticsDto.getTotalTrainings(),
                statisticsDto.getTotalDistance(),
                statisticsDto.getTotalCaloriesBurned()
        );
    }
}
