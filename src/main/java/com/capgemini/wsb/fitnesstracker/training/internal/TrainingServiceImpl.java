package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing training operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class TrainingServiceImpl implements TrainingProvider, TrainingService {

    private final TrainingRepository trainingRepository;

    /**
     * Retrieves a training by its ID.
     *
     * @param trainingId the ID of the training to retrieve
     * @return an Optional containing the training if found, or an empty Optional if not found
     */
    @Override
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    /**
     * Retrieves all trainings.
     *
     * @return a list of all trainings
     */
    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    /**
     * Retrieves all trainings for a specific user by user ID.
     *
     * @param userId the ID of the user whose trainings to retrieve
     * @return a list of trainings for the specified user
     */
    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findAll().stream().filter(training -> training.getUser().getId().equals(userId)).collect(Collectors.toList());
    }

    /**
     * Retrieves all completed trainings before a specified date.
     *
     * @param date the date to compare training end times against
     * @return a list of completed trainings before the specified date
     * @throws RuntimeException if the date format is invalid
     */
    @Override
    public List<Training> getCompletedTrainings(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return trainingRepository.findByEndTimeBefore(sdf.parse(date)).stream().toList();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }

    /**
     * Retrieves all trainings with a specific activity type.
     *
     * @param activityType the activity type to filter trainings by
     * @return a list of trainings with the specified activity type
     */
    @Override
    public List<Training> getTrainingsEqualsToActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType).stream().toList();
    }

    /**
     * Creates a new training.
     *
     * @param training the training entity to be created
     * @return the created training entity
     * @throws IllegalArgumentException if the training already has an ID
     */
    @Override
    public Training createTraining(final Training training) {
        log.info("Creating Training {}", training);
        if (training.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }
        return trainingRepository.save(training);
    }

    /**
     * Updates an existing training.
     *
     * @param training the training transfer object containing updated information
     * @return the updated training entity
     * @throws RuntimeException if the training was not found
     */
    @Override
    public Training updateTraining(TrainingTO training) {
        Optional<Training> foundTraining = trainingRepository.findById(training.getId());

        if (foundTraining.isPresent()) {
            Training trainingToUpdate = foundTraining.get();

            trainingToUpdate.setActivityType(training.getActivityType());
            trainingToUpdate.setDistance(training.getDistance());
            trainingToUpdate.setAverageSpeed(training.getAverageSpeed());
            trainingToUpdate.setStartTime(training.getStartTime());
            trainingToUpdate.setEndTime(training.getEndTime());

            return trainingRepository.save(trainingToUpdate);
        }
        throw new RuntimeException("Training was not found");
    }
}
