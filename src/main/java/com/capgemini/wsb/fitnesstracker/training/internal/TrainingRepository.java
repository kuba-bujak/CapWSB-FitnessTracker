package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

interface TrainingRepository extends JpaRepository<Training, Long> {
    /**
     * Finds all trainings that ended before the specified date.
     *
     * @param endDate the end date to filter completed trainings
     * @return list of completed trainings
     */
    List<Training> findByEndTimeBefore(Date endDate);
}
