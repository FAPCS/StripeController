package me.fapcs.stripe_controller.ws281x

import com.github.mbelling.ws281x.jni.rpi_ws281x
import com.github.mbelling.ws281x.jni.ws2811_channel_t
import com.github.mbelling.ws281x.jni.ws2811_t
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class Ws281xLedStripe(
    count: Int,
    gpioPin: Int,
    frequency: Long,
    dma: Int,
    brightness: Int,
    pwmChannel: Int,
    invert: Boolean,
    type: LedStripType
) : LedStripe {

    private val leds: ws2811_t
    private val channel: ws2811_channel_t

    private var destroyed = false

    init {
        loadNative()
        Runtime.getRuntime().addShutdownHook(Thread { down() })

        leds = ws2811_t()
        channel = rpi_ws281x.ws2811_channel_get(leds, pwmChannel)

        initChannel(channel, count, gpioPin, brightness, invert, type)

        leds.freq = frequency
        leds.dmanum = dma

        val result = rpi_ws281x.ws2811_init(leds)

        if (result.swigValue() != 0) throw Exception("ws2811_init failed with code $result")
    }

    private fun down() {
        setStripe(0, 0, 0)
        render()

        rpi_ws281x.ws2811_fini(leds)
        leds.delete()

        destroyed = true
    }

    private fun ensureWorking() {
        if (destroyed) throw Exception("LedStripe is already destroyed")
    }

    override fun setPixel(pixel: Int, red: Int, green: Int, blue: Int) {
        ensureWorking()

        rpi_ws281x.ws2811_led_set(channel, pixel, shiftColor(red, green, blue))
    }

    override fun setStripe(red: Int, green: Int, blue: Int) {
        ensureWorking()

        val color = shiftColor(red, green, blue)

        for (i in 0 until channel.count) rpi_ws281x.ws2811_led_set(channel, i, color)
    }

    override fun render() {
        ensureWorking()

        val result = rpi_ws281x.ws2811_render(leds)
        if (result.swigValue() != 0) throw Exception("ws2811_render failed with code $result")
    }

    override fun setBrightness(brightness: Int) {
        ensureWorking()

        channel.brightness = brightness.toShort()
    }

    companion object {

        private const val LIB_NAME = "ws281x"
        private var loaded = false

        private fun loadNative() {
            if (loaded) return

            val tempFile = Files.createTempFile("lib$LIB_NAME", ".so")
            tempFile.toFile().deleteOnExit()

            Files.copy(
                Ws281xLedStripe::class.java.getResourceAsStream("lib$LIB_NAME.so")
                    ?: throw Exception("Could not find native library"),
                tempFile,
                StandardCopyOption.REPLACE_EXISTING
            )

            System.load(tempFile.toString())
            loaded = true
        }

        private fun initChannel(
            channel: ws2811_channel_t,
            count: Int,
            gpioPin: Int,
            brightness: Int,
            invert: Boolean,
            type: LedStripType
        ) {
            channel.count = count
            channel.gpionum = gpioPin
            channel.invert = if (invert) 1 else 0
            channel.brightness = brightness.toShort()
            channel.strip_type = type.value
        }

        private fun shiftColor(red: Int, green: Int, blue: Int): Long {
            return (red.toShort().toInt() shl 16 or (green.toShort().toInt() shl 8) or blue.toShort().toInt()).toLong()
        }


    }

}