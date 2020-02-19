import com.shobande.menufactory.gateway.wrappers.HubTel
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat

object HubTelUssdApp {
    suspend fun main() {
        val menu = buildMenu(HubTel)

        val port = System.getenv("PORT")?.toInt() ?: 9090

        embeddedServer(Netty, port) {
            routing {
                install(ContentNegotiation) {
                    gson {
                        setDateFormat(DateFormat.LONG)
                        setPrettyPrinting()
                    }
                }

                get("/") {
                    call.respond(menu.name)
                }

                post("/") {
                    menu.handle(call.receive()) { call.respond(it) }
                }
            }
        }.start(wait = true)
    }
}

