import com.shobande.menufactory.gateway.wrappers.AfricasTalking
import com.shobande.menufactory.menu.Menu
import io.ktor.application.call
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

suspend fun main() {
    val menu = buildMenu()

    val port = System.getenv("PORT")?.toInt() ?: 9090

    embeddedServer(Netty, port) {
        routing {
//            install(ContentNegotiation) {
//                gson {
//                    setDateFormat(DateFormat.LONG)
//                    setPrettyPrinting()
//                }
//            }

            get("/") {
                call.respond("Alive!")
            }

            post("/") {
                val params = call.receiveParameters()
                println(params.entries())
                println("femi")
                val request = mutableMapOf<String, String>()
//                params.entries().forEach {
//                }
//                params.
                menu.handle(request) { call.respond(it) }
            }
        }
    }.start(wait = true)
}

enum class States {
    CHECK_BALANCE,
    BUY_AIRTIME,
    CONTACT_US
}

fun fetchBalance(): Int = 100

suspend fun buildMenu(): Menu {
    return Menu.menu("Mystery App", AfricasTalking) {
        startState {
            run {
                con("""Welcome to ${this@menu.name}
                    |1. Check Balance
                    |2. Buy Airtime
                    |3. Contact Us
                """.trimMargin())
            }

            transitions {
                "1" to States.CHECK_BALANCE.name
                "2" to {
                    if (it.operator.equals("MTN")) {
                        States.BUY_AIRTIME.name
                    } else {
                        States.CONTACT_US.name
                    }
                }
                "3" to States.CONTACT_US.name
                """^[a-zA-Z]*$""" to States.CONTACT_US.name
            }
        }

        state(States.CHECK_BALANCE.name) {
            run {
                if (it.phoneNumber.length != 13) {
                    goTo(States.CONTACT_US.name)
                } else {
                    end("You balance is ${fetchBalance()}")
                }
            }
        }

        state(States.BUY_AIRTIME.name) {
            run {
                con("Enter your phone number")
            }
            transitions {
                """^[0-9]*$""" to "selectNetworkProvider"
            }
            defaultNextState(States.CONTACT_US.name)
        }

        state(States.CONTACT_US.name) {
            run {
                end("You can reach me on Twitter @shobande_femi")
            }
        }
    }
}