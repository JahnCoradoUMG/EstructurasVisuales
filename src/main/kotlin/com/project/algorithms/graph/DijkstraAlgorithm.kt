package com.project.algorithms.graph

import com.project.models.VisualizationStep
import com.project.datastructures.graph.GraphData

class DijkstraAlgorithm : GraphAlgorithm {
    override fun name(): String = "Dijkstra's Shortest Path"
    
    override fun <T> execute(graph: GraphData<T>, startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<T>>> {
        val steps = mutableListOf<VisualizationStep<GraphAlgorithmResult<T>>>()
        val distances = mutableMapOf<Int, Double>()
        val visited = mutableSetOf<Int>()
        val path = mutableListOf<Int>()
        val previous = mutableMapOf<Int, Int?>()
        val infinity = 999999.0
        
        // Inicializar distancias
        for (node in graph.nodes) {
            distances[node.id] = if (node.id == startNodeId) 0.0 else infinity
            previous[node.id] = null
        }
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                distances = distances.toMap()
            ),
            description = "Inicializamos las distancias. Nodo $startNodeId tiene distancia 0, los demás infinito"
        ))
        
        while (visited.size < graph.nodes.size) {
            // Encontrar el nodo no visitado con menor distancia
            val currentNode = distances
                .filter { it.key !in visited }
                .minByOrNull { it.value }
                ?.key
            
            if (currentNode == null || distances[currentNode] == infinity) {
                break
            }
            
            visited.add(currentNode)
            path.add(currentNode)
            
            steps.add(VisualizationStep(
                data = GraphAlgorithmResult(
                    graph = graph,
                    visitedNodes = visited.toList(),
                    currentNode = currentNode,
                    path = path.toList(),
                    distances = distances.toMap()
                ),
                description = "Visitamos el nodo $currentNode (distancia: ${distances[currentNode]})"
            ))
            
            if (endNodeId != null && currentNode == endNodeId) {
                // Reconstruir el camino más corto
                val shortestPath = mutableListOf<Int>()
                var current: Int? = endNodeId
                while (current != null) {
                    shortestPath.add(0, current)
                    current = previous[current]
                }
                
                steps.add(VisualizationStep(
                    data = GraphAlgorithmResult(
                        graph = graph,
                        visitedNodes = visited.toList(),
                        currentNode = currentNode,
                        path = shortestPath,
                        distances = distances.toMap()
                    ),
                    description = "¡Encontramos el camino más corto a $endNodeId! Distancia: ${distances[endNodeId]}, Camino: ${shortestPath.joinToString(" → ")}"
                ))
                break
            }
            
            // Actualizar distancias de los vecinos
            val neighbors = graph.edges.filter { it.from == currentNode }
            var updatedAny = false
            
            for (edge in neighbors) {
                val neighbor = edge.to
                if (neighbor !in visited) {
                    val newDistance = distances[currentNode]!! + edge.weight
                    if (newDistance < distances[neighbor]!!) {
                        distances[neighbor] = newDistance
                        previous[neighbor] = currentNode
                        updatedAny = true
                    }
                }
            }
            
            if (updatedAny) {
                steps.add(VisualizationStep(
                    data = GraphAlgorithmResult(
                        graph = graph,
                        visitedNodes = visited.toList(),
                        currentNode = currentNode,
                        path = path.toList(),
                        distances = distances.toMap()
                    ),
                    description = "Actualizamos las distancias de los vecinos de $currentNode"
                ))
            }
        }
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                distances = distances.toMap()
            ),
            description = "Algoritmo de Dijkstra completado"
        ))
        
        return steps
    }
}
