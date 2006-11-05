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
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * {@link BarGraphImageCreator} creates a bar graph image containing time lines
 * based on the specified dates and bar graph flavor.
 * @author Cliffano
 */
final class BarGraphImageCreator implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -5504243324549206189L;

    /**
     * Default background color.
     */
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    /**
     * Default box border color.
     */
    private static final Color DEFAULT_BOX_BORDER_COLOR = Color.GRAY;
    /**
     * Default box background color.
     */
    private static final Color DEFAULT_BOX_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    /**
     * Default timeline color.
     */
    private static final Color DEFAULT_TIMELINE_COLOR =
            new Color(102, 102, 102);
    /**
     * Default time interval color.
     */
    private static final Color DEFAULT_TIME_INTERVAL_COLOR =
            new Color(51, 51, 51);
    /**
     * Default font color.
     */
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    /**
     * Default box height in pixels.
     */
    private static final int DEFAULT_BOX_HEIGHT = 25;
    /**
     * Default box width in pixels.
     */
    private static final int DEFAULT_BOX_WIDTH = 350;
    /**
     * Default box margin in pixels.
     */
    private static final int BOX_MARGIN = 15;
    /**
     * Default box border size in pixels.
     */
    private static final int BOX_BORDER_SIZE = 1;

    /**
     * Background color.
     */
    private Color mBackgroundColor;
    /**
     * Box border color.
     */
    private Color mBoxBorderColor;
    /**
     * Box background color.
     */
    private Color mBoxBackgroundColor;
    /**
     * Timeline color.
     */
    private Color mTimelineColor;
    /**
     * Time interval color.
     */
    private Color mTimeIntervalColor;
    /**
     * Font color.
     */
    private Color mFontColor;
    /**
     * Box height.
     */
    private int mBoxHeight;
    /**
     * Box width.
     */
    private int mBoxWidth;

    /**
     * Creates an instance of {@link BarGraphImageCreator}.
     */
    BarGraphImageCreator() {
        mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        mBoxBorderColor = DEFAULT_BOX_BORDER_COLOR;
        mBoxBackgroundColor = DEFAULT_BOX_BACKGROUND_COLOR;
        mTimelineColor = DEFAULT_TIMELINE_COLOR;
        mTimeIntervalColor = DEFAULT_TIME_INTERVAL_COLOR;
        mFontColor = DEFAULT_FONT_COLOR;
        mBoxHeight = DEFAULT_BOX_HEIGHT;
        mBoxWidth = DEFAULT_BOX_WIDTH;
    }

    /**
     * Creates the bar graph image with the dates timelines drawn on it.
     * @param flavor the flavor of the timeline to draw
     * @param dates the dates for the timelines
     * @return the bar graph image
     */
    BufferedImage createImage(final String flavor, final Date[] dates) {

        BarGraph barGraph = BarGraphFactory.getBarGraph(flavor);

        BufferedImage image = new BufferedImage(
                mBoxWidth + (BOX_MARGIN * 2),
                mBoxHeight + BOX_MARGIN,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = createBackground(image);
        drawTimelines(graphics, dates,
                barGraph.getCalendarUnit(), barGraph.getScaler());
        drawTimeIntervals(
                graphics, barGraph.getMaxValue(), barGraph.getInterval());
        drawBorders(graphics);

        graphics.dispose();

        return image;
    }

    /**
     * Creates the background graphics from the buffered image.
     * @param image the buffered image to create the graphics from
     * @return the background graphics
     */
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

    /**
     * Draws the timelines on the graphics.
     * @param graphics the graphics to draw on
     * @param dates the dates of the timelines
     * @param calendarUnit the calendar unit
     * @param scaler the scaler value
     */
    private void drawTimelines(
            final Graphics2D graphics,
            final Date[] dates,
            final int calendarUnit,
            final int scaler) {

        final int timelineSize = 1;

        for (int i = 0; i < dates.length; i++) {
            int totalSeconds = calculateTotalSeconds(
                    calendarUnit, dates[i]);
            float multiplier = Float.parseFloat(
                    String.valueOf(totalSeconds)) / scaler;
            int pos = BOX_MARGIN + Integer.parseInt(
                    String.valueOf(Math.round(mBoxWidth * multiplier)));
            graphics.setPaint(mTimelineColor);
            graphics.fill(new Rectangle2D.Double(
                    pos, BOX_BORDER_SIZE, timelineSize, mBoxHeight));
        }
    }

    /**
     * Draws time intervals on the graphics.
     * @param graphics the graphics to draw on
     * @param timelineEnd the end value of the timeline
     * @param interval the interval value of the timeline
     */
    private void drawTimeIntervals(
            final Graphics2D graphics,
            final int timelineEnd,
            final int interval) {

        final int fontSize = 12;
        final int fontMarginHeight = 2;
        final int fontMarginWidth = 5;
        final int timelineStart = 0;
        final int intervalWidth = 1;
        final int intervalHeight = 5;

        graphics.setFont(new Font(null, Font.PLAIN, fontSize));
        for (int i = timelineStart; i <= timelineEnd; i += interval) {

            int pos = BOX_MARGIN + ((i * mBoxWidth)
                    / (timelineEnd - timelineStart));

            if (i != timelineStart && i != timelineEnd) {
                graphics.setPaint(mTimeIntervalColor);
                graphics.fill(new Rectangle2D.Double(
                        pos, mBoxHeight - intervalHeight, intervalWidth,
                        intervalHeight));
            }

            graphics.setColor(mFontColor);
            graphics.drawString(String.valueOf(i), pos - fontMarginWidth,
                    mBoxHeight + BOX_MARGIN - fontMarginHeight);
        }
    }

    /**
     * Draws the borders on the graphics.
     * @param graphics the graphics to draw on
     */
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

    /**
     * Calculates the total number of seconds within the date based on the
     * calendar unit.
     * @param calendarUnit the calendar unit
     * @param date the date to check
     * @return the total number of seconds
     */
    private int calculateTotalSeconds(
            final int calendarUnit, final Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int totalSeconds = calendar.get(Calendar.SECOND);
        if (calendarUnit != Calendar.SECOND) {
            totalSeconds += calendar.get(
                    Calendar.MINUTE) * BlogTimesHelper.SECONDS_IN_MINUTE;
            if (calendarUnit != Calendar.MINUTE) {
                totalSeconds += calendar.get(
                        Calendar.HOUR_OF_DAY) * BlogTimesHelper.SECONDS_IN_HOUR;
            }
        }

        return totalSeconds;
    }

    /**
     * Sets the background color.
     * @param backgroundColor the background color to set
     */
    public void setBackgroundColor(final Color backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    /**
     * Sets the box background color.
     * @param boxBackgroundColor the box background color to set
     */
    public void setBoxBackgroundColor(final Color boxBackgroundColor) {
        mBoxBackgroundColor = boxBackgroundColor;
    }

    /**
     * Sets the box border color.
     * @param boxBorderColor the box border color to set
     */
    public void setBoxBorderColor(final Color boxBorderColor) {
        mBoxBorderColor = boxBorderColor;
    }

    /**
     * Sets the box height.
     * @param boxHeight the box height to set
     */
    public void setBoxHeight(final int boxHeight) {
        if (boxHeight <= 0) {
            throw new IllegalArgumentException("Invalid box height.");
        }
        mBoxHeight = boxHeight;
    }

    /**
     * Sets the box width.
     * @param boxWidth the box width to set
     */
    public void setBoxWidth(final int boxWidth) {
        if (boxWidth <= 0) {
            throw new IllegalArgumentException("Invalid box width.");
        }
        mBoxWidth = boxWidth;
    }

    /**
     * Sets the font color.
     * @param fontColor the font color to set
     */
    public void setFontColor(final Color fontColor) {
        mFontColor = fontColor;
    }

    /**
     * Sets the timeline color.
     * @param timelineColor the timeline color to set
     */
    public void setTimelineColor(final Color timelineColor) {
        mTimelineColor = timelineColor;
    }

    /**
     * Sets the time interval color.
     * @param timeIntervalColor the time interval color to set
     */
    public void setTimeIntervalColor(final Color timeIntervalColor) {
        mTimeIntervalColor = timeIntervalColor;
    }
}
