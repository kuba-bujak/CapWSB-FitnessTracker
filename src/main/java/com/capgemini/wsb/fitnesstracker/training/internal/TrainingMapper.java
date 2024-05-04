package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {
    TrainingTO toTraining(Training training) {
        return new TrainingTO(training.get )
    }
}
