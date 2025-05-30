package com.project.datastructures.queue

import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
data class PriorityQueueItem<T>(
    val value: T,
    val priority: Int
)

class PriorityQueue<T> {
    private val items = mutableListOf<PriorityQueueItem<T>>()
    
    fun enqueue(value: T, priority: Int): List<VisualizationStep<List<PriorityQueueItem<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<PriorityQueueItem<T>>>>()
        
        steps.add(VisualizationStep(
            data = items.toList(),
            description = "Estado inicial de la cola de prioridad"
        ))
        
        val newItem = PriorityQueueItem(value, priority)
        items.add(newItem)
        
        steps.add(VisualizationStep(
            data = items.toList(),
            description = "Agregando elemento con valor $value y prioridad $priority",
            highlightIndices = listOf(items.size - 1)
        ))
        
        // Ordenar por prioridad (menor número = mayor prioridad)
        items.sortBy { it.priority }
        
        steps.add(VisualizationStep(
            data = items.toList(),
            description = "Reordenando la cola según prioridades"
        ))
        
        return steps
    }
    
    fun dequeue(): List<VisualizationStep<List<PriorityQueueItem<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<PriorityQueueItem<T>>>>()
        
        steps.add(VisualizationStep(
            data = items.toList(),
            description = "Estado inicial de la cola de prioridad"
        ))
        
        if (items.isEmpty()) {
            steps.add(VisualizationStep(
                data = items.toList(),
                description = "La cola está vacía, no se puede eliminar ningún elemento"
            ))
            return steps
        }
        
        val removedItem = items.removeAt(0)
        
        steps.add(VisualizationStep(
            data = items.toList(),
            description = "Eliminando elemento con mayor prioridad: valor ${removedItem.value}, prioridad ${removedItem.priority}"
        ))
        
        return steps
    }
    
    fun getItemsForVisualization(): List<PriorityQueueItem<T>> {
        return items.toList()
    }
}
