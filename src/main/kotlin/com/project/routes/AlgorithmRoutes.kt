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
                    title("Algoritmos")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                }
                body {
                    h1 { +"Algoritmos de Ordenamiento" }
                    div {
                        a(href = "/algorithms/bubble-sort") {
                            +"Bubble Sort"
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
                    h1 { +"Bubble Sort" }
                    p { +"Bubble Sort es un algoritmo de ordenamiento simple que funciona comparando pares de elementos adyacentes y intercambiándolos si están en el orden incorrecto." }
                    
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
    }
}
