package com.project.routes

import com.project.services.AlgorithmService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable

@Serializable
data class SortRequest(val array: List<Int>)

@Serializable
data class ComparisonRequest(val array: List<Int>, val algorithm1: String, val algorithm2: String)

fun Route.algorithmRoutes() {
    val algorithmService = AlgorithmService()
    
    route("/algorithms") {
        get {
            call.respondHtml {
                head {
                    title("Mejora Educativa")
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
                        
                        h1 { +"🔄 Mejora Educativa" }
                        
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
                                li {
                                    a(href = "/algorithms/quick-sort") {
                                        +"⚡ Quick Sort"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Algoritmo eficiente que usa divide y vencerás"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/algorithms/comparison") {
                                        +"⚔️ Comparación de Algoritmos"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Compara el rendimiento de dos algoritmos lado a lado"
                                        }
                                    }
                                }
                            }
                            
                            div {
                                style = "text-align: center; margin-top: 2rem;"
                                a(href = "/home") {
                                    style = "color: #667eea; text-decoration: none; font-weight: 600;"
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
                }
                body {
                    div {
                        classes = setOf("main-container")

                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color:rgb(12, 13, 20); text-decoration: none; font-weight: 600;"
                                +"← Volver a Mejora Educativa"
                            }
                        }
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color: #667eea; text-decoration: none; font-weight: 600;"
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
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                }
                body {
                    div {
                        classes = setOf("main-container")
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color:rgb(12, 13, 20); text-decoration: none; font-weight: 600;"
                                +"← Volver a Mejora Educativa"
                            }
                        }

                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color: #667eea; text-decoration: none; font-weight: 600;"
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
        
        get("/quick-sort") {
            call.respondHtml {
                head {
                    title("Quick Sort")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                }
                body {
                    div {
                        classes = setOf("main-container")

                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color:rgb(12, 13, 20); text-decoration: none; font-weight: 600;"
                                +"← Volver a Mejora Educativa"
                            }
                        }
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color: #667eea; text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos"
                            }
                        }
                        
                        h1 { +"⚡ Quick Sort" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"Quick Sort es un algoritmo de ordenamiento eficiente que utiliza la estrategia divide y vencerás. Selecciona un elemento como pivot y particiona el arreglo alrededor de él." 
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
                    
                    script(src = "/static/js/quick-sort.js") {}
                }
            }
        }
        
        post("/quick-sort/sort") {
            val request = call.receive<SortRequest>()
            val steps = algorithmService.quickSort(request.array)
            call.respond(steps)
        }
        
        get("/comparison") {
            call.respondHtml {
                head {
                    title("Comparación de Algoritmos")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                    link(rel = "preconnect", href = "https://fonts.googleapis.com")
                    link(rel = "preconnect", href = "https://fonts.gstatic.com") {
                        attributes["crossorigin"] = ""
                    }
                    link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap")
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                }
                body {
                    div {
                        classes = setOf("main-container")

                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color:rgb(12, 13, 20); text-decoration: none; font-weight: 600;"
                                +"← Volver a Mejora Educativa"
                            }
                        }
                        
                        div {
                            style = "margin-bottom: 1rem;"
                            a(href = "/algorithms") {
                                style = "color: #667eea; text-decoration: none; font-weight: 600;"
                                +"← Volver a Algoritmos"
                            }
                        }
                        
                        h1 { +"⚔️ Comparación de Algoritmos" }
                        
                        p { 
                            style = "color: rgba(255,255,255,0.9); font-size: 1.1rem; text-align: center; margin-bottom: 2rem;"
                            +"Compara el rendimiento de dos algoritmos de ordenamiento lado a lado y observa sus diferencias en tiempo real." 
                        }
                        
                        div {
                            id = "input-container"
                            div {
                                style = "margin-bottom: 1rem;"
                                label {
                                    htmlFor = "array-input"
                                    +"Ingrese los números separados por comas: "
                                }
                                input {
                                    id = "array-input"
                                    type = InputType.text
                                    value = "64, 34, 25, 12, 22, 11, 90"
                                }
                            }
                            div {
                                style = "margin-bottom: 1rem;"
                                label {
                                    htmlFor = "algorithm1-select"
                                    +"Algoritmo 1: "
                                }
                                select {
                                    id = "algorithm1-select"
                                    option {
                                        value = "bubble"
                                        selected = true
                                        +"Bubble Sort"
                                    }
                                    option {
                                        value = "selection"
                                        +"Selection Sort"
                                    }
                                    option {
                                        value = "quick"
                                        +"Quick Sort"
                                    }
                                }
                                label {
                                    htmlFor = "algorithm2-select"
                                    style = "margin-left: 2rem;"
                                    +"Algoritmo 2: "
                                }
                                select {
                                    id = "algorithm2-select"
                                    option {
                                        value = "bubble"
                                        +"Bubble Sort"
                                    }
                                    option {
                                        value = "selection"
                                        +"Selection Sort"
                                    }
                                    option {
                                        value = "quick"
                                        selected = true
                                        +"Quick Sort"
                                    }
                                }
                            }
                            button {
                                id = "compare-button"
                                +"Comparar Algoritmos"
                            }
                        }
                        
                        div {
                            id = "visualization-container"
                            div {
                                id = "comparison-container"
                                classes = setOf("comparison-container")
                                div {
                                    id = "algorithm1-section"
                                    classes = setOf("algorithm-section")
                                    h3 {
                                        id = "algorithm1-title"
                                        +"Algoritmo 1"
                                    }
                                    div {
                                        id = "array1-container"
                                        classes = setOf("array-container")
                                    }
                                    div {
                                        id = "metrics1"
                                        classes = setOf("metrics")
                                    }
                                }
                                div {
                                    id = "algorithm2-section"
                                    classes = setOf("algorithm-section")
                                    h3 {
                                        id = "algorithm2-title"
                                        +"Algoritmo 2"
                                    }
                                    div {
                                        id = "array2-container"
                                        classes = setOf("array-container")
                                    }
                                    div {
                                        id = "metrics2"
                                        classes = setOf("metrics")
                                    }
                                }
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
                                button {
                                    id = "reset-button"
                                    +"Reiniciar"
                                }
                            }
                            div {
                                id = "performance-chart-container"
                                style = "margin-top: 2rem;"
                                canvas {
                                    id = "performance-chart"
                                    width = "400"
                                    height = "200"
                                }
                            }
                        }
                    }
                    
                    script(src = "/static/js/algorithm-comparison.js") {}
                }
            }
        }
        
        post("/comparison/compare") {
            try {
                val request = call.receive<ComparisonRequest>()
                val comparison = algorithmService.compareAlgorithms(request.array, request.algorithm1, request.algorithm2)
                
                call.respond(comparison)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }
    }
}
