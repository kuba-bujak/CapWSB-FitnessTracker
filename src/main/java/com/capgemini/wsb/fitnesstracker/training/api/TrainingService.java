package com.capgemini.wsb.fitnesstracker.training.api;

/**
 * Service interface for managing training operations.
 */
public interface TrainingService {

    /**
     * Creates a new training.
     *
     * @param training the training entity to be created
     * @return the created training entity
     */
    Training createTraining(Training training);

    /**
     * Updates an existing training.
     *
     * @param training the training transfer object containing updated information
     * @return the updated training entity
     */
    Training updateTraining(TrainingTO training);
}
