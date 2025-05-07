package com.leppard;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseManager {
    private static final String EXERCISE_FILE = "data.csv"; // Make sure this path is correct
    private static final Map<String, Exercise> exercises = new LinkedHashMap<>();

    // Load exercises from the CSV file
    public static void loadExercises() {
        try (BufferedReader br = new BufferedReader(new FileReader(EXERCISE_FILE))) {
            String line;
            br.readLine(); // Skip header line

            int count = 0;
            while ((line = br.readLine()) != null) {
                // Split line into components (name, muscle group, muscles worked)
                String[] parts = line.split(",", 3);
                if (parts.length < 3) {
                    System.out.println("Skipping malformed line: " + line); // Debugging malformed lines
                    continue;
                }

                String exerciseName = parts[0].trim();
                String muscleGroup = parts[1].trim();
                String musclesWorkedString = parts[2].trim();

                // Parse muscles worked
                List<Muscle> musclesWorked = parseMusclesWorked(musclesWorkedString);

                // Create Exercise object and store it in the map
                Exercise exercise = new Exercise(exerciseName, muscleGroup, musclesWorked);
                exercises.put(exerciseName, exercise);

                count++; // Track the number of exercises loaded
            }

            System.out.println("Loaded " + count + " exercises from the file.");
        } catch (IOException e) {
            System.out.println("Error loading exercises: " + e.getMessage());
        }
    }

    // Helper method to parse muscles worked from a string
    private static List<Muscle> parseMusclesWorked(String musclesWorkedString) {
        List<Muscle> muscles = new ArrayList<>();
        // Regex to match the pattern (muscle, muscle part, percentage%)
        String regex = "\\(([^,]+), ([^,]+), ([0-9.]+)%\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(musclesWorkedString);

        while (matcher.find()) {
            String muscleName = matcher.group(1).trim();
            String musclePart = matcher.group(2).trim();
            double percentage = Double.parseDouble(matcher.group(3).trim());

            muscles.add(new Muscle(muscleName, musclePart, percentage));
        }

        return muscles;
    }

    // Retrieve all exercise names
    public static List<String> getAllExercises() {
        return new ArrayList<>(exercises.keySet());
    }

    // Retrieve details for a specific exercise
    public static Exercise getExercise(String exerciseName) {
        return exercises.get(exerciseName);
    }
}
