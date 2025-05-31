package com.project

import com.project.routes.algorithmRoutes
import com.project.routes.dataStructureRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import freemarker.cache.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import com.project.routes.graphAlgorithmRoutes

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondRedirect("/home")
        }
        
        get("/home") {
            call.respondHtml {
                head {
                    title("Estructuras Visuales")
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
                        
                        h1 { +"🚀 Estructuras Visuales" }
                        
                        div {
                            style = "text-align: center; margin-bottom: 2rem;"
                            p { 
                                style = "color: rgba(255,255,255,0.9); font-size: 1.3rem; font-weight: 500;"
                                +"Explora algoritmos y estructuras de datos de forma interactiva" 
                            }
                        }
                        
                        div {
                            classes = setOf("menu")
                            h2 { +"📚 Selecciona una categoría" }
                            ul {
                                li {
                                    a(href = "/algorithms") {
                                        +"🔄 Mejora Educativa"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Visualiza cómo funcionan los algoritmos paso a paso"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/data-structures") {
                                        +"🏗️ Estructuras de Datos"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Interactúa con diferentes estructuras de datos"
                                        }
                                    }
                                }
                                li {
                                    a(href = "/graph-algorithms") {
                                        +"🔍 Algoritmos de Grafos"
                                        br
                                        span {
                                            style = "font-size: 0.9rem; opacity: 0.8; font-weight: 400;"
                                            +"Visualiza algoritmos de búsqueda y caminos más cortos"
                                        }
                                    }
                                }
                            }
                        }
                        
                        div {
                            style = "text-align: center; margin-top: 2rem; color: rgba(222, 30, 30, 0);"
                            p {
                                style = "font-size: 0.9rem;"
                                +"💡 Haz clic en cualquier categoría para comenzar tu exploración"
                            }
                        }
                    }
                }
            }
        }
        
        // Rutas para algoritmos
        algorithmRoutes()
        
        // Rutas para estructuras de datos
        dataStructureRoutes()

        // Rutas para algoritmos de grafos
        graphAlgorithmRoutes()
        
        // Archivos estáticos
        static("/static") {
            resources("static")
        }
    }
}
