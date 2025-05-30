package com.project.models

import kotlinx.serialization.Serializable

@Serializable
data class VisualizationStep<T>(
    val data: T,
    val description: String,
    val highlightIndices: List<Int> = emptyList()
)
