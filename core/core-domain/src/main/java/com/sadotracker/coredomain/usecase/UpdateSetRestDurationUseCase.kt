package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.SetDao
import javax.inject.Inject

class UpdateSetRestDurationUseCase @Inject constructor(
    private val setDao: SetDao
) {
    suspend operator fun invoke(setId: Long, durationSecs: Int) {
        setDao.updateRestDuration(setId, durationSecs)
    }
}
