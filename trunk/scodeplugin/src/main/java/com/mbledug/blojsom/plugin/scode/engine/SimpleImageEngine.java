/**
 * Copyright (c) 2004-2006, Cliffano Subagio
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
package com.mbledug.blojsom.plugin.scode.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * {@link SimpleImageEngine} generates an image with light gray background with
 * vertical and horizontal lines, and dark gray text drawn on the foreground.
 * The image is the original image of WordPress' SCode plugin.
 * @author Cliffano Subagio
 */
public final class SimpleImageEngine implements ImageEngine {

    /**
     * Background color.
     */
    private static final Color BACKGROUND_COLOR = new Color(224, 224, 224);
    /**
     * Line color.
     */
    private static final Color LINE_COLOR = new Color(192, 192, 192);
    /**
     * Line size in pixels.
     */
    private static final int LINE_SIZE = 1;
    /**
     * Line spacing in pixels.
     */
    private static final int LINE_SPACING = 5;

    /**
     * Text font color.
     */
    private static final Color FONT_COLOR = new Color(128, 128, 128);
    /**
     * Text font size.
     */
    private static final int FONT_SIZE = 14;
    /**
     * Starting X coordinate of the text.
     */
    private static final int FONT_X_POS = 12;
    /**
     * Starting Y coordinate of the text.
     */
    private static final int FONT_Y_POS = 18;
    /**
     * Font for the text.
     */
    private static final Font FONT = new Font(null, Font.BOLD, FONT_SIZE);

    /**
     * {@inheritDoc}
     */
    public BufferedImage getImage(final String text) {

        BufferedImage image = new BufferedImage(
                IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        image.setData(getBackgroundImageData());
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(FONT_COLOR);
        graphics.setFont(FONT);
        graphics.drawString(text, FONT_X_POS, FONT_Y_POS);
        graphics.dispose();

        return image;
    }

    /**
     * Gets background image with the vertical and horizontal lines.
     * @return background image data
     */
    private Raster getBackgroundImageData() {

        BufferedImage backgroundImage = new BufferedImage(
                IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = backgroundImage.createGraphics();

        // draws background
        graphics.setPaint(BACKGROUND_COLOR);
        graphics.fill(new Rectangle2D.Double(
                LINE_SIZE, LINE_SIZE,
                IMAGE_WIDTH - 2 * LINE_SIZE,
                IMAGE_HEIGHT - 2 * LINE_SIZE));

        // draws vertical and horizontal lines
        graphics.setPaint(LINE_COLOR);
        for (int y = LINE_SPACING; y < IMAGE_HEIGHT - LINE_SIZE;
                y += LINE_SPACING) {
            graphics.draw(new Line2D.Double(
                    LINE_SIZE, y, IMAGE_WIDTH - 2 * LINE_SIZE, y));
        }
        for (int x = LINE_SPACING; x < IMAGE_WIDTH - LINE_SIZE;
                x += LINE_SPACING) {
            graphics.draw(new Line2D.Double(
                    x, LINE_SIZE, x, IMAGE_HEIGHT - 2 * LINE_SIZE));
        }

        return backgroundImage.getData();
    }
}
