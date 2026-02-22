package com.sadotracker.coredatabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSeedModel(
    val name: String,
    @SerialName("muscle_group") val muscleGroup: String,
    @SerialName("primary_muscle") val primaryMuscle: String,
    @SerialName("secondary_muscle") val secondaryMuscle: String? = null,
    val category: String,
    val equipment: String,
    val mechanics: String,
    val modality: String,
    @SerialName("force_vector") val forceVector: String,
    @SerialName("is_custom") val isCustom: Boolean = false,
    val instructions: String? = null,
    @SerialName("image_res_id") val imageResId: String? = null
)
