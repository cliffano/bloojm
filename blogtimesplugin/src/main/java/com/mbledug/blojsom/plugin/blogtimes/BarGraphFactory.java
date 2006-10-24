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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BarGraphFactory {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final Color DEFAULT_BOX_BORDER_COLOR = Color.WHITE;
    private static final Color DEFAULT_BOX_BACKGROUND_COLOR =
            new Color(235, 235, 235);
    private static final Color DEFAULT_TIMELINE_COLOR =
            new Color(102, 102, 102);
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private static final int DEFAULT_BOX_HEIGHT = 25;
    private static final int DEFAULT_BOX_WIDTH = 350;

    private static final int BOX_BORDER_SIZE = 1;
    private static final int BOX_MARGIN = 15;
    private static final int FONT_SIZE = 12;
    private static final int FONT_MARGIN_HEIGHT = 2;
    private static final int FONT_MARGIN_WIDTH = 5;
    private static final int TIMELINE_START = 0;
    private static final int TIMELINE_SIZE = 1;
    private static final int INTERVAL_WIDTH = 1;
    private static final int INTERVAL_HEIGHT = 5;

    private static final int MAX_SECOND_IN_MINUTE = 60;
    private static final int MAX_MINUTE_IN_HOUR = 60;
    private static final int MAX_HOUR_IN_DAY = 24;

    private static final int SECONDS_IN_MINUTE = MAX_SECOND_IN_MINUTE;
    private static final int SECONDS_IN_HOUR =
            MAX_MINUTE_IN_HOUR * SECONDS_IN_MINUTE;
    private static final int SECONDS_IN_DAY = MAX_HOUR_IN_DAY * SECONDS_IN_HOUR;

    private static final String SECOND_OF_MINUTE_FLAVOR = "second-of-minute";
    private static final String MINUTE_OF_HOUR_FLAVOR = "minute-of-hour";
    private static final String HOUR_OF_DAY_FLAVOR = "hour-of-day";

    private static final BarGraphMeasurement SECOND_OF_MINUTE_MEASUREMENT
            = new BarGraphMeasurement(Calendar.SECOND, SECONDS_IN_MINUTE, 5,
                    MAX_SECOND_IN_MINUTE);
    private static final BarGraphMeasurement MINUTE_OF_HOUR_MEASUREMENT
            = new BarGraphMeasurement(
                    Calendar.MINUTE, SECONDS_IN_HOUR, 5, MAX_MINUTE_IN_HOUR);
    private static final BarGraphMeasurement HOUR_OF_DAY_MEASUREMENT
            = new BarGraphMeasurement(
                    Calendar.HOUR_OF_DAY, SECONDS_IN_DAY, 2, MAX_HOUR_IN_DAY);

    private static Map mMeasurements;

    private Color mBackgroundColor;
    private Color mBoxBorderColor;
    private Color mBoxBackgroundColor;
    private Color mTimelineColor;
    private Color mFontColor;
    private int mBoxHeight;
    private int mBoxWidth;

    BarGraphFactory() {
        mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        mBoxBorderColor = DEFAULT_BOX_BORDER_COLOR;
        mBoxBackgroundColor = DEFAULT_BOX_BACKGROUND_COLOR;
        mTimelineColor = DEFAULT_TIMELINE_COLOR;
        mFontColor = DEFAULT_FONT_COLOR;
        mBoxHeight = DEFAULT_BOX_HEIGHT;
        mBoxWidth = DEFAULT_BOX_WIDTH;

        mMeasurements = new HashMap();
        mMeasurements.put(
                SECOND_OF_MINUTE_FLAVOR, SECOND_OF_MINUTE_MEASUREMENT);
        mMeasurements.put(MINUTE_OF_HOUR_FLAVOR, MINUTE_OF_HOUR_MEASUREMENT);
        mMeasurements.put(HOUR_OF_DAY_FLAVOR, HOUR_OF_DAY_MEASUREMENT);
    }

    BufferedImage createImage(final String flavor, final Date[] dates) {

        BarGraphMeasurement measurement = (BarGraphMeasurement)
                mMeasurements.get(flavor);
        if (measurement == null) {
            throw new IllegalArgumentException("Flavor: " + flavor
                    + ", is invalid.");
        }

        int imageHeight = mBoxHeight + BOX_MARGIN;
        int imageWidth = mBoxWidth + (BOX_MARGIN * 2);

        BufferedImage image = new BufferedImage(
                imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // fill background
        graphics.setPaint(mBackgroundColor);
        graphics.fill(new Rectangle2D.Double(0, 0, imageWidth, imageHeight));

        //  draw box
        graphics.setPaint(mBoxBackgroundColor);
        graphics.fill(new Rectangle2D.Double(
                BOX_MARGIN + 1, 1, mBoxWidth - 1, mBoxHeight - 1));

        // draw date timelines
        for (int i = 0; i < dates.length; i++) {
            int totalSeconds = calculateTotalSeconds(
                    measurement.getCalendarUnit(), dates[i]);
            float multiplier = Float.parseFloat(
                    String.valueOf(totalSeconds)) / measurement.getScaler();
            int pos = BOX_MARGIN + Integer.parseInt(
                    String.valueOf(Math.round(mBoxWidth * multiplier)));
            graphics.setPaint(mTimelineColor);
            graphics.fill(new Rectangle2D.Double(
                    pos, BOX_BORDER_SIZE, TIMELINE_SIZE, mBoxHeight));
        }

        // draw time intervals
        graphics.setFont(new Font(null, Font.PLAIN, FONT_SIZE));
        for (int i = TIMELINE_START; i <= measurement.getMaxValue();
                i += measurement.getInterval()) {

            int pos = BOX_MARGIN + ((i * mBoxWidth) /
                    (measurement.getMaxValue() - TIMELINE_START));

            if (i != TIMELINE_START && i != measurement.getMaxValue()) {
                graphics.setPaint(mTimelineColor);
                graphics.fill(new Rectangle2D.Double(
                        pos, mBoxHeight - INTERVAL_HEIGHT, INTERVAL_WIDTH,
                        INTERVAL_HEIGHT));
            }

            graphics.setColor(mFontColor);
            graphics.drawString(String.valueOf(i), pos - FONT_MARGIN_WIDTH,
                    mBoxHeight + BOX_MARGIN - FONT_MARGIN_HEIGHT);
        }

        // draw borders
        graphics.setPaint(mBoxBorderColor);
        graphics.fill(new Rectangle2D.Double(BOX_MARGIN, 0, BOX_BORDER_SIZE,
                mBoxHeight));
        graphics.fill(new Rectangle2D.Double(
                BOX_MARGIN + mBoxWidth - BOX_BORDER_SIZE, 0, BOX_BORDER_SIZE,
                mBoxHeight));
        graphics.fill(new Rectangle2D.Double(BOX_MARGIN, 0, mBoxWidth,
                BOX_BORDER_SIZE));
        graphics.fill(new Rectangle2D.Double(BOX_MARGIN, mBoxHeight, mBoxWidth,
                BOX_BORDER_SIZE));

        graphics.dispose();

        return image;
    }

    private final int calculateTotalSeconds(
            final int calendarUnit, final Date date) {

        if (calendarUnit != Calendar.SECOND &&
                calendarUnit != Calendar.MINUTE &&
                calendarUnit != Calendar.HOUR_OF_DAY) {
            throw new IllegalArgumentException("Invalid calendar unit: "
                    + calendarUnit);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int totalSeconds = calendar.get(Calendar.SECOND);
        if (calendarUnit != Calendar.SECOND) {
            totalSeconds += calendar.get(Calendar.MINUTE) * SECONDS_IN_MINUTE;
            if (calendarUnit != Calendar.MINUTE) {
                totalSeconds += calendar.get(Calendar.HOUR_OF_DAY) *
                        SECONDS_IN_HOUR;
            }
        }

        return totalSeconds;
    }
}
