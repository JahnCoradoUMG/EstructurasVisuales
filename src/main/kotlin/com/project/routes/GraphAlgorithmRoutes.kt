package com.project.routes

import com.project.services.GraphAlgorithmService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable

@Serializable
data class GraphAlgorithmRequest(val startNodeId: Int, val endNodeId: Int? = null)

fun Route.graphAlgorithmRoutes() {
    val graphAlgorithmService = GraphAlgorithmService()
    
    route("/graph-algorithms") {
        get {
            call.respondHtml {
                head {
                    title("Algoritmos de Grafos")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        h1 { +"🔍 Algoritmos de Grafos" }
                        
                        div {
                            style = "text-align: center; margin-bottom: 2rem;"
                            p { 
                                style = "color: rgba(255,255,255,0.9); font-size: 1.2rem;"
                                +"Explora algoritmos de búsqueda y caminos más cortos en grafos" 
                            }
                        }
                        
                        div {
                            classes = setOf("menu")
                            h2 { +"🚀 Algoritmos Disponibles" }
                            ul {
                                li {
                                    a(href = "/graph-algorithms/dfs") {
                                        +"🌊 Búsqueda en Profundidad (DFS)"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Explora el grafo tan profundo como sea posible"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/graph-algorithms/bfs") {
                                        +"🌐 Búsqueda en Anchura (BFS)"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Explora el grafo nivel por nivel"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/graph-algorithms/dijkstra") {
                                        +"🎯 Algoritmo de Dijkstra"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Encuentra el camino más corto entre nodos"
                                        }
                                    }
                                }
                            }
                            
                            div {
                                style = "text-align: center; margin-top: 2rem;"
                                a(href = "/home") {
                                    style = "color:rgb(181, 190, 232); text-decoration: none; font-weight: 600;"
                                    +"← Volver al inicio"
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // DFS
        get("/dfs") {
            call.respondHtml {
                head {
                    title("Búsqueda en Profundidad (DFS)")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/graph-algorithms") {
                                style = "color:rgb(6, 8, 15); text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos de Grafos"
                            }
                        }
                        
                        h1 { +"🌊 Búsqueda en Profundidad (DFS)" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"DFS explora el grafo tan profundo como sea posible antes de retroceder. Utiliza una pila (stack) para recordar qué nodos visitar." 
                        }
                        
                        div {
                            id = "input-container"
                            label {
                                htmlFor = "start-node-input"
                                +"Nodo inicial: "
                            }
                            input {
                                id = "start-node-input"
                                type = InputType.number
                                value = "0"
                                min = "0"
                                max = "4"
                            }
                            label {
                                htmlFor = "end-node-input"
                                +"Nodo objetivo (opcional): "
                            }
                            input {
                                id = "end-node-input"
                                type = InputType.number
                                value = "4"
                                min = "0"
                                max = "4"
                            }
                            button {
                                id = "execute-button"
                                +"Ejecutar DFS"
                            }
                        }
                        
                        div {
                            id = "visualization-container"
                            div {
                                id = "graph-container"
                                classes = setOf("graph-container")
                            }
                            div {
                                id = "algorithm-info"
                                classes = setOf("algorithm-info")
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
                    }
                    
                    script(src = "/static/js/graph-algorithms.js") {}
                    script {
                        +"window.algorithmType = 'dfs';"
                    }
                }
            }
        }
        
        post("/dfs/execute") {
            val request = call.receive<GraphAlgorithmRequest>()
            val steps = graphAlgorithmService.executeDFS(request.startNodeId, request.endNodeId)
            call.respond(steps)
        }
        
        // BFS
        get("/bfs") {
            call.respondHtml {
                head {
                    title("Búsqueda en Anchura (BFS)")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/graph-algorithms") {
                                style = "color:rgb(6, 8, 15); text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos de Grafos"
                            }
                        }
                        
                        h1 { +"🌐 Búsqueda en Anchura (BFS)" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"BFS explora el grafo nivel por nivel. Utiliza una cola (queue) para procesar los nodos en el orden correcto." 
                        }
                        
                        div {
                            id = "input-container"
                            label {
                                htmlFor = "start-node-input"
                                +"Nodo inicial: "
                            }
                            input {
                                id = "start-node-input"
                                type = InputType.number
                                value = "0"
                                min = "0"
                                max = "4"
                            }
                            label {
                                htmlFor = "end-node-input"
                                +"Nodo objetivo (opcional): "
                            }
                            input {
                                id = "end-node-input"
                                type = InputType.number
                                value = "4"
                                min = "0"
                                max = "4"
                            }
                            button {
                                id = "execute-button"
                                +"Ejecutar BFS"
                            }
                        }
                        
                        div {
                            id = "visualization-container"
                            div {
                                id = "graph-container"
                                classes = setOf("graph-container")
                            }
                            div {
                                id = "algorithm-info"
                                classes = setOf("algorithm-info")
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
                    }
                    
                    script(src = "/static/js/graph-algorithms.js") {}
                    script {
                        +"window.algorithmType = 'bfs';"
                    }
                }
            }
        }
        
        post("/bfs/execute") {
            val request = call.receive<GraphAlgorithmRequest>()
            val steps = graphAlgorithmService.executeBFS(request.startNodeId, request.endNodeId)
            call.respond(steps)
        }
        
        // Dijkstra
        get("/dijkstra") {
            call.respondHtml {
                head {
                    title("Algoritmo de Dijkstra")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/graph-algorithms") {
                                style = "color:rgb(6, 8, 15); text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos de Grafos"
                            }
                        }
                        
                        h1 { +"🎯 Algoritmo de Dijkstra" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"El algoritmo de Dijkstra encuentra el camino más corto desde un nodo inicial a todos los demás nodos en un grafo con pesos positivos." 
                        }
                        
                        div {
                            id = "input-container"
                            label {
                                htmlFor = "start-node-input"
                                +"Nodo inicial: "
                            }
                            input {
                                id = "start-node-input"
                                type = InputType.number
                                value = "0"
                                min = "0"
                                max = "4"
                            }
                            label {
                                htmlFor = "end-node-input"
                                +"Nodo objetivo (opcional): "
                            }
                            input {
                                id = "end-node-input"
                                type = InputType.number
                                value = "4"
                                min = "0"
                                max = "4"
                            }
                            button {
                                id = "execute-button"
                                +"Ejecutar Dijkstra"
                            }
                        }
                        
                        div {
                            id = "visualization-container"
                            div {
                                id = "graph-container"
                                classes = setOf("graph-container")
                            }
                            div {
                                id = "algorithm-info"
                                classes = setOf("algorithm-info")
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
                    }
                    
                    script(src = "/static/js/graph-algorithms.js") {}
                    script {
                        +"window.algorithmType = 'dijkstra';"
                    }
                }
            }
        }
        
        post("/dijkstra/execute") {
            val request = call.receive<GraphAlgorithmRequest>()
            val steps = graphAlgorithmService.executeDijkstra(request.startNodeId, request.endNodeId)
            call.respond(steps)
        }
        
        get("/sample-graph") {
            val data = graphAlgorithmService.getSampleGraphData()
            call.respond(data)
        }
    }
}
