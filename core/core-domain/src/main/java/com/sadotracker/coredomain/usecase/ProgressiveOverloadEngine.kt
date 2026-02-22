package com.sadotracker.coredomain.usecase

import javax.inject.Inject

data class OverloadSuggestion(val weightKg: Double, val reps: Int)

class ProgressiveOverloadEngine @Inject constructor() {
    /**
     * Given the last set performed and current rep-range config,
     * returns the suggested (weightKg, reps) for the next set.
     *
     * Rules:
     * 1. If lastReps < repMax  → keep weight, suggest lastReps + 1
     * 2. If lastReps >= repMax → increase weight by increment, drop back to repMin
     */
    fun suggest(
        lastWeightKg: Double,
        lastReps: Int,
        repMin: Int,
        repMax: Int,
        weightIncrementKg: Double
    ): OverloadSuggestion {
        return if (lastReps < repMax) {
            OverloadSuggestion(
                weightKg = lastWeightKg,
                reps = lastReps + 1
            )
        } else {
            OverloadSuggestion(
                weightKg = lastWeightKg + weightIncrementKg,
                reps = repMin
            )
        }
    }
}
