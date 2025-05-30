package com.project.algorithms.sort

import com.project.models.VisualizationStep

class BubbleSort : SortAlgorithm {
    override fun name(): String = "Bubble Sort"
    
    override fun <T : Comparable<T>> sort(array: MutableList<T>): List<VisualizationStep<List<T>>> {
        val steps = mutableListOf<VisualizationStep<List<T>>>()
        
        // Agregar estado inicial
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Estado inicial del arreglo"
        ))
        
        val n = array.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                // Agregar paso de comparación
                steps.add(VisualizationStep(
                    data = array.toList(),
                    description = "Comparando elementos en posiciones $j y ${j + 1}",
                    highlightIndices = listOf(j, j + 1)
                ))
                
                if (array[j] > array[j + 1]) {
                    // Intercambiar elementos
                    val temp = array[j]
                    array[j] = array[j + 1]
                    array[j + 1] = temp
                    
                    // Agregar paso después del intercambio
                    steps.add(VisualizationStep(
                        data = array.toList(),
                        description = "Intercambiando elementos en posiciones $j y ${j + 1}",
                        highlightIndices = listOf(j, j + 1)
                    ))
                }
            }
            
            // Agregar paso después de cada iteración externa
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Elemento ${array[n - i - 1]} está en su posición final",
                highlightIndices = listOf(n - i - 1)
            ))
        }
        
        // Agregar estado final
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Arreglo ordenado"
        ))
        
        return steps
    }
}
