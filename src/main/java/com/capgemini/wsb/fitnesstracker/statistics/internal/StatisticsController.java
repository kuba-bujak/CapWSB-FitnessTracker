package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/statistics")
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    @GetMapping
    public List<StatisticsDto> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }


}
