package com.project.datastructures.linkedlist

import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
data class SinglyNode<T>(
    val value: T,
    var next: Int? = null
)

@Serializable
class SinglyLinkedList<T> {
    private val nodes = mutableListOf<SinglyNode<T>>()
    private var head: Int? = null
    private var tail: Int? = null
    
    fun addFirst(value: T): List<VisualizationStep<List<SinglyNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<SinglyNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista simple"
        ))
        
        val newNodeIndex = nodes.size
        val newNode = SinglyNode(value = value, next = head)
        nodes.add(newNode)
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Creando nuevo nodo con valor $value",
            highlightIndices = listOf(newNodeIndex)
        ))
        
        if (head == null) {
            tail = newNodeIndex
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista estaba vacía, el nuevo nodo es también el tail",
                highlightIndices = listOf(newNodeIndex)
            ))
        }
        
        head = newNodeIndex
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Actualizando head para que apunte al nuevo nodo",
            highlightIndices = listOf(head!!)
        ))
        
        return steps
    }
    
    fun addLast(value: T): List<VisualizationStep<List<SinglyNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<SinglyNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista simple"
        ))
        
        val newNodeIndex = nodes.size
        val newNode = SinglyNode(value = value, next = null)
        nodes.add(newNode)
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Creando nuevo nodo con valor $value",
            highlightIndices = listOf(newNodeIndex)
        ))
        
        if (tail != null) {
            nodes[tail!!] = nodes[tail!!].copy(next = newNodeIndex)
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Actualizando el nodo tail para que su next apunte al nuevo nodo",
                highlightIndices = listOf(tail!!, newNodeIndex)
            ))
        } else {
            head = newNodeIndex
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista estaba vacía, el nuevo nodo es también el head",
                highlightIndices = listOf(newNodeIndex)
            ))
        }
        
        tail = newNodeIndex
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Actualizando tail para que apunte al nuevo nodo",
            highlightIndices = listOf(tail!!)
        ))
        
        return steps
    }
    
    fun removeFirst(): List<VisualizationStep<List<SinglyNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<SinglyNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista simple"
        ))
        
        if (head == null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista está vacía, no se puede eliminar"
            ))
            return steps
        }
        
        val oldHead = head!!
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Eliminando el nodo head",
            highlightIndices = listOf(oldHead)
        ))
        
        head = nodes[oldHead].next
        
        if (head == null) {
            tail = null
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista quedó vacía, tail también es null"
            ))
        } else {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Actualizando head al siguiente nodo",
                highlightIndices = listOf(head!!)
            ))
        }
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado final de la lista después de eliminar",
            highlightIndices = if (head != null) listOf(head!!) else emptyList()
        ))
        
        return steps
    }

    fun search(value: T): List<VisualizationStep<List<SinglyNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<SinglyNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Iniciando búsqueda del valor $value en lista simple"
        ))
        
        if (head == null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista está vacía, elemento no encontrado"
            ))
            return steps
        }
        
        var current = head
        var position = 0
        
        while (current != null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Comparando con el nodo en posición $position (valor: ${nodes[current].value})",
                highlightIndices = listOf(current)
            ))
            
            if (nodes[current].value == value) {
                steps.add(VisualizationStep(
                    data = nodes.toList(),
                    description = "¡Elemento encontrado en la posición $position!",
                    highlightIndices = listOf(current)
                ))
                return steps
            }
            
            current = nodes[current].next
            position++
        }
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Elemento $value no encontrado en la lista simple"
        ))
        
        return steps
    }

    fun removeByValue(value: T): List<VisualizationStep<List<SinglyNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<SinglyNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Iniciando eliminación del valor $value en lista simple"
        ))
        
        if (head == null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista está vacía, no se puede eliminar"
            ))
            return steps
        }
        
        // Caso especial: eliminar el primer nodo
        if (nodes[head!!].value == value) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "El valor a eliminar está en el head",
                highlightIndices = listOf(head!!)
            ))
            return removeFirst()
        }
        
        var current = head
        var position = 0
        
        while (current != null && nodes[current].next != null) {
            val nextIndex = nodes[current].next!!
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Revisando si el siguiente nodo tiene el valor $value",
                highlightIndices = listOf(current, nextIndex)
            ))
            
            if (nodes[nextIndex].value == value) {
                steps.add(VisualizationStep(
                    data = nodes.toList(),
                    description = "Valor encontrado, actualizando enlaces",
                    highlightIndices = listOf(nextIndex)
                ))
                
                nodes[current] = nodes[current].copy(next = nodes[nextIndex].next)
                
                if (tail == nextIndex) {
                    tail = current
                }
                
                steps.add(VisualizationStep(
                    data = nodes.toList(),
                    description = "Nodo eliminado exitosamente de la lista simple",
                    highlightIndices = listOf(current)
                ))
                
                return steps
            }
            
            current = nodes[current].next
            position++
        }
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Valor $value no encontrado en la lista simple"
        ))
        
        return steps
    }
    
    fun getNodesForVisualization(): List<SinglyNode<T>> {
        return nodes.toList()
    }
    
    fun getHeadIndex(): Int? = head
    
    fun getTailIndex(): Int? = tail
}
