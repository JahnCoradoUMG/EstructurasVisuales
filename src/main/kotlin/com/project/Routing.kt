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

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondRedirect("/home")
        }
        
        get("/home") {
            call.respondHtml {
                head {
                    title("Estructuras y Algoritmos Visuales")
                    link(rel = "stylesheet", href = "/static/css/styles.css")
                }
                body {
                    h1 { +"Estructuras Visuales" }
                    p { +"Bienvenido al sistema de visualización de algoritmos y estructuras de datos." }
                    
                    div {
                        classes = setOf("menu")
                        h2 { +"Menú Principal" }
                        ul {
                            li {
                                a(href = "/algorithms") {
                                    +"Algoritmos de Ordenamiento"
                                }
                            }
                            li {
                                a(href = "/data-structures") {
                                    +"Estructuras de Datos"
                                }
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
        
        // Archivos estáticos
        static("/static") {
            resources("static")
        }
    }
}
