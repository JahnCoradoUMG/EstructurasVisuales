package com.project.algorithms.sort

import com.project.models.VisualizationStep

class SelectionSort : SortAlgorithm {
    override fun name(): String = "Selection Sort"
    
    override fun <T : Comparable<T>> sort(array: MutableList<T>): List<VisualizationStep<List<T>>> {
        val steps = mutableListOf<VisualizationStep<List<T>>>()
        
        // Agregar estado inicial
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Estado inicial del arreglo"
        ))
        
        val n = array.size
        
        for (i in 0 until n - 1) {
            var minIndex = i
            
            // Agregar paso al inicio de cada iteración externa
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Buscando el elemento mínimo en la parte no ordenada del arreglo (desde el índice $i)",
                highlightIndices = listOf(i)
            ))
            
            for (j in i + 1 until n) {
                // Agregar paso de comparación
                steps.add(VisualizationStep(
                    data = array.toList(),
                    description = "Comparando elementos en posiciones $minIndex y $j",
                    highlightIndices = listOf(minIndex, j)
                ))
                
                if (array[j] < array[minIndex]) {
                    minIndex = j
                    
                    // Agregar paso cuando se encuentra un nuevo mínimo
                    steps.add(VisualizationStep(
                        data = array.toList(),
                        description = "Nuevo mínimo encontrado en la posición $minIndex",
                        highlightIndices = listOf(minIndex)
                    ))
                }
            }
            
            // Intercambiar elementos si es necesario
            if (minIndex != i) {
                val temp = array[i]
                array[i] = array[minIndex]
                array[minIndex] = temp
                
                // Agregar paso después del intercambio
                steps.add(VisualizationStep(
                    data = array.toList(),
                    description = "Intercambiando elementos en posiciones $i y $minIndex",
                    highlightIndices = listOf(i, minIndex)
                ))
            }
            
            // Agregar paso después de cada iteración externa
            steps.add(VisualizationStep(
                data = array.toList(),
                description = "Elemento ${array[i]} está en su posición final",
                highlightIndices = listOf(i)
            ))
        }
        
        // Agregar estado final
        steps.add(VisualizationStep(
            data = array.toList(),
            description = "Arreglo ordenado",
            highlightIndices = (0 until n).toList()
        ))
        
        return steps
    }
}
