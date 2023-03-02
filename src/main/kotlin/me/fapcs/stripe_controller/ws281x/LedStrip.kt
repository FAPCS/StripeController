package me.fapcs.stripe_controller.ws281x

interface LedStrip {

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
            type: LedStripType
        ) = Ws281xLedStripe(count, gpioPin, frequency, dma, brightness, pwmChannel, invert, type)

    }

}