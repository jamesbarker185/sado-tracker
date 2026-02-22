package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.AchievementDao
import com.sadotracker.coredatabase.dao.MesocycleDao
import com.sadotracker.coredatabase.entity.AchievementEntity
import javax.inject.Inject

class CheckMesocycleAchievementsUseCase @Inject constructor(
    private val mesocycleDao: MesocycleDao,
    private val achievementDao: AchievementDao
) {
    suspend operator fun invoke(mesocycleId: Long) {
        val mesocycle = mesocycleDao.getById(mesocycleId) ?: return
        val now = System.currentTimeMillis()
        val weeksCompleted = ((now - mesocycle.startedAt) / (1000 * 60 * 60 * 24 * 7L)).toInt()

        val badgesToUnlock = mutableListOf<String>()

        if (weeksCompleted >= 1) badgesToUnlock.add("meso_week_1")
        if (weeksCompleted >= 2) badgesToUnlock.add("meso_week_2")
        if (weeksCompleted >= 4) badgesToUnlock.add("meso_week_4")
        if (weeksCompleted >= 8) badgesToUnlock.add("meso_week_8")
        if (weeksCompleted >= 12) badgesToUnlock.add("meso_week_12")
        if (weeksCompleted >= 16) badgesToUnlock.add("meso_week_16")

        val badgeDetails = mapOf(
            "meso_week_1" to Pair("First Week Down", "You showed up. That's everything."),
            "meso_week_2" to Pair("Habit Forming", "Two weeks of consistency — your body is listening."),
            "meso_week_4" to Pair("On Fire", "A full month of progressive overload. Gains are coming."),
            "meso_week_8" to Pair("Iron Will", "Halfway through a full mesocycle. Elite territory."),
            "meso_week_12" to Pair("Mesocycle Complete", "12 weeks of consistent training. This is how champions are made."),
            "meso_week_16" to Pair("Legendary", "16 weeks. You are the program.")
        )

        val newAchievements = badgesToUnlock.map { badgeId ->
            AchievementEntity(
                id = badgeId,
                title = badgeDetails[badgeId]!!.first,
                description = badgeDetails[badgeId]!!.second,
                unlockedAt = now
            )
        }

        if (newAchievements.isNotEmpty()) {
            achievementDao.insertAll(newAchievements)
        }
    }
}
