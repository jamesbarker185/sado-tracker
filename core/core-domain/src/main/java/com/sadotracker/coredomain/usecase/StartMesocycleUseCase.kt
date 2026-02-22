package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.MesocycleDao
import com.sadotracker.coredatabase.entity.MesocycleEntity
import javax.inject.Inject

class StartMesocycleUseCase @Inject constructor(
    private val mesocycleDao: MesocycleDao
) {
    suspend operator fun invoke(programId: Long): Long {
        val mesocycle = MesocycleEntity(
            programId = programId,
            startedAt = System.currentTimeMillis()
        )
        return mesocycleDao.insert(mesocycle)
    }
}
