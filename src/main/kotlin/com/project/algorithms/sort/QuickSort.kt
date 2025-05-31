package com.project.algorithms.sort

import com.project.models.VisualizationStep

class QuickSort : SortAlgorithm {
    override fun name(): String = "Quick Sort"
    
    override fun <T : Comparable<T>> sort(array: MutableList<T>): List<VisualizationStep<List<T>>> {
        val steps = mutableListOf<VisualizationStep<List<T>>>()
        
        // Agregar estado inicial
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Estado inicial del arreglo"
        ))
        
        quickSortRecursive(array, 0, array.size - 1, steps)
        
        // Agregar estado final
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Arreglo ordenado completamente"
        ))
        
        return steps
    }
    
    private fun <T : Comparable<T>> quickSortRecursive(
        array: MutableList<T>, 
        low: Int, 
        high: Int, 
        steps: MutableList<VisualizationStep<List<T>>>
    ) {
        if (low < high) {
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Ordenando subarray desde índice $low hasta $high",
                highlightIndices = (low..high).toList()
            ))
            
            val pivotIndex = partition(array, low, high, steps)
            
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Pivot ${array[pivotIndex]} está en su posición final (índice $pivotIndex)",
                highlightIndices = listOf(pivotIndex)
            ))
            
            // Recursivamente ordenar elementos antes y después del pivot
            quickSortRecursive(array, low, pivotIndex - 1, steps)
            quickSortRecursive(array, pivotIndex + 1, high, steps)
        }
    }
    
    private fun <T : Comparable<T>> partition(
        array: MutableList<T>, 
        low: Int, 
        high: Int, 
        steps: MutableList<VisualizationStep<List<T>>>
    ): Int {
        val pivot = array[high]
        
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Seleccionando pivot: ${pivot} (índice $high)",
            highlightIndices = listOf(high)
        ))
        
        var i = low - 1
        
        for (j in low until high) {
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Comparando ${array[j]} con pivot $pivot",
                highlightIndices = listOf(j, high)
            ))
            
            if (array[j] <= pivot) {
                i++
                if (i != j) {
                    // Intercambiar elementos
                    val temp = array[i]
                    array[i] = array[j]
                    array[j] = temp
                    
                    steps.add(VisualizationStep(
                        data = array.toList(),
                        description = "Intercambiando ${array[i]} y ${array[j]} (posiciones $i y $j)",
                        highlightIndices = listOf(i, j)
                    ))
                }
            }
        }
        
        // Colocar el pivot en su posición correcta
        val temp = array[i + 1]
        array[i + 1] = array[high]
        array[high] = temp
        
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Colocando pivot $pivot en su posición correcta (índice ${i + 1})",
            highlightIndices = listOf(i + 1, high)
        ))
        
        return i + 1
    }
}
