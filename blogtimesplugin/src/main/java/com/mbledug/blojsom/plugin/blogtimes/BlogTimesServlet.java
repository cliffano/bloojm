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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BlogTimesServlet extends HttpServlet {

    /**
     * Log for BlogTimesServlet.
     */
    private static final Log LOG = LogFactory.getLog(BlogTimesServlet.class);

    public final void init() {
        LOG.info("Initialising BlogTimesServlet.");
    }

    public final void service(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse) {

        Date[] dates = (Date[]) httpServletRequest.getSession().
                getAttribute(BlogTimesPlugin.DATES_SESSION_KEY);
        String flavor = String.valueOf(httpServletRequest.
                getParameter("flavor"));

        BarGraphFactory factory = new BarGraphFactory();

        BufferedImage image = factory.createImage(flavor, dates);

        httpServletResponse.setContentType("image/png");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setHeader("Pragma", "no-cache");

        try {
            ServletOutputStream out = httpServletResponse.getOutputStream();
            ImageIO.write(image, "png", out);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            LOG.error("");
        }
    }

    public final void destroy() {
        LOG.info("Destroying BlogTimesServlet.");
    }
}
