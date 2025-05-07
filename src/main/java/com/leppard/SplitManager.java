package com.leppard;

import java.io.*;
import java.util.*;

public class SplitManager {
    private static final String SPLITS_DIR = "splits/";
    private static final Map<String, List<String>> splits = new LinkedHashMap<>();

    public static void loadSplits() {
        File dir = new File(SPLITS_DIR);
        if (!dir.exists()) dir.mkdir();

        File[] splitFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (splitFiles != null) {
            for (File file : splitFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String splitName = file.getName().replace(".txt", "");
                    List<String> exercises = new ArrayList<>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        exercises.add(line.trim());
                    }
                    splits.put(splitName, exercises);
                } catch (IOException e) {
                    System.out.println("Error loading split: " + e.getMessage());
                }
            }
        }
    }

    public static void saveSplit(String splitName, List<String> exercises) {
        try (PrintWriter out = new PrintWriter(new FileWriter(SPLITS_DIR + splitName + ".txt"))) {
            for (String exercise : exercises) {
                out.println(exercise);
            }
            splits.put(splitName, exercises);
        } catch (IOException e) {
            System.out.println("Error saving split: " + e.getMessage());
        }
    }

    public static Map<String, List<String>> getSplits() {
        return splits;
    }
}
