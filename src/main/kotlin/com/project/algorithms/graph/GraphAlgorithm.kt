package com.project.algorithms.graph

import com.project.models.VisualizationStep
import com.project.datastructures.graph.GraphData

interface GraphAlgorithm {
    fun <T> execute(graph: GraphData<T>, startNodeId: Int, endNodeId: Int? = null): List<VisualizationStep<GraphAlgorithmResult<T>>>
    fun name(): String
}
