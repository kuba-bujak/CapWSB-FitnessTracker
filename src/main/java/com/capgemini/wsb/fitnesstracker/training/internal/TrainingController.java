package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/training")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingProvider trainingProvider;
    private final TrainingMapper trainingMapper;

    @GetMapping
    public List<TrainingTO> getTrainings() {
        return trainingProvider.getTrainings().stream().map(trainingMapper::toTraining).toList();
    }
}
