package me.fapcs.stripe_controller.ws281x

interface LedStripe {

    fun setPixel(pixel: Int, red: Int, green: Int, blue: Int)

    fun setStripe(red: Int, green: Int, blue: Int)

    fun render()

    fun setBrightness(brightness: Int)

}