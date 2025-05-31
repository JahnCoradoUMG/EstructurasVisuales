package com.project.algorithms.graph

import com.project.datastructures.graph.GraphData
import kotlinx.serialization.Serializable

@Serializable
data class GraphAlgorithmResult<T>(
    val graph: GraphData<T>,
    val visitedNodes: List<Int> = emptyList(),
    val currentNode: Int? = null,
    val path: List<Int> = emptyList(),
    val distances: Map<Int, Double> = emptyMap(),
    val queue: List<Int> = emptyList(),
    val stack: List<Int> = emptyList()
)
