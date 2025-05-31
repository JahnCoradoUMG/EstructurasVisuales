package com.project.services

import com.project.algorithms.graph.*
import com.project.datastructures.graph.*
import com.project.models.VisualizationStep

class GraphAlgorithmService {
    private val dfs = DepthFirstSearch()
    private val bfs = BreadthFirstSearch()
    private val dijkstra = DijkstraAlgorithm()
    private val sampleGraph = DirectedGraph<String>()
    
    init {
        // Crear un grafo de ejemplo
        sampleGraph.addNode("A")
        sampleGraph.addNode("B")
        sampleGraph.addNode("C")
        sampleGraph.addNode("D")
        sampleGraph.addNode("E")
        sampleGraph.addEdge(0, 1, 4.0)
        sampleGraph.addEdge(0, 2, 2.0)
        sampleGraph.addEdge(1, 3, 3.0)
        sampleGraph.addEdge(2, 1, 1.0)
        sampleGraph.addEdge(2, 3, 5.0)
        sampleGraph.addEdge(3, 4, 1.0)
    }
    
    fun executeDFS(startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<String>>> {
        return dfs.execute(sampleGraph.getGraphData(), startNodeId, endNodeId)
    }
    
    fun executeBFS(startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<String>>> {
        return bfs.execute(sampleGraph.getGraphData(), startNodeId, endNodeId)
    }
    
    fun executeDijkstra(startNodeId: Int, endNodeId: Int?): List<VisualizationStep<GraphAlgorithmResult<String>>> {
        return dijkstra.execute(sampleGraph.getGraphData(), startNodeId, endNodeId)
    }
    
    fun getSampleGraphData(): GraphData<String> {
        return sampleGraph.getGraphData()
    }
}
