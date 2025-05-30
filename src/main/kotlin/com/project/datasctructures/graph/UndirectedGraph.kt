package com.project.datastructures.graph

import com.project.models.VisualizationStep

class UndirectedGraph<T> : Graph<T> {
    private val nodes = mutableListOf<GraphNode<T>>()
    private val edges = mutableListOf<GraphEdge>()
    
    override fun addNode(value: T): List<VisualizationStep<GraphData<T>>> {
        val steps = mutableListOf<VisualizationStep<GraphData<T>>>()
        
        steps.add(VisualizationStep(
            data = getGraphData(),
            description = "Estado inicial del grafo"
        ))
        
        val newNodeId = nodes.size
        nodes.add(GraphNode(id = newNodeId, value = value))
        
        steps.add(VisualizationStep(
            data = getGraphData(),
            description = "Agregando nodo con valor $value e ID $newNodeId"
        ))
        
        return steps
    }
    
    override fun addEdge(fromId: Int, toId: Int, weight: Double): List<VisualizationStep<GraphData<T>>> {
        val steps = mutableListOf<VisualizationStep<GraphData<T>>>()
        
        steps.add(VisualizationStep(
            data = getGraphData(),
            description = "Estado inicial del grafo"
        ))
        
        if (fromId >= nodes.size || toId >= nodes.size) {
            steps.add(VisualizationStep(
                data = getGraphData(),
                description = "Error: ID de nodo inválido"
            ))
            return steps
        }
        
        // En un grafo no dirigido, agregamos dos aristas (en ambas direcciones)
        edges.add(GraphEdge(from = fromId, to = toId, weight = weight))
        edges.add(GraphEdge(from = toId, to = fromId, weight = weight))
        
        steps.add(VisualizationStep(
            data = getGraphData(),
            description = "Agregando arista no dirigida entre nodos $fromId y $toId con peso $weight"
        ))
        
        return steps
    }
    
    override fun getGraphData(): GraphData<T> {
        return GraphData(
            nodes = nodes.toList(),
            edges = edges.toList(),
            directed = false
        )
    }
}
