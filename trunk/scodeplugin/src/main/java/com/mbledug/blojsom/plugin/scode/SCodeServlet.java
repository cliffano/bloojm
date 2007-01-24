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
package com.mbledug.blojsom.plugin.scode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link SCodeServlet} serves a dynamic image with a random SCode drawn on it.
 * The SCode is saved in the session, and then used by {@link SCodePlugin} to
 * moderate blog comment.
 * @author Cliffano Subagio
 */
public class SCodeServlet extends HttpServlet {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5506707527123785294L;

    /**
     * Log for {@link SCodeServlet}.
     */
    private static final Log LOG = LogFactory.getLog(SCodeServlet.class);

    /**
     * Number of characters used as SCode.
     */
    private static final int SCODE_LENGTH = 6;

    /**
     * Creates an image with SCode drawn on it, sets the SCode value as
     * session atttribute.
     * @param httpServletRequest the servlet request
     * @param httpServletResponse  the servlet response
     */
    public final void service(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse) {

        String sCode = RandomStringUtils.random(SCODE_LENGTH, false, true);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating SCode:" + sCode);
        }

        // set the sCode on the session
        httpServletRequest.getSession().setAttribute(
                SCodePlugin.PARAM_SCODE, sCode);

        // set headers
        httpServletResponse.setContentType("image/png");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setHeader("Pragma", "no-cache");

        // retrieve the engines configuration
        Map engines = (Map) httpServletRequest.getSession().getAttribute(
                SCodePlugin.SESSION_ATTR_ENGINES);
        String flavor = httpServletRequest.getParameter("flavor");

        // create a PNG image of the SCode
        ImageFactory imageFactory = new ImageFactory(engines);
        BufferedImage image = imageFactory.getImage(sCode, flavor);

        try {
            ServletOutputStream out = httpServletResponse.getOutputStream();
            ImageIO.write(image, "png", out);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            LOG.error("Unable to write SCode image.");
        }
    }
}
