package com.project.algorithms.sort

import com.project.models.VisualizationStep

interface SortAlgorithm {
    fun <T : Comparable<T>> sort(array: MutableList<T>): List<VisualizationStep<List<T>>>
    fun name(): String
}
