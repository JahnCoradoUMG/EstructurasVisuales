package com.project.algorithms.graph

import com.project.models.VisualizationStep
import com.project.datastructures.graph.GraphData

class DepthFirstSearch : GraphAlgorithm {
    override fun name(): String = "Depth-First Search (DFS)"
    
    override fun <T> execute(graph: GraphData<T>, startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<T>>> {
        val steps = mutableListOf<VisualizationStep<GraphAlgorithmResult<T>>>()
        val visited = mutableSetOf<Int>()
        val stack = mutableListOf<Int>()
        val path = mutableListOf<Int>()
        
        // Estado inicial
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                stack = stack.toList()
            ),
            description = "Estado inicial del grafo. Comenzaremos DFS desde el nodo $startNodeId"
        ))
        
        stack.add(startNodeId)
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                stack = stack.toList()
            ),
            description = "Agregamos el nodo inicial $startNodeId al stack"
        ))
        
        while (stack.isNotEmpty()) {
            val currentNode = stack.removeAt(stack.size - 1)
            
            steps.add(VisualizationStep(
                data = GraphAlgorithmResult(
                    graph = graph,
                    visitedNodes = visited.toList(),
                    currentNode = currentNode,
                    path = path.toList(),
                    stack = stack.toList()
                ),
                description = "Sacamos el nodo $currentNode del stack"
            ))
            
            if (currentNode !in visited) {
                visited.add(currentNode)
                path.add(currentNode)
                
                steps.add(VisualizationStep(
                    data = GraphAlgorithmResult(
                        graph = graph,
                        visitedNodes = visited.toList(),
                        currentNode = currentNode,
                        path = path.toList(),
                        stack = stack.toList()
                    ),
                    description = "Visitamos el nodo $currentNode y lo agregamos al camino"
                ))
                
                if (endNodeId != null && currentNode == endNodeId) {
                    steps.add(VisualizationStep(
                        data = GraphAlgorithmResult(
                            graph = graph,
                            visitedNodes = visited.toList(),
                            currentNode = currentNode,
                            path = path.toList(),
                            stack = stack.toList()
                        ),
                        description = "¡Encontramos el nodo objetivo $endNodeId!"
                    ))
                    break
                }
                
                // Agregar vecinos al stack (en orden inverso para mantener el orden correcto)
                val neighbors = graph.edges
                    .filter { it.from == currentNode }
                    .map { it.to }
                    .filter { it !in visited }
                    .reversed()
                
                for (neighbor in neighbors) {
                    if (neighbor !in stack) {
                        stack.add(neighbor)
                    }
                }
                
                if (neighbors.isNotEmpty()) {
                    steps.add(VisualizationStep(
                        data = GraphAlgorithmResult(
                            graph = graph,
                            visitedNodes = visited.toList(),
                            currentNode = currentNode,
                            path = path.toList(),
                            stack = stack.toList()
                        ),
                        description = "Agregamos los vecinos no visitados de $currentNode al stack: ${neighbors.joinToString(", ")}"
                    ))
                }
            }
        }
        
        steps.add(VisualizationStep(
            data = GraphAlgorithmResult(
                graph = graph,
                visitedNodes = visited.toList(),
                currentNode = null,
                path = path.toList(),
                stack = stack.toList()
            ),
            description = "DFS completado. Nodos visitados en orden: ${path.joinToString(" → ")}"
        ))
        
        return steps
    }
}
