package com.jarvis.gymtracker.data.local

import com.jarvis.gymtracker.data.local.dao.ExerciseDao
import com.jarvis.gymtracker.data.local.entity.ExerciseEntity
import javax.inject.Inject

class DatabaseInitializer @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    suspend fun populateInitialExercises() {
        if (exerciseDao.getExerciseCount() > 0) return

        val initialExercises = listOf(
            // Chest
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Bench Press"),
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Incline Dumbbell Press"),
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Cable Fly"),
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Dumbbell Press"),
            
            // Back
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Deadlift"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Lat Pulldown"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Pull Up"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Bent Over Row"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Seated Row"),
            
            // Leg
            ExerciseEntity(muscleGroup = "Leg", exerciseName = "Squats"),
            ExerciseEntity(muscleGroup = "Leg", exerciseName = "Leg Press"),
            ExerciseEntity(muscleGroup = "Leg", exerciseName = "Leg Extension"),
            ExerciseEntity(muscleGroup = "Leg", exerciseName = "Leg Curl"),
            ExerciseEntity(muscleGroup = "Leg", exerciseName = "Calf Raise"),
            
            // Shoulder
            ExerciseEntity(muscleGroup = "Shoulder", exerciseName = "Shoulder Press"),
            ExerciseEntity(muscleGroup = "Shoulder", exerciseName = "Lateral Raise"),
            ExerciseEntity(muscleGroup = "Shoulder", exerciseName = "Front Raise"),
            ExerciseEntity(muscleGroup = "Shoulder", exerciseName = "Rear Delt Fly"),
            
            // Triceps
            ExerciseEntity(muscleGroup = "Triceps", exerciseName = "Tricep Pushdown"),
            ExerciseEntity(muscleGroup = "Triceps", exerciseName = "Skull Crushers"),
            ExerciseEntity(muscleGroup = "Triceps", exerciseName = "Overhead Extension"),
            
            // Biceps
            ExerciseEntity(muscleGroup = "Biceps", exerciseName = "Bicep Curl"),
            ExerciseEntity(muscleGroup = "Biceps", exerciseName = "Hammer Curl"),
            ExerciseEntity(muscleGroup = "Biceps", exerciseName = "Preacher Curl"),
            
            // Forearms
            ExerciseEntity(muscleGroup = "Forearms", exerciseName = "Wrist Curls"),
            ExerciseEntity(muscleGroup = "Forearms", exerciseName = "Reverse Curls"),
            
            // Abs
            ExerciseEntity(muscleGroup = "Abs", exerciseName = "Crunches"),
            ExerciseEntity(muscleGroup = "Abs", exerciseName = "Leg Raise"),
            ExerciseEntity(muscleGroup = "Abs", exerciseName = "Plank")
        )

        initialExercises.forEach { exerciseDao.insertExercise(it) }
    }
}