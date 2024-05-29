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

@Service
@RequiredArgsConstructor
@Slf4j
class TrainingServiceImpl implements TrainingProvider, TrainingService {

    private final TrainingRepository trainingRepository;

    @Override
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findAll().stream().filter(training -> training.getUser().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Training> getCompletedTrainings(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return trainingRepository.findByEndTimeBefore(sdf.parse(date)).stream().toList();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }

    @Override
    public List<Training> getTrainingsEqualsToActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType).stream().toList();
    }

    @Override
    public Training createTraining(final Training training) {
        log.info("Creating Training {}", training);
        if (training.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }
        return trainingRepository.save(training);
    }

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
