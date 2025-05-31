package com.project.datastructures.tree

import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
class BinaryTree<T : Comparable<T>> {
    private val nodes = mutableListOf<TreeNode<T>>()
    private var root: Int? = null
    
    fun insert(value: T): List<VisualizationStep<List<TreeNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<TreeNode<T>>>>()
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Estado inicial del árbol"
        ))
        
        if (root == null) {
            val newNodeIndex = nodes.size
            nodes.add(TreeNode(value = value))
            root = newNodeIndex
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Insertando $value como raíz del árbol",
                highlightIndices = listOf(newNodeIndex)
            ))
            
            return steps
        }
        
        val path = mutableListOf<Int>()
        var current = root
        
        while (current != null) {
            path.add(current)
            
            steps.add(VisualizationStep(
                data = nodes.toList(),
                description = "Comparando $value con ${nodes[current].value}",
                highlightIndices = path
            ))
            
            if (value < nodes[current].value) {
                if (nodes[current].leftIndex == null) {
                    val newNodeIndex = nodes.size
                    nodes.add(TreeNode(value = value))
                    nodes[current] = nodes[current].copy(leftIndex = newNodeIndex)
                    
                    steps.add(VisualizationStep(
                        data = nodes.toList(),
                        description = "$value es menor que ${nodes[current].value}, insertando a la izquierda",
                        highlightIndices = path + newNodeIndex
                    ))
                    
                    break
                }
                current = nodes[current].leftIndex
            } else {
                if (nodes[current].rightIndex == null) {
                    val newNodeIndex = nodes.size
                    nodes.add(TreeNode(value = value))
                    nodes[current] = nodes[current].copy(rightIndex = newNodeIndex)
                    
                    steps.add(VisualizationStep(
                        data = nodes.toList(),
                        description = "$value es mayor o igual que ${nodes[current].value}, insertando a la derecha",
                        highlightIndices = path + newNodeIndex
                    ))
                    
                    break
                }
                current = nodes[current].rightIndex
            }
        }
        
        steps.add(VisualizationStep(
            data = nodes.toList(),
            description = "Árbol después de insertar $value"
        ))
        
        return steps
    }

    fun removeNodes(): List<VisualizationStep<List<TreeNode<T>>>> {
        val steps = mutableListOf<VisualizationStep<List<TreeNode<T>>>>()

        steps.add(
            VisualizationStep(
                data = nodes.toList(),
                description = "Estado actual del árbol antes de eliminar los nodos"
            )
        )

        nodes.clear()
        root = null

        steps.add(
            VisualizationStep(
                data = emptyList(),
                description = "Todos los nodos del árbol han sido eliminados"
            )
        )

        return steps
    }
    
    fun getNodesForVisualization(): List<TreeNode<T>> {
        return nodes.toList()
    }
    
    fun getRootIndex(): Int? = root
}
