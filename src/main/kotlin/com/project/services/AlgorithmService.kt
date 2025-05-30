package com.project.services

import com.project.algorithms.sort.BubbleSort
import com.project.algorithms.sort.SelectionSort
import com.project.models.VisualizationStep

class AlgorithmService {
    private val bubbleSort = BubbleSort()
    private val selectionSort = SelectionSort()
    
    fun bubbleSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return bubbleSort.sort(array.toMutableList())
    }

    fun selectionSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return selectionSort.sort(array.toMutableList())
    }
}
