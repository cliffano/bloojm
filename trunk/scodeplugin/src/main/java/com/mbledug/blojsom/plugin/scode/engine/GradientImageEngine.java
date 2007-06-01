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
import java.io.Serializable;

import com.octo.captcha.component.image.backgroundgenerator.
        BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.
        GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.
        TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.wordgenerator.DummyWordGenerator;
import com.octo.captcha.component.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * {@link GradientImageEngine} generates an image with grayish gradient
 * background. Dark gray text drawn on the foreground, with random twist and
 * shear font, and at random position.
 * @author Cliffano Subagio
 */
public class GradientImageEngine extends JCaptchaImageEngine
        implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5234274928742397423L;

    /**
     * Minimum font size.
     */
    private static final int FONT_SIZE_MIN = 16;
    /**
     * Maximum font size.
     */
    private static final int FONT_SIZE_MAX = 18;

    /**
     * Font color.
     */
    private static final Color FONT_COLOR = new Color(128, 128, 128);
    /**
     * Gradient color 1.
     */
    private static final Color GRADIENT_COLOR_1 = new Color(224, 224, 224);
    /**
     * Gradient color 2.
     */
    private static final Color GRADIENT_COLOR_2 = new Color(192, 192, 192);

    /**
     * {@inheritDoc}
     */
    public final ImageCaptchaFactory getFactory(final String text) {

        WordGenerator wordGenerator = new DummyWordGenerator(text);
        TextPaster textPaster = new RandomTextPaster(
                new Integer(text.length()),
                new Integer(text.length()),
                FONT_COLOR);
        BackgroundGenerator backgroundGenerator =
                new GradientBackgroundGenerator(
                        new Integer(IMAGE_WIDTH),
                        new Integer(IMAGE_HEIGHT),
                        GRADIENT_COLOR_1,
                        GRADIENT_COLOR_2);
        FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(
                new Integer(FONT_SIZE_MIN), new Integer(FONT_SIZE_MAX));
        WordToImage wordToImage = new ComposedWordToImage(
                fontGenerator, backgroundGenerator, textPaster);
        return new GimpyFactory(wordGenerator, wordToImage);
    }
}
