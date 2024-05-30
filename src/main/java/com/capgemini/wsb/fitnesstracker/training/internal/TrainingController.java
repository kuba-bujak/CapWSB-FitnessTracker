package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingWithUserIdTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return a ResponseEntity with a list of all training transfer objects and HTTP status OK
     */
    @GetMapping
    public ResponseEntity<List<TrainingTO>> getTrainings() {
        List<TrainingTO> trainings = trainingService.getTrainings().stream().map(trainingMapper::toTraining).toList();
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    /**
     * Retrieves all trainings for a specific user by user ID.
     *
     * @param userId the ID of the user whose trainings to retrieve
     * @return a ResponseEntity with a list of training transfer objects for the specified user and HTTP status OK,
     * or HTTP status NOT_FOUND if no trainings are found for the user
     */
    @GetMapping("/training/{userId}")
    public ResponseEntity<List<TrainingTO>> getTrainingByUserId(@PathVariable Long userId) {
        List<TrainingTO> trainingList = trainingService.getTrainingsByUserId(userId).stream().map(trainingMapper::toTraining).toList();
        if (trainingList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(trainingList, HttpStatus.OK);
    }

    /**
     * Retrieves all completed trainings after a specified date.
     *
     * @param endDate the date to compare training end times against
     * @return a ResponseEntity with a list of completed training transfer objects after the specified date and HTTP status OK
     */
    @GetMapping("/completed")
    public ResponseEntity<List<TrainingTO>> getTrainingsAfterDate(@RequestParam("endDate") String endDate) {
        List<TrainingTO> trainings = trainingService.getCompletedTrainings(endDate).stream().map(trainingMapper::toTraining).toList();
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    /**
     * Retrieves all trainings with a specific activity type.
     *
     * @param activityType the activity type to filter trainings by
     * @return a ResponseEntity with a list of training transfer objects with the specified activity type and HTTP status OK
     */
    @GetMapping("/activity")
    public ResponseEntity<List<TrainingTO>> getTrainingsByActivityEqual(@RequestParam("activityType") ActivityType activityType) {
        List<TrainingTO> trainings = trainingService.getTrainingsEqualsToActivityType(activityType).stream().map(trainingMapper::toTraining).toList();
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    /**
     * Creates a new training.
     *
     * @param trainingWithUserIdTO the training transfer object containing user ID
     * @return a ResponseEntity with the created training transfer object and HTTP status CREATED,
     * or HTTP status NOT_FOUND if the user is not found
     */
    @PostMapping
    public ResponseEntity<TrainingTO> createTraining(@RequestBody TrainingWithUserIdTO trainingWithUserIdTO) {
        if (trainingWithUserIdTO.getUserId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.getUser(trainingWithUserIdTO.getUserId());

        if (user.isPresent()) {
            Training createdTraining = trainingService.createTraining(trainingMapper.toEntity(trainingWithUserIdTO, user.get()));
            return new ResponseEntity<>(trainingMapper.toTraining(createdTraining), HttpStatus.CREATED);
        }
        throw new UserNotFoundException(trainingWithUserIdTO.getUserId());
    }

    /**
     * Updates an existing training.
     *
     * @param id the ID of the training to update
     * @param trainingWithUserIdTO the training transfer object containing updated information
     * @return a ResponseEntity with the updated training transfer object and HTTP status OK,
     * or HTTP status NOT_FOUND if the training is not found
     */
    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingTO> updateTraining(@PathVariable("trainingId") Long id, @RequestBody TrainingWithUserIdTO trainingWithUserIdTO) {
        if (trainingWithUserIdTO.getUserId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userService.getUser(trainingWithUserIdTO.getUserId());

        if (user.isPresent()) {
            Training trainingToUpdate = trainingMapper.toEntity(trainingWithUserIdTO, user.get());
            trainingToUpdate.setId(id);
            Training updatedTraining = trainingService.updateTraining(trainingToUpdate);
            return new ResponseEntity<>(trainingMapper.toTraining(updatedTraining), HttpStatus.OK);
        }
        throw new UserNotFoundException(trainingWithUserIdTO.getUserId());
    }
}
