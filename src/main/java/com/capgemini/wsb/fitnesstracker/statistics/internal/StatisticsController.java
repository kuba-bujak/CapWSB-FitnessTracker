package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/statistics")
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    @GetMapping
    public List<Statistics> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }

    @PostMapping
    public Statistics createStatistics(@RequestBody Statistics statistics) {
        return statisticsService.createStatictics(statistics);
    }

    @PutMapping("/{userId}")
    public Statistics updateStatistics(@PathVariable Long userId, @RequestBody Statistics statistics) {
        return statisticsService.updateStatistics(userId, statistics);
    }

    @GetMapping("/user/{userId}")
    public Statistics getStatisticsByUserId(@PathVariable Long userId) {
        return statisticsService.getStatisticsByUserId(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteStatisticsByUserId(@PathVariable Long userId) {
        statisticsService.deleteStatisticsByUserId(userId);
    }
}
