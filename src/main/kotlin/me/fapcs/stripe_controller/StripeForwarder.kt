package me.fapcs.stripe_controller

import me.fapcs.shared.config.ConfigurationHandler
import me.fapcs.shared.log.Logger
import me.fapcs.shared.module.stripe.packet.SetLedsPacket
import me.fapcs.stripe_controller.config.StripeConfig
import me.fapcs.stripe_controller.ws281x.LedeStrip

class StripeForwarder {

    private val stripe1: LedeStrip
    private val stripe2: LedeStrip
    private val stripe3: LedeStrip

    init {
        Logger.info("Initializing StripeForwarder")

        Logger.debug("Loading StripeConfig for stripe 1")
        stripe1 = LedeStrip.create(
            ConfigurationHandler.get(
                "led.stripe.1",
                StripeConfig.first,
                true
            )
        )
        Logger.debug("Creating Stripe 1")

        Logger.debug("Loading StripeConfig for stripe 2")
        stripe2 = LedeStrip.create(
            ConfigurationHandler.get(
                "led.stripe.2",
                StripeConfig.second,
                true
            )
        )
        Logger.debug("Creating Stripe 2")

        Logger.debug("Loading StripeConfig for stripe 3")
        stripe3 = LedeStrip.create(
            ConfigurationHandler.get(
                "led.stripe.3",
                StripeConfig.third,
                true
            )
        )
        Logger.debug("Creating Stripe 3")

        Logger.info("StripeForwarder initialized")
    }

    fun handlePacket(packet: SetLedsPacket) {
        val stripe = when (packet.stripe) {
            1 -> stripe1
            2 -> stripe2
            3 -> stripe3
            else -> {
                Logger.error("Received packet with illegal stripe number: ${packet.stripe}")
                return
            }
        }

        packet.leds.forEach { led -> stripe.setPixel(led, packet.color.red, packet.color.green, packet.color.blue) }
    }

}