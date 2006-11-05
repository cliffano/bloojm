/**
 * Copyright (c) 2005-2006, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Mbledug nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.blogtimes;

import java.awt.Color;

/**
 * Holds constants related to time values.
 * @author Cliffano
 */
final class BlogTimesHelper {

    /**
     * Maximum second value in a minute.
     */
    static final int MAX_SECOND_IN_MINUTE = 60;

    /**
     * Maximum minute value in an hour.
     */
    static final int MAX_MINUTE_IN_HOUR = 60;

    /**
     * Maximum hour value in a day.
     */
    static final int MAX_HOUR_IN_DAY = 24;

    /**
     * Number of seconds in a minute.
     */
    static final int SECONDS_IN_MINUTE = MAX_SECOND_IN_MINUTE;

    /**
     * Number of seconds in an hour.
     */
    static final int SECONDS_IN_HOUR = MAX_MINUTE_IN_HOUR * SECONDS_IN_MINUTE;

    /**
     * Number of seconds in a day.
     */
    static final int SECONDS_IN_DAY = MAX_HOUR_IN_DAY * SECONDS_IN_HOUR;

    /**
     * Hides default constructor of helper class.
     */
    private BlogTimesHelper() {
    }

    /**
     * Converts a hexadecimal color to Color object
     * e.g. ff0000 will be converted to Color 255 0 0
     * @param hex the hexadecimal color
     * @return Color object of the hexadecimal value
     */
    static Color hexToColor(final String hex) {

        final int hexColorDigitSize = 6;
        final int hexRadix = 16;
        final int hexRgbDigitSize = 2;
        final int redStartIndex = 0;
        final int greenStartIndex = 2;
        final int blueStartIndex = 4;

        if (hex == null || hex.length() != hexColorDigitSize) {
            throw new IllegalArgumentException("Invalid hex color value.");
        }

        int red = Integer.parseInt(hex.substring(
                redStartIndex, redStartIndex + hexRgbDigitSize),
                hexRadix);
        int green = Integer.parseInt(hex.substring(
                greenStartIndex, greenStartIndex + hexRgbDigitSize),
                hexRadix);
        int blue = Integer.parseInt(hex.substring(
                blueStartIndex, blueStartIndex + hexRgbDigitSize),
                hexRadix);
        return new Color(red, green, blue);
    }
}
