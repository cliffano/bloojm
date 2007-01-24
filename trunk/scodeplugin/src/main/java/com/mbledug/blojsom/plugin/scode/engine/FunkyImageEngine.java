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

import com.octo.captcha.component.image.backgroundgenerator.
        BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.
        FunkyBackgroundGenerator;
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
 * {@link FunkyImageEngine} generates an image with funky background of red,
 * green, yellow noises. Black text drawn on the foreground, with random twist
 * and shear font, and at random position.
 * @author Cliffano Subagio
 */
public final class FunkyImageEngine extends JCaptchaImageEngine {

    /**
     * Minimum font size.
     */
    private static final int FONT_SIZE_MIN = 16;
    /**
     * Maximum font size.
     */
    private static final int FONT_SIZE_MAX = 18;

    /**
     * {@inheritDoc}
     */
    public ImageCaptchaFactory getFactory(final String text) {

        WordGenerator wordGenerator = new DummyWordGenerator(text);
        TextPaster textPaster = new RandomTextPaster(
                new Integer(text.length()),
                new Integer(text.length()),
                Color.BLACK);
        BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(
                new Integer(IMAGE_WIDTH), new Integer(IMAGE_HEIGHT));
        FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(
                new Integer(FONT_SIZE_MIN), new Integer(FONT_SIZE_MAX));
        WordToImage wordToImage = new ComposedWordToImage(
                fontGenerator, backgroundGenerator, textPaster);
        return new GimpyFactory(wordGenerator, wordToImage);
    }
}
