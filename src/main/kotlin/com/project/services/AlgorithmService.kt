package com.project.services

import com.project.algorithms.sort.BubbleSort
import com.project.algorithms.sort.SelectionSort
import com.project.algorithms.sort.QuickSort
import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
data class AlgorithmComparison(
    val algorithm1: AlgorithmResult,
    val algorithm2: AlgorithmResult
)

@Serializable
data class AlgorithmResult(
    val name: String,
    val steps: List<VisualizationStep<List<Int>>>,
    val metrics: AlgorithmMetrics
)

@Serializable
data class AlgorithmMetrics(
    val comparisons: Int,
    val swaps: Int,
    val executionTimeMs: Long,
    val totalSteps: Int
)

class AlgorithmService {
    private val bubbleSort = BubbleSort()
    private val selectionSort = SelectionSort()
    private val quickSort = QuickSort()
    
    fun bubbleSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return bubbleSort.sort(array.toMutableList())
    }

    fun selectionSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return selectionSort.sort(array.toMutableList())
    }

    fun quickSort(array: List<Int>): List<VisualizationStep<List<Int>>> {
        return quickSort.sort(array.toMutableList())
    }
    
    fun compareAlgorithms(array: List<Int>, algorithm1: String, algorithm2: String): AlgorithmComparison {
        println("Comparando algoritmos: $algorithm1 vs $algorithm2")
        println("Array: $array")
        
        val result1 = executeAlgorithmWithMetrics(array, algorithm1)
        val result2 = executeAlgorithmWithMetrics(array, algorithm2)
        
        println("Resultado 1: ${result1.name} - ${result1.steps.size} pasos")
        println("Resultado 2: ${result2.name} - ${result2.steps.size} pasos")
        
        return AlgorithmComparison(result1, result2)
    }
    
    private fun executeAlgorithmWithMetrics(array: List<Int>, algorithmName: String): AlgorithmResult {
        val startTime = System.currentTimeMillis()
        
        val steps = when (algorithmName.lowercase()) {
            "bubble" -> {
                println("Ejecutando Bubble Sort")
                bubbleSort(array)
            }
            "selection" -> {
                println("Ejecutando Selection Sort")
                selectionSort(array)
            }
            "quick" -> {
                println("Ejecutando Quick Sort")
                quickSort(array)
            }
            else -> {
                println("Algoritmo no soportado: $algorithmName")
                throw IllegalArgumentException("Algoritmo no soportado: $algorithmName")
            }
        }
        
        val endTime = System.currentTimeMillis()
        val executionTime = endTime - startTime
        
        println("Algoritmo $algorithmName ejecutado en ${executionTime}ms con ${steps.size} pasos")
        
        // Calcular métricas basadas en los pasos
        val metrics = calculateMetrics(steps, executionTime)
        
        val displayName = when (algorithmName.lowercase()) {
            "bubble" -> "Bubble Sort"
            "selection" -> "Selection Sort"
            "quick" -> "Quick Sort"
            else -> algorithmName
        }
        
        return AlgorithmResult(
            name = displayName,
            steps = steps,
            metrics = metrics
        )
    }
    
    private fun calculateMetrics(steps: List<VisualizationStep<List<Int>>>, executionTime: Long): AlgorithmMetrics {
        var comparisons = 0
        var swaps = 0
        
        for (step in steps) {
            val description = step.description.lowercase()
            if (description.contains("comparando")) {
                comparisons++
            }
            if (description.contains("intercambiando")) {
                swaps++
            }
        }
        
        return AlgorithmMetrics(
            comparisons = comparisons,
            swaps = swaps,
            executionTimeMs = executionTime,
            totalSteps = steps.size
        )
    }
}
