package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingWithUserIdTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing training operations.
 */
@RestController
@RequestMapping("v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserServiceImpl userService;

    /**
     * Retrieves all trainings.
     *
     * @return a list of all training transfer objects
     */
    @GetMapping
    public List<TrainingTO> getTrainings() {
        return trainingService.getTrainings().stream().map(trainingMapper::toTraining).toList();
    }

    /**
     * Retrieves all trainings for a specific user by user ID.
     *
     * @param userId the ID of the user whose trainings to retrieve
     * @return a list of training transfer objects for the specified user
     * @throws RuntimeException if no trainings are found for the user
     */
    @GetMapping("/training/{userId}")
    public List<TrainingTO> getTrainingByUserId(@PathVariable Long userId) {
        List<TrainingTO> trainingList = trainingService.getTrainingsByUserId(userId).stream().map(trainingMapper::toTraining).toList();
        if (trainingList.isEmpty()) {
            throw new RuntimeException("No trainings found");
        }
        return trainingList;
    }

    /**
     * Retrieves all completed trainings before a specified date.
     *
     * @param endDate the date to compare training end times against
     * @return a list of completed training transfer objects before the specified date
     */
    @GetMapping("/completed")
    public List<TrainingTO> getTrainingsAfterDate(@RequestParam("endDate") String endDate) {
        return trainingService.getCompletedTrainings(endDate).stream().map(trainingMapper::toTraining).toList();
    }

    /**
     * Retrieves all trainings with a specific activity type.
     *
     * @param activityType the activity type to filter trainings by
     * @return a list of training transfer objects with the specified activity type
     */
    @GetMapping("/activity")
    public List<TrainingTO> getTrainingsByActivityEqual(@RequestParam("activityType") ActivityType activityType) {
        return trainingService.getTrainingsEqualsToActivityType(activityType).stream().map(trainingMapper::toTraining).toList();
    }

    /**
     * Creates a new training.
     *
     * @param trainingWithUserIdTO the training transfer object containing user ID
     * @return the created training transfer object
     * @throws RuntimeException if user ID is not provided or if the user is not found
     */
    @PostMapping
    public TrainingTO createTraining(@RequestBody TrainingWithUserIdTO trainingWithUserIdTO) {
        if (trainingWithUserIdTO.getUserId() == null) {
            throw new RuntimeException("User id was not provided");
        }
        Optional<User> user = userService.getUser(trainingWithUserIdTO.getUserId());

        if (user.isPresent()) {
            Training createdTraining = trainingService.createTraining(trainingMapper.toEntity(trainingWithUserIdTO, user.get()));
            return trainingMapper.toTraining(createdTraining);
        }
        throw new UserNotFoundException(trainingWithUserIdTO.getUserId());
    }

    /**
     * Updates an existing training.
     *
     * @param id the ID of the training to update
     * @param fieldName the name of the field to update
     * @param fieldValue the new value for the field
     * @return the updated training transfer object
     * @throws RuntimeException if the training is not found
     */
    @PatchMapping("/training/{id}")
    public TrainingTO updateTraining(@PathVariable Long id,
                                     @RequestParam String fieldName,
                                     @RequestParam String fieldValue) {
        Optional<Training> training = trainingService.getTraining(id);
        if (training.isPresent()) {
            TrainingTO trainingToUpdate = trainingMapper.toTraining(training.get());

            switch (fieldName) {
                case "startTime" :
                    trainingToUpdate.setStartTime(new Date(fieldValue));
                    break;

                case "endTime":
                    trainingToUpdate.setEndTime(new Date(fieldValue));
                    break;

                case "activityType":
                    try {
                        ActivityType activityType = ActivityType.valueOf(fieldValue.toUpperCase());
                        trainingToUpdate.setActivityType(activityType);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Nieprawidłowa wartość activityType: " + fieldValue);
                    }
                    break;

                case "distance":
                    double distance = Double.parseDouble(fieldValue);
                    trainingToUpdate.setDistance(distance);
                    break;

                case "averageSpeed":
                    double averageSpeed = Double.parseDouble(fieldValue);
                    trainingToUpdate.setAverageSpeed(averageSpeed);
                    break;

                default:
                    System.out.println("Nieznane pole: " + fieldName);
                    break;

            }
            Training updatedTraining = trainingService.updateTraining(trainingToUpdate);
            return trainingMapper.toTraining(updatedTraining);
        }

        throw new RuntimeException("Training was not found");

    }
}
