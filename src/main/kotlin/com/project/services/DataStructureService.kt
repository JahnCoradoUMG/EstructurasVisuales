package com.project.services

import com.project.datastructures.linkedlist.DoublyLinkedList
import com.project.datastructures.linkedlist.Node
import com.project.datastructures.linkedlist.SinglyLinkedList
import com.project.datastructures.linkedlist.SinglyNode
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

    fun removeNodesFromDoublyList(): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.removeNodes()
    }
    
    fun searchInLinkedList(value: Int): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.search(value)
    }

    fun removeByValueFromLinkedList(value: Int): List<VisualizationStep<List<Node<Int>>>> {
        return doublyLinkedList.removeByValue(value)
    }
    
    fun getLinkedListData(): Pair<List<Node<Int>>, Pair<Int?, Int?>> {
        return Pair(doublyLinkedList.getNodesForVisualization(), Pair(doublyLinkedList.getHeadIndex(), doublyLinkedList.getTailIndex()))
    }
    
    // Lista simplemente enlazada
    private val singlyLinkedList = SinglyLinkedList<Int>()
    
    fun addFirstToSinglyLinkedList(value: Int): List<VisualizationStep<List<SinglyNode<Int>>>> {
        return singlyLinkedList.addFirst(value)
    }
    
    fun addLastToSinglyLinkedList(value: Int): List<VisualizationStep<List<SinglyNode<Int>>>> {
        return singlyLinkedList.addLast(value)
    }
    
    fun removeFirstFromSinglyLinkedList(): List<VisualizationStep<List<SinglyNode<Int>>>> {
        return singlyLinkedList.removeFirst()
    }
    
    fun searchInSinglyLinkedList(value: Int): List<VisualizationStep<List<SinglyNode<Int>>>> {
        return singlyLinkedList.search(value)
    }

    fun removeByValueFromSinglyLinkedList(value: Int): List<VisualizationStep<List<SinglyNode<Int>>>> {
        return singlyLinkedList.removeByValue(value)
    }
    
    fun getSinglyLinkedListData(): Pair<List<SinglyNode<Int>>, Pair<Int?, Int?>> {
        return Pair(singlyLinkedList.getNodesForVisualization(), Pair(singlyLinkedList.getHeadIndex(), singlyLinkedList.getTailIndex()))
    }
    
    // Árbol binario
    private val binaryTree = BinaryTree<Int>()

    fun removeNodesFromBinaryTree(): List<VisualizationStep<List<TreeNode<Int>>>> {
        return binaryTree.removeNodes()
    }
    
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
