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
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Bench Press"),
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Incline Dumbbell Press"),
            ExerciseEntity(muscleGroup = "Chest", exerciseName = "Cable Fly"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Deadlift"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Lat Pulldown"),
            ExerciseEntity(muscleGroup = "Back", exerciseName = "Pull Up"),
            ExerciseEntity(muscleGroup = "Legs", exerciseName = "Squats"),
            ExerciseEntity(muscleGroup = "Legs", exerciseName = "Leg Press"),
            ExerciseEntity(muscleGroup = "Legs", exerciseName = "Leg Extension"),
            ExerciseEntity(muscleGroup = "Shoulders", exerciseName = "Shoulder Press"),
            ExerciseEntity(muscleGroup = "Shoulders", exerciseName = "Lateral Raise"),
            ExerciseEntity(muscleGroup = "Arms", exerciseName = "Bicep Curl"),
            ExerciseEntity(muscleGroup = "Arms", exerciseName = "Tricep Pushdown")
        )

        initialExercises.forEach { exerciseDao.insertExercise(it) }
    }
}