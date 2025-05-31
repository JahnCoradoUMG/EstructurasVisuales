package com.project.algorithms.graph

import com.project.models.VisualizationStep
import com.project.datastructures.graph.GraphData

class BreadthFirstSearch : GraphAlgorithm {
    override fun name(): String = "Breadth-First Search (BFS)"
    
    override fun <T> execute(graph: GraphData<T>, startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<T>>> {
        val steps = mutableListOf<VisualizationStep<GraphAlgorithmResult<T>>>()
        val visited = mutableSetOf<Int>()
        val queue = mutableListOf<Int>()
        val path = mutableListOf<Int>()
        
        // Estado inicial
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                queue = queue.toList()
            ),
            description = "Estado inicial del grafo. Comenzaremos BFS desde el nodo $startNodeId"
        ))
        
        queue.add(startNodeId)
        visited.add(startNodeId)
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                queue = queue.toList()
            ),
            description = "Agregamos el nodo inicial $startNodeId a la cola y lo marcamos como visitado"
        ))
        
        while (queue.isNotEmpty()) {
            val currentNode = queue.removeAt(0)
            path.add(currentNode)
            
            steps.add(VisualizationStep(
                data = GraphAlgorithmResult(
                    graph = graph,
                    visitedNodes = visited.toList(),
                    currentNode = currentNode,
                    path = path.toList(),
                    queue = queue.toList()
                ),
                description = "Procesamos el nodo $currentNode (sacado de la cola)"
            ))
            
            if (endNodeId != null && currentNode == endNodeId) {
                steps.add(VisualizationStep(
                    data = GraphAlgorithmResult(
                        graph = graph,
                        visitedNodes = visited.toList(),
                        currentNode = currentNode,
                        path = path.toList(),
                        queue = queue.toList()
                    ),
                    description = "¡Encontramos el nodo objetivo $endNodeId!"
                ))
                break
            }
            
            // Agregar vecinos no visitados a la cola
            val neighbors = graph.edges
                .filter { it.from == currentNode }
                .map { it.to }
                .filter { it !in visited }
            
            for (neighbor in neighbors) {
                visited.add(neighbor)
                queue.add(neighbor)
            }
            
            if (neighbors.isNotEmpty()) {
                steps.add(VisualizationStep(
                    data = GraphAlgorithmResult(
                        graph = graph,
                        visitedNodes = visited.toList(),
                        currentNode = currentNode,
                        path = path.toList(),
                        queue = queue.toList()
                    ),
                    description = "Agregamos los vecinos no visitados de $currentNode a la cola: ${neighbors.joinToString(", ")}"
                ))
            }
        }
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                queue = queue.toList()
            ),
            description = "BFS completado. Nodos visitados en orden: ${path.joinToString(" → ")}"
        ))
        
        return steps
    }
}
