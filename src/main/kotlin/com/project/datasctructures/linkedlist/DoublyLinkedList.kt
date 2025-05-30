package com.project.datastructures.linkedlist

import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
class DoublyLinkedList<T> {
    private val nodes = mutableListOf<Node<T>>()
    private var head: Int? = null
    private var tail: Int? = null
    
    fun addFirst(value: T): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista"
        ))
        
        val newNodeIndex = nodes.size
        val newNode = Node(value = value, prev = null, next = head)
        nodes.add(newNode)
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Creando nuevo nodo con valor $value",
            highlightIndices = listOf(newNodeIndex)
        ))
        
        if (head != null) {
            nodes[head!!] = nodes[head!!].copy(prev = newNodeIndex)
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Actualizando el nodo anterior head para que su prev apunte al nuevo nodo",
                highlightIndices = listOf(head!!, newNodeIndex)
            ))
        } else {
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
    
    fun addLast(value: T): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista"
        ))
        
        val newNodeIndex = nodes.size
        val newNode = Node(value = value, prev = tail, next = null)
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
                description = "Actualizando el nodo anterior tail para que su next apunte al nuevo nodo",
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
    
    fun removeFirst(): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial de la lista"
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
        
        if (head != null) {
            nodes[head!!] = nodes[head!!].copy(prev = null)
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Actualizando el nuevo head para que su prev sea null",
                highlightIndices = listOf(head!!)
            ))
        } else {
            tail = null
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista quedó vacía, tail también es null"
            ))
        }
        
        // No eliminamos realmente el nodo para mantener los índices consistentes en la visualización
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado final de la lista después de eliminar",
            highlightIndices = if (head != null) listOf(head!!) else emptyList()
        ))
        
        return steps
    }
    
    fun getNodesForVisualization(): List<Node<T>> {
        return nodes.toList()
    }
    
    fun getHeadIndex(): Int? = head
    
    fun getTailIndex(): Int? = tail
}
