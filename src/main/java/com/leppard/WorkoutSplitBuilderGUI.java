package com.leppard;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class WorkoutSplitBuilderGUI {
    private static JPanel muscleDisplayPanel;
    private static JTextArea splitArea;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        ExerciseManager.loadExercises(); // Load exercises from CSV
        SplitManager.loadSplits(); // Load any saved splits
        SwingUtilities.invokeLater(WorkoutSplitBuilderGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Workout Split Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main container
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Header panel with buttons (Selectors)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton exercisesButton = new JButton("Exercises");
        exercisesButton.addActionListener(e -> displayExercises());
        JButton createSplitButton = new JButton("Create Split");
        createSplitButton.addActionListener(e -> createSplit());
        JButton viewSplitsButton = new JButton("View Splits");
        viewSplitsButton.addActionListener(e -> viewSplits());

        headerPanel.add(exercisesButton);
        headerPanel.add(createSplitButton);
        headerPanel.add(viewSplitsButton);

        // Content panel where we will dynamically change the content
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());

        // Panel for displaying exercise details
        muscleDisplayPanel = new JPanel();
        muscleDisplayPanel.setLayout(new BoxLayout(muscleDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane muscleScrollPane = new JScrollPane(muscleDisplayPanel);
        muscleScrollPane.setPreferredSize(new Dimension(200, 200));

        // Panel for split area (view and manage splits)
        splitArea = new JTextArea(10, 30);
        splitArea.setEditable(false);
        JScrollPane splitScrollPane = new JScrollPane(splitArea);

        // Add exercise display and split area to contentPanel
        JPanel exercisesPanel = new JPanel(new BorderLayout());
        exercisesPanel.add(muscleScrollPane, BorderLayout.CENTER);

        JPanel splitPanel = new JPanel(new BorderLayout());
        splitPanel.add(splitScrollPane, BorderLayout.CENTER);

        // Add all panels to contentPanel as separate cards
        contentPanel.add(exercisesPanel, "Exercises");
        contentPanel.add(splitPanel, "ViewSplits");

        // Add header and contentPanel to the main frame
        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void displayExercises() {
        // Clear previous muscle information
        muscleDisplayPanel.removeAll();

        // Set a layout that will display components vertically and allow scrolling
        muscleDisplayPanel.setLayout(new BoxLayout(muscleDisplayPanel, BoxLayout.Y_AXIS));

        // Create buttons for each exercise
        for (String exercise : ExerciseManager.getAllExercises()) {
            JButton exerciseButton = new JButton(exercise);
            exerciseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            exerciseButton.addActionListener(e -> displayMuscleInfo(exercise));
            muscleDisplayPanel.add(exerciseButton);
        }

        // Add some space at the bottom
        muscleDisplayPanel.add(Box.createVerticalGlue());

        // Update the UI
        muscleDisplayPanel.revalidate();
        muscleDisplayPanel.repaint();

        // Update the content panel (Exercises tab)
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "Exercises");
    }

    private static void displayMuscleInfo(String exercise) {
        muscleDisplayPanel.removeAll();

        Exercise ex = ExerciseManager.getExercise(exercise);
        if (ex != null) {
            for (Muscle muscle : ex.getMusclesWorked()) {
                String label = muscle.getName();
                if (!muscle.getPart().isEmpty()) {
                    label += " (" + muscle.getPart() + ")";
                }
                label += ": " + muscle.getPercentage() + "%";

                JLabel muscleLabel = new JLabel(label);
                muscleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                muscleDisplayPanel.add(muscleLabel);
            }
        }

        muscleDisplayPanel.revalidate();
        muscleDisplayPanel.repaint();
    }

    private static void createSplit() {
        String splitName = JOptionPane.showInputDialog("Enter split name (e.g. Push, Pull, Legs):");
        if (splitName == null || splitName.isEmpty()) return;

        // Create a panel with checkboxes for all exercises
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a list to store selected exercises
        List<String> selectedExercises = new ArrayList<>();
        JCheckBox[] checkBoxes = new JCheckBox[ExerciseManager.getAllExercises().size()];

        int i = 0;
        for (String exercise : ExerciseManager.getAllExercises()) {
            checkBoxes[i] = new JCheckBox(exercise);
            panel.add(checkBoxes[i]);
            i++;
        }

        // Add the panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(350, 400));

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(null, scrollPane,
                "Select Exercises for " + splitName,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Collect selected exercises
            for (i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    selectedExercises.add(checkBoxes[i].getText());
                }
            }

            if (selectedExercises.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No exercises selected. Please select at least one exercise.");
                createSplit(); // Recursively call again
                return;
            }

            SplitManager.saveSplit(splitName, selectedExercises);
            JOptionPane.showMessageDialog(null, "Split '" + splitName + "' created with " + selectedExercises.size() + " exercises.");
        }
    }

    private static void viewSplits() {
        Map<String, List<String>> splits = SplitManager.getSplits();
        if (splits.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No splits created yet.");
            return;
        }

        StringBuilder splitText = new StringBuilder("Existing Splits:\n");
        for (String splitName : splits.keySet()) {
            splitText.append(splitName).append("\n");
        }

        splitArea.setText(splitText.toString());

        // Update the content panel (ViewSplits tab)
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "ViewSplits");
    }
}
