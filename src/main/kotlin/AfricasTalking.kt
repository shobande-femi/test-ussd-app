import com.shobande.menufactory.gateway.wrappers.AfricasTalking
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object AfricasTalkingUssdApp {
    suspend fun main() {
        val menu = buildMenu(AfricasTalking)

        val port = System.getenv("PORT")?.toInt() ?: 9090

        embeddedServer(Netty, port) {
            routing {
                get("/") {
                    call.respond(menu.name)
                }

                post("/") {
                    val params = call.receiveParameters()
                    val request = mutableMapOf<String, String>()
                    params.entries().forEach { request[it.key] = it.value.first() }

                    menu.handle(request) { call.respond(it) }
                }
            }
        }.start(wait = true)
    }
}