package com.capgemini.wsb.fitnesstracker.training.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/training")
public class TrainingController {

    @GetMapping
    public List<Training>
}
