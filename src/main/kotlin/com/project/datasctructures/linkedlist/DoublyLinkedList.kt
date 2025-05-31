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

    fun search(value: T): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Iniciando búsqueda del valor $value"
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
            description = "Elemento $value no encontrado en la lista"
        ))
        
        return steps
    }

    fun removeByValue(value: T): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Iniciando eliminación del valor $value"
        ))
        
        if (head == null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "La lista está vacía, no se puede eliminar"
            ))
            return steps
        }
        
        var current = head
        var position = 0
        
        while (current != null) {
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Buscando el valor $value... Revisando posición $position",
                highlightIndices = listOf(current)
            ))
            
            if (nodes[current].value == value) {
                steps.add(VisualizationStep(
                    data = nodes.toList(),
                    description = "Valor encontrado en posición $position, procediendo a eliminar",
                    highlightIndices = listOf(current)
                ))
                
                // Actualizar enlaces
                val prevIndex = nodes[current].prev
                val nextIndex = nodes[current].next
                
                if (prevIndex != null) {
                    nodes[prevIndex] = nodes[prevIndex].copy(next = nextIndex)
                } else {
                    head = nextIndex
                }
                
                if (nextIndex != null) {
                    nodes[nextIndex] = nodes[nextIndex].copy(prev = prevIndex)
                } else {
                    tail = prevIndex
                }
                
                steps.add(VisualizationStep(
                    data = nodes.toList(),
                    description = "Nodo eliminado exitosamente. Enlaces actualizados.",
                    highlightIndices = if (nextIndex != null) listOf(nextIndex) else if (prevIndex != null) listOf(prevIndex) else emptyList()
                ))
                
                return steps
            }
            
            current = nodes[current].next
            position++
        }
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Valor $value no encontrado en la lista"
        ))
        
        return steps
    }

    fun size(): Int {
        var count = 0
        var current = head
        while (current != null) {
            count++
            current = nodes[current].next
        }
        return count
    }

    fun isEmpty(): Boolean = head == null
    

    fun removeNodes(): List<VisualizationStep<List<Node<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<Node<T>>>>()

        steps.add(
            VisualizationStep(
                data = nodes.toList(),
                description = "Estado inicial de la lista"
            )
        )

        if (head == null) {
            steps.add(
                VisualizationStep(
                    data = nodes.toList(),
                    description = "La lista ya está vacía"
                )
            )
            return steps
        }

        val indices = nodes.indices.toList()

        head = null
        tail = null

        steps.add(
            VisualizationStep(
                data = nodes.toList(),
                description = "Eliminando referencias a head y tail (lista vacía)",
                highlightIndices = indices
            )
        )

        steps.add(
            VisualizationStep(
                data = nodes.toList(),
                description = "Estado final después de eliminar todos los nodos"
            )
        )
        nodes.clear()
        return steps
    }
    
    fun getNodesForVisualization(): List<Node<T>> {
        return nodes.toList()
    }
    
    fun getHeadIndex(): Int? = head
    
    fun getTailIndex(): Int? = tail
}
