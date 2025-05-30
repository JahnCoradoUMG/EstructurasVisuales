// package com.project

// import freemarker.cache.*
// import io.ktor.http.*
// import io.ktor.serialization.kotlinx.json.*
// import io.ktor.server.application.*
// import io.ktor.server.freemarker.*
// import io.ktor.server.html.*
// import io.ktor.server.http.content.*
// import io.ktor.server.plugins.contentnegotiation.*
// import io.ktor.server.plugins.cors.routing.*
// import io.ktor.server.response.*
// import io.ktor.server.routing.*

// fun Application.configureRouting() {
//     routing {
//         get("/") {
//             call.respondText("Hello World!")
//         }
//         // Static plugin. Try to access `/static/index.html`
//         staticResources("/static", "static")
//     }
// }
