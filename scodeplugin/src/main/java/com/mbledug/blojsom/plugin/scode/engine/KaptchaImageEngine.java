/**
 * Copyright (c) 2004-2007, Cliffano Subagio
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
 *   * Neither the name of Studio Cliffano nor the names of its contributors
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

import java.awt.image.BufferedImage;
import java.util.Properties;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.impl.DefaultBackground;
import com.google.code.kaptcha.text.WordRenederer;
import com.google.code.kaptcha.text.impl.DefaultWordRenderer;

/**
 * {@link KaptchaImageEngine} generates image using
 * <a href="http://code.google.com/p/kaptcha/">Kaptcha</a>.
 * Font size and image dimension are optimized for Kaptcha which renders the
 * text on a fixed coordinate (25, 35).
 * @author Cliffano Subagio
 */
public abstract class KaptchaImageEngine implements ImageEngine {

    /**
     * Height for Kaptcha-generated image.
     */
    protected static final int IMAGE_HEIGHT = 60;

    /**
     * Width for Kaptcha-generated image.
     */
    protected static final int IMAGE_WIDTH = 130;

    /**
     * Kaptcha configuration properties.
     */
    private Properties mProperties;

    /**
     * Initialises Kaptcha configuration properties.
     */
    public KaptchaImageEngine() {
        mProperties = new Properties();
        mProperties.put("cap.font.arr", "Arial,Helvetica,Courier,TimesRoman");
        mProperties.put("cap.font.size", "32");
    }

    /**
     * Provides GimpyEngine implementation.
     * @return the gimpy engine
     */
    protected abstract GimpyEngine getGimpyEngine();

    /**
     * {@inheritDoc}
     */
    public final BufferedImage getImage(final String text) {
        WordRenederer wordRenderer = new DefaultWordRenderer(getProperties());
        BackgroundProducer backgroundProducer = new DefaultBackground(
                getProperties());
        GimpyEngine gimpyEngine = getGimpyEngine();

        BufferedImage image = wordRenderer.renderWord(
                text, IMAGE_WIDTH, IMAGE_HEIGHT);
        image = gimpyEngine.getDistortedImage(image);
        image = backgroundProducer.addBackground(image);
        return image;
    }

    /**
     * Gets Kaptcha configuration properties.
     * @return the configuration properties
     */
    protected final Properties getProperties() {
        return mProperties;
    }
}
