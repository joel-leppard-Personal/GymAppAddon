package com.leppard;

import java.util.List;

public class Exercise {
    private final String name;
    private final String muscleGroup;
    private final List<Muscle> musclesWorked;

    public Exercise(String name, String muscleGroup, List<Muscle> musclesWorked) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.musclesWorked = musclesWorked;
    }

    public String getName() {
        return name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public List<Muscle> getMusclesWorked() {
        return musclesWorked;
    }
}
