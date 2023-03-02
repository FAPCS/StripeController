package me.fapcs.stripe_controller.ws281x

import com.github.mbelling.ws281x.jni.rpi_ws281xConstants

enum class LedStripType(val value: Int) {

    SK6812_STRIP_RGBW(rpi_ws281xConstants.SK6812_STRIP_RGBW),
    SK6812_STRIP_RBGW(rpi_ws281xConstants.SK6812_STRIP_RBGW),
    SK6812_STRIP_GRBW(rpi_ws281xConstants.SK6812_STRIP_GRBW),
    SK6812_STRIP_GBRW(rpi_ws281xConstants.SK6812_STRIP_GBRW),
    SK6812_STRIP_BRGW(rpi_ws281xConstants.SK6812_STRIP_BRGW),
    SK6812_STRIP_BGRW(rpi_ws281xConstants.SK6812_STRIP_BGRW),
    WS2811_STRIP_RGB(rpi_ws281xConstants.WS2811_STRIP_RGB),
    WS2811_STRIP_RBG(rpi_ws281xConstants.WS2811_STRIP_RBG),
    WS2811_STRIP_GRB(rpi_ws281xConstants.WS2811_STRIP_GRB),
    WS2811_STRIP_GBR(rpi_ws281xConstants.WS2811_STRIP_GBR),
    WS2811_STRIP_BRG(rpi_ws281xConstants.WS2811_STRIP_BRG),
    WS2811_STRIP_BGR(rpi_ws281xConstants.WS2811_STRIP_BGR),

}