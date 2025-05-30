package com.project.services

import com.project.algorithms.sort.BubbleSort
import com.project.models.VisualizationStep

class AlgorithmService {
    private val bubbleSort = BubbleSort()
    
    fun bubbleSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return bubbleSort.sort(array.toMutableList())
    }
}
