package me.fapcs.stripe_controller

import me.fapcs.shared.communication.CommunicationHandler
import me.fapcs.shared.log.Logger
import me.fapcs.shared.module.stripe.packet.SetLedsPacket

object StripeController {

    @JvmStatic
    fun main(args: Array<String>) {
        Logger.info("Starting StripeController")

        val stripeForwarder = StripeForwarder()
        CommunicationHandler.receive<SetLedsPacket> { stripeForwarder.handlePacket(it) }

        Logger.info("StripeController started")
    }

}