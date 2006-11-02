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

/**
 * {@link BarGraphImageCreator} creates a bar graph image containing time lines
 * based on the specified dates and bar graph flavor.
 * @author Cliffano
 */
final class BarGraphImageCreator {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private static final Color DEFAULT_BOX_BORDER_COLOR = Color.WHITE;
    private static final Color DEFAULT_BOX_BACKGROUND_COLOR =
            new Color(235, 235, 235);
    private static final Color DEFAULT_TIMELINE_COLOR =
            new Color(102, 102, 102);
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private static final int DEFAULT_BOX_HEIGHT = 25;
    private static final int DEFAULT_BOX_WIDTH = 350;

    private static final int BOX_MARGIN = 15;
    private static final int BOX_BORDER_SIZE = 1;

    private Color mBackgroundColor;
    private Color mBoxBorderColor;
    private Color mBoxBackgroundColor;
    private Color mTimelineColor;
    private Color mFontColor;
    private int mBoxHeight;
    private int mBoxWidth;

    BarGraphImageCreator() {
        mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        mBoxBorderColor = DEFAULT_BOX_BORDER_COLOR;
        mBoxBackgroundColor = DEFAULT_BOX_BACKGROUND_COLOR;
        mTimelineColor = DEFAULT_TIMELINE_COLOR;
        mFontColor = DEFAULT_FONT_COLOR;
        mBoxHeight = DEFAULT_BOX_HEIGHT;
        mBoxWidth = DEFAULT_BOX_WIDTH;
    }

    BufferedImage createImage(final String flavor, final Date[] dates) {

        BarGraph barGraph = BarGraphFactory.getBarGraph(flavor);

        BufferedImage image = new BufferedImage(
                mBoxWidth + (BOX_MARGIN * 2),
                mBoxHeight + BOX_MARGIN,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = createBackground(image);
        drawTimelines(graphics, barGraph, dates);
        drawTimeIntervals(graphics, barGraph);
        drawBorders(graphics);

        graphics.dispose();

        return image;
    }

    private Graphics2D createBackground(final BufferedImage image) {

        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(mBackgroundColor);
        graphics.fill(new Rectangle2D.Double(
                0, 0, image.getWidth(), image.getHeight()));

        graphics.setPaint(mBoxBackgroundColor);
        graphics.fill(new Rectangle2D.Double(
                BOX_MARGIN + 1, 1, mBoxWidth - 1, mBoxHeight - 1));

        return graphics;
    }

    private void drawTimelines(
            final Graphics2D graphics,
            final BarGraph barGraph,
            final Date[] dates) {

        final int TIMELINE_SIZE = 1;

        for (int i = 0; i < dates.length; i++) {
            int totalSeconds = calculateTotalSeconds(
                    barGraph.getCalendarUnit(), dates[i]);
            float multiplier = Float.parseFloat(
                    String.valueOf(totalSeconds)) / barGraph.getScaler();
            int pos = BOX_MARGIN + Integer.parseInt(
                    String.valueOf(Math.round(mBoxWidth * multiplier)));
            graphics.setPaint(mTimelineColor);
            graphics.fill(new Rectangle2D.Double(
                    pos, BOX_BORDER_SIZE, TIMELINE_SIZE, mBoxHeight));
        }
    }

    private void drawTimeIntervals(
            final Graphics2D graphics, final BarGraph barGraph) {

        final int FONT_SIZE = 12;
        final int FONT_MARGIN_HEIGHT = 2;
        final int FONT_MARGIN_WIDTH = 5;
        final int TIMELINE_START = 0;
        final int INTERVAL_WIDTH = 1;
        final int INTERVAL_HEIGHT = 5;

        graphics.setFont(new Font(null, Font.PLAIN, FONT_SIZE));
        for (int i = TIMELINE_START; i <= barGraph.getMaxValue();
                i += barGraph.getInterval()) {

            int pos = BOX_MARGIN + ((i * mBoxWidth) /
                    (barGraph.getMaxValue() - TIMELINE_START));

            if (i != TIMELINE_START && i != barGraph.getMaxValue()) {
                graphics.setPaint(mTimelineColor);
                graphics.fill(new Rectangle2D.Double(
                        pos, mBoxHeight - INTERVAL_HEIGHT, INTERVAL_WIDTH,
                        INTERVAL_HEIGHT));
            }

            graphics.setColor(mFontColor);
            graphics.drawString(String.valueOf(i), pos - FONT_MARGIN_WIDTH,
                    mBoxHeight + BOX_MARGIN - FONT_MARGIN_HEIGHT);
        }
    }

    private void drawBorders(final Graphics2D graphics) {

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
            totalSeconds += calendar.get(
                    Calendar.MINUTE) * TimeUnit.SECONDS_IN_MINUTE;
            if (calendarUnit != Calendar.MINUTE) {
                totalSeconds += calendar.get(
                        Calendar.HOUR_OF_DAY) * TimeUnit.SECONDS_IN_HOUR;
            }
        }

        return totalSeconds;
    }
}
