package me.fapcs.stripe_controller.ws281x

import com.github.mbelling.ws281x.jni.rpi_ws281x
import com.github.mbelling.ws281x.jni.rpi_ws281xConstants
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
) : LedStrip {

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
                Companion::class.java.classLoader.getResourceAsStream("lib$LIB_NAME.so")
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
            channel.strip_type = getNativeLedStripType(type)
        }

        private fun shiftColor(red: Int, green: Int, blue: Int): Long {
            return (red.toShort().toInt() shl 16 or (green.toShort().toInt() shl 8) or blue.toShort().toInt()).toLong()
        }

        private fun getNativeLedStripType(type: LedStripType) = when (type) {
            LedStripType.SK6812_STRIP_RGBW -> rpi_ws281xConstants.SK6812_STRIP_RGBW
            LedStripType.SK6812_STRIP_RBGW -> rpi_ws281xConstants.SK6812_STRIP_RBGW
            LedStripType.SK6812_STRIP_GRBW -> rpi_ws281xConstants.SK6812_STRIP_GRBW
            LedStripType.SK6812_STRIP_GBRW -> rpi_ws281xConstants.SK6812_STRIP_GBRW
            LedStripType.SK6812_STRIP_BRGW -> rpi_ws281xConstants.SK6812_STRIP_BRGW
            LedStripType.SK6812_STRIP_BGRW -> rpi_ws281xConstants.SK6812_STRIP_BGRW
            LedStripType.WS2811_STRIP_RGB -> rpi_ws281xConstants.WS2811_STRIP_RGB
            LedStripType.WS2811_STRIP_RBG -> rpi_ws281xConstants.WS2811_STRIP_RBG
            LedStripType.WS2811_STRIP_GRB -> rpi_ws281xConstants.WS2811_STRIP_GRB
            LedStripType.WS2811_STRIP_GBR -> rpi_ws281xConstants.WS2811_STRIP_GBR
            LedStripType.WS2811_STRIP_BRG -> rpi_ws281xConstants.WS2811_STRIP_BRG
            LedStripType.WS2811_STRIP_BGR -> rpi_ws281xConstants.WS2811_STRIP_BGR
        }


    }

}