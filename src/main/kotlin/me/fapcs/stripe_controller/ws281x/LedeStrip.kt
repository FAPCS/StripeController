package me.fapcs.stripe_controller.ws281x

import me.fapcs.stripe_controller.config.StripeConfig

interface LedeStrip {

    fun setPixel(pixel: Int, red: Int, green: Int, blue: Int)

    fun setStripe(red: Int, green: Int, blue: Int)

    fun render()

    fun setBrightness(brightness: Int)

    companion object {

        fun create(
            count: Int,
            gpioPin: Int,
            frequency: Long,
            dma: Int,
            brightness: Int,
            pwmChannel: Int,
            invert: Boolean,
            type: LedStripeType
        ) = Ws281xLedStripe(count, gpioPin, frequency, dma, brightness, pwmChannel, invert, type)

        fun create(config: StripeConfig) = create(
            config.count,
            config.gpioPin,
            config.frequency,
            config.dma,
            config.brightness,
            config.pwmChannel,
            config.invert,
            config.type
        )

    }

}