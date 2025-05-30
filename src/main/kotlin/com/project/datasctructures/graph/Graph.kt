package com.project.datastructures.graph

import com.project.models.VisualizationStep
import kotlinx.serialization.Serializable

@Serializable
data class GraphNode<T>(
    val id: Int,
    val value: T
)

@Serializable
data class GraphEdge(
    val from: Int,
    val to: Int,
    val weight: Double = 1.0
)

@Serializable
data class GraphData<T>(
    val nodes: List<GraphNode<T>>,
    val edges: List<GraphEdge>,
    val directed: Boolean
)

interface Graph<T> {
    fun addNode(value: T): List<VisualizationStep<GraphData<T>>>
    fun addEdge(fromId: Int, toId: Int, weight: Double = 1.0): List<VisualizationStep<GraphData<T>>>
    fun getGraphData(): GraphData<T>
}
