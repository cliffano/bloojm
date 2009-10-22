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
package com.mbledug.blojsom.plugin.scode;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.CommentPlugin;

/**
 * {@link SCodePlugin} checks SCode input from comment form against SCode
 * answer generated by {@link SCodeServlet}. If the input doesn't match the
 * answer, then comment is marked for deletion.
 * @author Cliffano Subagio
 */
public class SCodePlugin implements Plugin {

    /**
     * Logger for {@link SCodePlugin}.
     */
    private static final Log LOG = LogFactory.getLog(SCodePlugin.class);

    /**
     * Session attribute name for storing {@link ImageFactory}.
     */
    public static final String SESSION_ATTR_IMAGE_FACTORY =
        "scode-image-factory";

    /**
     * Paramater name of SCode input.
     */
    public static final String PARAM_SCODE = "scode";

    /**
     * Blog property whether plugin is enabled.
     */
    public static final String PROPERTY_ENABLED = "scode-enabled";

    /**
     * {@link ImageFactory} to pass to SCode servlet.
     */
    private ImageFactory mImageFactory;

    /**
     * Creates an instance of {@link SCodePlugin} with non-null
     * {@link ImageFactory}.
     * @param imageFactory the {@link ImageFactory}
     */
    public SCodePlugin(final ImageFactory imageFactory) {
        if (imageFactory == null) {
            throw new IllegalArgumentException(
                    "Image factory must be provided.");
        }
        mImageFactory = imageFactory;
    }

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising SCodePlugin.");
    }

    /**
     * Marks comment for deletion if the SCode input doesn't match the SCode
     * stored in the session.
     *
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries blog entries with comment marked for deletion in the case
     * of non-matching SCode input
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {

        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(SESSION_ATTR_IMAGE_FACTORY) == null) {
            httpSession.setAttribute(SESSION_ATTR_IMAGE_FACTORY, mImageFactory);
        }

        boolean isPluginEnabled = "true".equalsIgnoreCase(
                blog.getProperty(PROPERTY_ENABLED));
        boolean isCommentFormSubmission = "y".equalsIgnoreCase(
                httpServletRequest.getParameter(CommentPlugin.COMMENT_PARAM));
        boolean isBlogCommentEnabled = blog.getBlogCommentsEnabled()
                .booleanValue();

        if (isPluginEnabled && isCommentFormSubmission && isBlogCommentEnabled)
                {
            String inputSCode = httpServletRequest.getParameter(PARAM_SCODE);
            String answerSCode = String.valueOf(httpSession
                    .getAttribute(PARAM_SCODE));
            if (LOG.isDebugEnabled()) {
                LOG.debug("SCode input from comment form: " + inputSCode + ", "
                        + "Correct SCode answer: " + answerSCode);
            }
            checkSCode(context, inputSCode, answerSCode);
            httpSession.removeAttribute(PARAM_SCODE);
        }

        return entries;
    }

    /**
     * cleanup method has an empty implementation in {@link SCodePlugin}.
     * @throws PluginException when there's an error performing cleanup of
     * this plugin
     */
    public final void cleanup() throws PluginException {
    }

    /**
     * Writes plugin destroy message.
     * @throws PluginException when there's an error in finalising this plugin
     */
    public final void destroy() throws PluginException {
        LOG.info("Destroying SCodePlugin.");
    }

    /**
     * Checks SCode input against SCode answer. If they don't match then
     * mark the comment for deletion.
     * @param context the context that holds comment meta data
     * @param inputSCode the SCode input
     * @param answerSCode the SCode answer
     */
    private void checkSCode(
            final Map context,
            final String inputSCode,
            final String answerSCode) {
        if (inputSCode != null && answerSCode != null) {
            if (!inputSCode.equals(answerSCode)) {
                markCommentForDeletion(context);
                LOG.info("SCode input doesn't match the SCode answer. "
                        + "Marking comment for deletion.");
            } else {
                LOG.info("SCode input matches the SCode answer.");
            }
        } else {
            markCommentForDeletion(context);
            LOG.info("Either SCode input or SCode answer doesn't exist. "
                    + "Marking comment for deletion.");
        }
    }

    /**
     * Marks comment for deletion by adding comment destroy to comment meta data
     * in the context.
     * @param context the context that holds the comment meta data
     */
    private void markCommentForDeletion(final Map context) {
        Map commentMetaData;
        if (context.containsKey(
                CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA)) {
            commentMetaData = (Map) context.get(
                    CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA);
        } else {
            commentMetaData = new HashMap();
        }
        commentMetaData.put(
                CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA_DESTROY,
                Boolean.TRUE.toString());
        context.put(CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA,
                commentMetaData);
    }
}
