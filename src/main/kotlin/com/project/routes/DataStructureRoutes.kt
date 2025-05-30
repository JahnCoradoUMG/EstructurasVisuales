package com.project.routes

import com.project.services.DataStructureService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable

@Serializable
data class LinkedListRequest(val value: Int)

@Serializable
data class TreeRequest(val value: Int)

@Serializable
data class GraphNodeRequest(val value: String)

@Serializable
data class GraphEdgeRequest(val fromId: Int, val toId: Int, val weight: Double = 1.0)

@Serializable
data class PriorityQueueRequest(val value: String, val priority: Int)

fun Route.dataStructureRoutes() {
    val dataStructureService = DataStructureService()
    
    route("/data-structures") {
        get {
            call.respondHtml {
                head {
                    title("Estructuras de Datos")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                }
                body {
                    h1 { +"Estructuras de Datos" }
                    ul {
                        li {
                            a(href = "/data-structures/linked-list") {
                                +"Lista Doblemente Enlazada"
                            }
                        }
                        li {
                            a(href = "/data-structures/binary-tree") {
                                +"Árbol Binario"
                            }
                        }
                        li {
                            a(href = "/data-structures/directed-graph") {
                                +"Grafo Dirigido"
                            }
                        }
                        li {
                            a(href = "/data-structures/undirected-graph") {
                                +"Grafo No Dirigido"
                            }
                        }
                        li {
                            a(href = "/data-structures/priority-queue") {
                                +"Cola de Prioridad"
                            }
                        }
                    }
                }
            }
        }
        
        // Lista doblemente enlazada
        get("/linked-list") {
            call.respondHtml {
                head {
                    title("Lista Doblemente Enlazada")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    h1 { +"Lista Doblemente Enlazada" }
                    p { +"Una lista doblemente enlazada es una estructura de datos lineal que contiene un conjunto de nodos donde cada nodo tiene referencias al nodo anterior y al siguiente." }
                    
                    div {
                        id = "input-container"
                        label {
                            htmlFor = "value-input"
                            +"Valor: "
                        }
                        input {
                            id = "value-input"
                            type = InputType.number
                            value = "42"
                        }
                        button {
                            id = "add-first-button"
                            +"Agregar al Inicio"
                        }
                        button {
                            id = "add-last-button"
                            +"Agregar al Final"
                        }
                        button {
                            id = "remove-first-button"
                            +"Eliminar del Inicio"
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "linked-list-container"
                            classes = setOf("linked-list-container")
                        }
                        div {
                            id = "description"
                            classes = setOf("description")
                        }
                        div {
                            id = "controls"
                            classes = setOf("controls")
                            button {
                                id = "prev-step"
                                +"Anterior"
                            }
                            button {
                                id = "next-step"
                                +"Siguiente"
                            }
                            button {
                                id = "play-pause"
                                +"Reproducir"
                            }
                        }
                    }
                    
                    script(src = "/static/js/linked-list.js") {}
                }
            }
        }
        
        post("/linked-list/add-first") {
            val request = call.receive<LinkedListRequest>()
            val steps = dataStructureService.addFirstToLinkedList(request.value)
            call.respond(steps)
        }
        
        post("/linked-list/add-last") {
            val request = call.receive<LinkedListRequest>()
            val steps = dataStructureService.addLastToLinkedList(request.value)
            call.respond(steps)
        }
        
        post("/linked-list/remove-first") {
            val steps = dataStructureService.removeFirstFromLinkedList()
            call.respond(steps)
        }
        
        get("/linked-list/data") {
            val data = dataStructureService.getLinkedListData()
            call.respond(data)
        }
        
        // Árbol binario
        get("/binary-tree") {
            call.respondHtml {
                head {
                    title("Árbol Binario")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    h1 { +"Árbol Binario" }
                    p { +"Un árbol binario es una estructura de datos jerárquica donde cada nodo tiene como máximo dos hijos, referidos como hijo izquierdo y derecho." }
                    
                    div {
                        id = "input-container"
                        label {
                            htmlFor = "value-input"
                            +"Valor: "
                        }
                        input {
                            id = "value-input"
                            type = InputType.number
                            value = "50"
                        }
                        button {
                            id = "insert-button"
                            +"Insertar"
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "tree-container"
                            classes = setOf("tree-container")
                        }
                        div {
                            id = "description"
                            classes = setOf("description")
                        }
                        div {
                            id = "controls"
                            classes = setOf("controls")
                            button {
                                id = "prev-step"
                                +"Anterior"
                            }
                            button {
                                id = "next-step"
                                +"Siguiente"
                            }
                            button {
                                id = "play-pause"
                                +"Reproducir"
                            }
                        }
                    }
                    
                    script(src = "/static/js/binary-tree.js") {}
                }
            }
        }
        
        post("/binary-tree/insert") {
            val request = call.receive<TreeRequest>()
            val steps = dataStructureService.insertIntoBinaryTree(request.value)
            call.respond(steps)
        }
        
        get("/binary-tree/data") {
            val data = dataStructureService.getBinaryTreeData()
            call.respond(data)
        }
        
        // Grafo dirigido
        get("/directed-graph") {
            call.respondHtml {
                head {
                    title("Grafo Dirigido")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    h1 { +"Grafo Dirigido" }
                    p { +"Un grafo dirigido es una estructura de datos que consiste en un conjunto de nodos y aristas, donde cada arista tiene una dirección específica." }
                    
                    div {
                        id = "input-container"
                        div {
                            id = "node-input"
                            label {
                                htmlFor = "node-value-input"
                                +"Valor del Nodo: "
                            }
                            input {
                                id = "node-value-input"
                                type = InputType.text
                                value = "A"
                            }
                            button {
                                id = "add-node-button"
                                +"Agregar Nodo"
                            }
                        }
                        div {
                            id = "edge-input"
                            label {
                                htmlFor = "from-id-input"
                                +"Desde ID: "
                            }
                            input {
                                id = "from-id-input"
                                type = InputType.number
                                value = "0"
                            }
                            label {
                                htmlFor = "to-id-input"
                                +"Hacia ID: "
                            }
                            input {
                                id = "to-id-input"
                                type = InputType.number
                                value = "1"
                            }
                            label {
                                htmlFor = "weight-input"
                                +"Peso: "
                            }
                            input {
                                id = "weight-input"
                                type = InputType.number
                                value = "1.0"
                                step = "0.1"
                            }
                            button {
                                id = "add-edge-button"
                                +"Agregar Arista"
                            }
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "graph-container"
                            classes = setOf("graph-container")
                        }
                        div {
                            id = "description"
                            classes = setOf("description")
                        }
                        div {
                            id = "controls"
                            classes = setOf("controls")
                            button {
                                id = "prev-step"
                                +"Anterior"
                            }
                            button {
                                id = "next-step"
                                +"Siguiente"
                            }
                            button {
                                id = "play-pause"
                                +"Reproducir"
                            }
                        }
                    }
                    
                    script(src = "/static/js/directed-graph.js") {}
                }
            }
        }
        
        post("/directed-graph/add-node") {
            val request = call.receive<GraphNodeRequest>()
            val steps = dataStructureService.addNodeToDirectedGraph(request.value)
            call.respond(steps)
        }
        
        post("/directed-graph/add-edge") {
            val request = call.receive<GraphEdgeRequest>()
            val steps = dataStructureService.addEdgeToDirectedGraph(request.fromId, request.toId, request.weight)
            call.respond(steps)
        }
        
        get("/directed-graph/data") {
            val data = dataStructureService.getDirectedGraphData()
            call.respond(data)
        }
        
        // Grafo no dirigido
        get("/undirected-graph") {
            call.respondHtml {
                head {
                    title("Grafo No Dirigido")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    h1 { +"Grafo No Dirigido" }
                    p { +"Un grafo no dirigido es una estructura de datos que consiste en un conjunto de nodos y aristas, donde las aristas no tienen dirección." }
                    
                    div {
                        id = "input-container"
                        div {
                            id = "node-input"
                            label {
                                htmlFor = "node-value-input"
                                +"Valor del Nodo: "
                            }
                            input {
                                id = "node-value-input"
                                type = InputType.text
                                value = "A"
                            }
                            button {
                                id = "add-node-button"
                                +"Agregar Nodo"
                            }
                        }
                        div {
                            id = "edge-input"
                            label {
                                htmlFor = "from-id-input"
                                +"Nodo 1 ID: "
                            }
                            input {
                                id = "from-id-input"
                                type = InputType.number
                                value = "0"
                            }
                            label {
                                htmlFor = "to-id-input"
                                +"Nodo 2 ID: "
                            }
                            input {
                                id = "to-id-input"
                                type = InputType.number
                                value = "1"
                            }
                            label {
                                htmlFor = "weight-input"
                                +"Peso: "
                            }
                            input {
                                id = "weight-input"
                                type = InputType.number
                                value = "1.0"
                                step = "0.1"
                            }
                            button {
                                id = "add-edge-button"
                                +"Agregar Arista"
                            }
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "graph-container"
                            classes = setOf("graph-container")
                        }
                        div {
                            id = "description"
                            classes = setOf("description")
                        }
                        div {
                            id = "controls"
                            classes = setOf("controls")
                            button {
                                id = "prev-step"
                                +"Anterior"
                            }
                            button {
                                id = "next-step"
                                +"Siguiente"
                            }
                            button {
                                id = "play-pause"
                                +"Reproducir"
                            }
                        }
                    }
                    
                    script(src = "/static/js/undirected-graph.js") {}
                }
            }
        }
        
        post("/undirected-graph/add-node") {
            val request = call.receive<GraphNodeRequest>()
            val steps = dataStructureService.addNodeToUndirectedGraph(request.value)
            call.respond(steps)
        }
        
        post("/undirected-graph/add-edge") {
            val request = call.receive<GraphEdgeRequest>()
            val steps = dataStructureService.addEdgeToUndirectedGraph(request.fromId, request.toId, request.weight)
            call.respond(steps)
        }
        
        get("/undirected-graph/data") {
            val data = dataStructureService.getUndirectedGraphData()
            call.respond(data)
        }
        
        // Cola de prioridad
        get("/priority-queue") {
            call.respondHtml {
                head {
                    title("Cola de Prioridad")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    h1 { +"Cola de Prioridad" }
                    p { +"Una cola de prioridad es una estructura de datos similar a una cola, pero donde cada elemento tiene asociada una prioridad y los elementos se atienden según su prioridad." }
                    
                    div {
                        id = "input-container"
                        label {
                            htmlFor = "value-input"
                            +"Valor: "
                        }
                        input {
                            id = "value-input"
                            type = InputType.text
                            value = "Tarea A"
                        }
                        label {
                            htmlFor = "priority-input"
                            +"Prioridad: "
                        }
                        input {
                            id = "priority-input"
                            type = InputType.number
                            value = "1"
                        }
                        button {
                            id = "enqueue-button"
                            +"Encolar"
                        }
                        button {
                            id = "dequeue-button"
                            +"Desencolar"
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "queue-container"
                            classes = setOf("queue-container")
                        }
                        div {
                            id = "description"
                            classes = setOf("description")
                        }
                        div {
                            id = "controls"
                            classes = setOf("controls")
                            button {
                                id = "prev-step"
                                +"Anterior"
                            }
                            button {
                                id = "next-step"
                                +"Siguiente"
                            }
                            button {
                                id = "play-pause"
                                +"Reproducir"
                            }
                        }
                    }
                    
                    script(src = "/static/js/priority-queue.js") {}
                }
            }
        }
        
        post("/priority-queue/enqueue") {
            val request = call.receive<PriorityQueueRequest>()
            val steps = dataStructureService.enqueueToPriorityQueue(request.value, request.priority)
            call.respond(steps)
        }
        
        post("/priority-queue/dequeue") {
            val steps = dataStructureService.dequeueFromPriorityQueue()
            call.respond(steps)
        }
        
        get("/priority-queue/data") {
            val data = dataStructureService.getPriorityQueueData()
            call.respond(data)
        }
    }
}
