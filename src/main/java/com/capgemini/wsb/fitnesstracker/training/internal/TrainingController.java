package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;

    @GetMapping
    public List<TrainingTO> getTrainings() {
        return trainingService.getTrainings().stream().map(trainingMapper::toTraining).toList();
    }

    @GetMapping("/training/{userId}")
    public List<TrainingTO> getTrainingByUserId(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId).stream().map(trainingMapper::toTraining).toList();
    }

    @GetMapping("/completed")
    public List<TrainingTO> getTrainingsAfterDate(@RequestParam("endDate") String endDate) {
        return trainingService.getCompletedTrainings(endDate).stream().map(trainingMapper::toTraining).toList();
    }
}
