package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingWithUserIdTO;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TrainingMapper {

    private final UserMapper userMapper;

    TrainingTO toTraining(Training training) {
        return new TrainingTO(
                training.getId(),
                userMapper.toDto(training.getUser()),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed());
    }

    Training toEntity(TrainingTO trainingTO) {
        return new Training(
                userMapper.toEntity(trainingTO.getUser()),
                trainingTO.getStartTime(),
                trainingTO.getEndTime(),
                trainingTO.getActivityType(),
                trainingTO.getDistance(),
                trainingTO.getAverageSpeed()
        );
    }

    Training toEntity(TrainingWithUserIdTO trainingWithUserIdTO, User user) {
        return new Training(
                user,
                trainingWithUserIdTO.getStartTime(),
                trainingWithUserIdTO.getEndTime(),
                trainingWithUserIdTO.getActivityType(),
                trainingWithUserIdTO.getAverageSpeed(),
                trainingWithUserIdTO.getDistance()
        );
    }
}
