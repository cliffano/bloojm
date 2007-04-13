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

import java.io.Serializable;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.impl.WaterRiple;

/**
 * {@link KinkImageEngine} generates an image with grey white gradient
 * background, a rippled black text, with a line in front of the text.
 * The image will look very similar to the ones on
 * <a href=https://edit.yahoo.com/config/eval_register?.intl=us&new=1">
 *   Yahoo!
 * </a>.
 * @author Cliffano Subagio
 */
public class KinkImageEngine extends KaptchaImageEngine
        implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5349875353537454355L;

    /**
     * {@inheritDoc}
     */
    public final GimpyEngine getGimpyEngine() {
        return new WaterRiple(getProperties());
    }
}
