package com.project.routes

import com.project.services.AlgorithmService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable

@Serializable
data class SortRequest(val array: List<Int>)

fun Route.algorithmRoutes() {
    val algorithmService = AlgorithmService()
    
    route("/algorithms") {
        get {
            call.respondHtml {
                head {
                    title("Algoritmos de Ordenamiento")
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
                        
                        h1 { +"🔄 Algoritmos de Ordenamiento" }
                        
                        div {
                            style = "text-align: center; margin-bottom: 2rem;"
                            p { 
                                style = "color: rgba(255,255,255,0.9); font-size: 1.2rem;"
                                +"Descubre cómo funcionan los algoritmos de ordenamiento más populares" 
                            }
                        }
                        
                        div {
                            classes = setOf("menu")
                            h2 { +"📊 Algoritmos Disponibles" }
                            ul {
                                li {
                                    a(href = "/algorithms/bubble-sort") {
                                        +"🫧 Bubble Sort"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Algoritmo simple que compara elementos adyacentes"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/algorithms/selection-sort") {
                                        +"🔍 Selection Sort"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Algoritmo que busca el mínimo en cada iteración"
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
        
        get("/bubble-sort") {
            call.respondHtml {
                head {
                    title("Bubble Sort")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    classes = setOf("main-container")
                        
                    div {
                        style = "margin-bottom: 1rem;"
                        a(href = "/algorithms") {
                            style = "color:rgb(6, 8, 15); text-decoration: none; font-weight: 600;"
                            +"← Volver a Algoritmos"
                        }
                    }
                        
                    h1 { +"🫧 Bubble Sort" }
                        
                    p { 
                        style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                        +"Bubble Sort es un algoritmo de ordenamiento simple que funciona comparando pares de elementos adyacentes y intercambiándolos si están en el orden incorrecto." 
                    }
                    div {
                        id = "input-container"
                        label {
                            htmlFor = "array-input"
                            +"Ingrese los números separados por comas: "
                        }
                        input {
                            id = "array-input"
                            type = InputType.text
                            value = "64, 34, 25, 12, 22, 11, 90"
                        }
                        button {
                            id = "sort-button"
                            +"Ordenar"
                        }
                    }
                    
                    div {
                        id = "visualization-container"
                        div {
                            id = "array-container"
                            classes = setOf("array-container")
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
                    
                    script(src = "/static/js/bubble-sort.js") {}
                }
            }
        }
        
        post("/bubble-sort/sort") {
            val request = call.receive<SortRequest>()
            val steps = algorithmService.bubbleSort(request.array)
            call.respond(steps)
        }

        get("/selection-sort") {
            call.respondHtml {
                head {
                    title("Selection Sort")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                    script(src = "https://cdn.jsdelivr.net/npm/d3@7") {}
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color:rgb(9, 11, 19); text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos"
                            }
                        }
                        
                        h1 { +"🔍 Selection Sort" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"Selection Sort es un algoritmo de ordenamiento que divide el arreglo en una parte ordenada y otra no ordenada. En cada iteración, busca el elemento mínimo en la parte no ordenada y lo coloca al final de la parte ordenada." 
                        }
                        
                        div {
                            id = "input-container"
                            label {
                                htmlFor = "array-input"
                                +"Ingrese los números separados por comas: "
                            }
                            input {
                                id = "array-input"
                                type = InputType.text
                                value = "64, 34, 25, 12, 22, 11, 90"
                            }
                            button {
                                id = "sort-button"
                                +"Ordenar"
                            }
                        }
                        
                        div {
                            id = "visualization-container"
                            div {
                                id = "array-container"
                                classes = setOf("array-container")
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
                    
                    script(src = "/static/js/selection-sort.js") {}
                }
            }
        }
        
        post("/selection-sort/sort") {
            val request = call.receive<SortRequest>()
            val steps = algorithmService.selectionSort(request.array)
            call.respond(steps)
        }
    }
}
