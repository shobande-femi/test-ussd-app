import com.shobande.menufactory.gateway.Gateway
import com.shobande.menufactory.menu.Menu

suspend fun buildMenu(gateway: Gateway): Menu {
    return Menu.menu("Mystery App", gateway) {
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
                    if (!it.operator.equals("MTN")) {
                        States.BUY_AIRTIME.name
                    } else {
                        States.CONTACT_US.name
                    }
                }
                "3" to States.CONTACT_US.name
            }
        }

        state(States.CHECK_BALANCE.name) {
            run {
                if (it.phoneNumber.length != 14) {
                    goTo(States.CONTACT_US.name)
                } else {
                    end("You balance is ${fetchBalance()}")
                }
            }
        }

        state(States.BUY_AIRTIME.name) {
            run {
                con("""Enter your phone number
                    |0. Go back
                """.trimMargin())
            }
            transitions {
                "0" to session.startStateName
                """^[0-9]*$""" to {
                    session.set(it.sessionId, "phoneNumber", it.message)
                    "airtimeBought"
                }
                """^[a-zA-Z]*$""" to States.CONTACT_US.name
            }
            defaultNextState(States.CONTACT_US.name)
        }

        state(States.CONTACT_US.name) {
            run {
                end("You can reach me on Twitter @shobande_femi")
            }
        }

        state("airtimeBought") {
            run {
                end("You have successfully bought some airtime on ${session.get(it.sessionId, "phoneNumber")}")
            }
        }
    }
}

enum class States {
    CHECK_BALANCE,
    BUY_AIRTIME,
    CONTACT_US
}

fun fetchBalance(): Int = 100