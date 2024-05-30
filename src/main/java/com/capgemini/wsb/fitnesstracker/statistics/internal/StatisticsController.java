package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/statistics")
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    /**
     * Retrieves all statistics available.
     *
     * @return A ResponseEntity containing a list of all {@link Statistics} objects
     */
    @GetMapping
    public ResponseEntity<List<Statistics>> getAllStatistics() {
        List<Statistics> allStatistics = statisticsService.getAllStatistics();
        return ResponseEntity.ok(allStatistics);
    }

    /**
     * Creates new statistics entry.
     *
     * @param statistics the statistics object to be created
     * @return A ResponseEntity containing the created {@link Statistics} object
     */
    @PostMapping
    public ResponseEntity<Statistics> createStatistics(@RequestBody Statistics statistics) {
        Statistics createdStatistics = statisticsService.createStatictics(statistics);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatistics);
    }

    /**
     * Updates the statistics for a given user ID.
     *
     * @param userId the user ID whose statistics are to be updated
     * @param statistics the new statistics data
     * @return A ResponseEntity containing the updated {@link Statistics} object
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Statistics> updateStatistics(@PathVariable Long userId, @RequestBody Statistics statistics) {
        Statistics updatedStatistics = statisticsService.updateStatistics(userId, statistics);
        return ResponseEntity.ok().body(updatedStatistics);
    }

    /**
     * Retrieves the statistics for a given user ID.
     *
     * @param userId the user ID whose statistics are to be retrieved
     * @return A ResponseEntity containing the located {@link Statistics} object
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Statistics> getStatisticsByUserId(@PathVariable Long userId) {
        Statistics statistics = statisticsService.getStatisticsByUserId(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Deletes the statistics for a given user ID.
     *
     * @param userId the user ID whose statistics are to be deleted
     * @return A ResponseEntity indicating the success of the operation
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteStatisticsByUserId(@PathVariable Long userId) {
        statisticsService.deleteStatisticsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
