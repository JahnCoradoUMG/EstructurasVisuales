package com.project.services

import com.project.datastructures.linkedlist.DoublyLinkedList
import com.project.datastructures.linkedlist.Node
import com.project.datastructures.tree.BinaryTree
import com.project.datastructures.tree.TreeNode
import com.project.datastructures.graph.DirectedGraph
import com.project.datastructures.graph.UndirectedGraph
import com.project.datastructures.graph.GraphData
import com.project.datastructures.queue.PriorityQueue
import com.project.datastructures.queue.PriorityQueueItem
import com.project.models.VisualizationStep

class DataStructureService {
    // Lista doblemente enlazada
    private val doublyLinkedList = DoublyLinkedList<Int>()
    
    fun addFirstToLinkedList(value: Int): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.addFirst(value)
    }
    
    fun addLastToLinkedList(value: Int): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.addLast(value)
    }
    
    fun removeFirstFromLinkedList(): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.removeFirst()
    }
    
    fun getLinkedListData(): Pair<List<Node<Int>>, Pair<Int?, Int?>> {
        return Pair(doublyLinkedList.getNodesForVisualization(), Pair(doublyLinkedList.getHeadIndex(), doublyLinkedList.getTailIndex()))
    }
    
    // Árbol binario
    private val binaryTree = BinaryTree<Int>()
    
    fun insertIntoBinaryTree(value: Int): List<VisualizationStep<List<TreeNode<Int>>>> {
        return binaryTree.insert(value)
    }
    
    fun getBinaryTreeData(): Pair<List<TreeNode<Int>>, Int?> {
        return Pair(binaryTree.getNodesForVisualization(), binaryTree.getRootIndex())
    }
    
    // Grafo dirigido
    private val directedGraph = DirectedGraph<String>()
    
    fun addNodeToDirectedGraph(value: String): List<VisualizationStep<GraphData<String>>> {
        return directedGraph.addNode(value)
    }
    
    fun addEdgeToDirectedGraph(fromId: Int, toId: Int, weight: Double = 1.0): List<VisualizationStep<GraphData<String>>> {
        return directedGraph.addEdge(fromId, toId, weight)
    }
    
    fun getDirectedGraphData(): GraphData<String> {
        return directedGraph.getGraphData()
    }
    
    // Grafo no dirigido
    private val undirectedGraph = UndirectedGraph<String>()
    
    fun addNodeToUndirectedGraph(value: String): List<VisualizationStep<GraphData<String>>> {
        return undirectedGraph.addNode(value)
    }
    
    fun addEdgeToUndirectedGraph(fromId: Int, toId: Int, weight: Double = 1.0): List<VisualizationStep<GraphData<String>>> {
        return undirectedGraph.addEdge(fromId, toId, weight)
    }
    
    fun getUndirectedGraphData(): GraphData<String> {
        return undirectedGraph.getGraphData()
    }
    
    // Cola de prioridad
    private val priorityQueue = PriorityQueue<String>()
    
    fun enqueueToPriorityQueue(value: String, priority: Int): List<VisualizationStep<List<PriorityQueueItem<String>>>> {
        return priorityQueue.enqueue(value, priority)
    }
    
    fun dequeueFromPriorityQueue(): List<VisualizationStep<List<PriorityQueueItem<String>>>> {
        return priorityQueue.dequeue()
    }
    
    fun getPriorityQueueData(): List<PriorityQueueItem<String>> {
        return priorityQueue.getItemsForVisualization()
    }
}
