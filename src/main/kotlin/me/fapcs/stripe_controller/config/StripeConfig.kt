package me.fapcs.stripe_controller.config

import me.fapcs.stripe_controller.ws281x.LedStripeType

data class StripeConfig(
    val count: Int,
    val gpioPin: Int,
    val frequency: Long = 800000,
    val dma: Int = 10,
    val brightness: Int = 200,
    val pwmChannel: Int,
    val invert: Boolean = false,
    val type: LedStripeType = LedStripeType.WS2811_STRIP_GRB
) {

    companion object {

        val first = StripeConfig(count = 50, gpioPin = 12, pwmChannel = 0)
        val second = StripeConfig(count = 80, gpioPin = 13, pwmChannel = 1)
        val third = StripeConfig(count = 410, gpioPin = 21, pwmChannel = 0)

    }

}