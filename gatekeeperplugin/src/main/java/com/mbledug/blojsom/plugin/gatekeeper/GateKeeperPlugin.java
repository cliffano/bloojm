/**
 * Copyright (c) 2007, Cliffano Subagio
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
package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.CommentPlugin;

/**
 * {@link GateKeeperPlugin} fights comment spam by displaying a random question
 * on the form which should be answered by the commenter. Questions can be
 * configured from Blojsom context file, from the database, and from blog
 * properties.
 * @author Cliffano Subagio
 */
public class GateKeeperPlugin implements Plugin {

    /**
     * Logger for {@link GateKeeperPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(GateKeeperPlugin.class);

    /**
     * Session attribute name for storing question answer.
     */
    public static final String SESSION_ATTR_QA = "gatekeeper_qa";

    /**
     * Paramater name of GateKeeper input.
     */
    public static final String PARAM_GATEKEEPER = "gatekeeper";

    /**
     * Context name for the question answer to pass to velocity template.
     */
    public static final String CONTEXT_QA = "GATEKEEPER_QA";

    /**
     * Blog property whether plugin is enabled.
     */
    public static final String PROPERTY_ENABLED = "gatekeeper-enabled";

    /**
     * Manager to provide random {@link QA}.
     */
    private QAManager mQAManager;

    /**
     * Constructs {@link GateKeeperPlugin}.
     * @param qAManager the {@link QAManager}.
     */
    public GateKeeperPlugin(final QAManager qAManager) {
        if (qAManager == null) {
            throw new IllegalArgumentException("QAManager cannot be null.");
        }
        mQAManager = qAManager;
    }

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising GateKeeperPlugin.");
    }

    /**
     * Marks comment for deletion if the GateKeeper input doesn't match the
     * expected answer from the question answer stored in the session.
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries the original unmodified entries
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {

        boolean isPluginEnabled = "true".equalsIgnoreCase(
                blog.getProperty(PROPERTY_ENABLED));
        boolean isBlogCommentEnabled = blog.getBlogCommentsEnabled()
                .booleanValue();
        boolean isCommentFormSubmission = "y".equalsIgnoreCase(
                httpServletRequest.getParameter(CommentPlugin.COMMENT_PARAM));

        if (isPluginEnabled && isBlogCommentEnabled) {
            if (isCommentFormSubmission) {
                String inputGateKeeper = httpServletRequest.getParameter(
                        PARAM_GATEKEEPER);
                String answerGateKeeper = ((QA) httpServletRequest.getSession()
                        .getAttribute(SESSION_ATTR_QA)).getAnswer();
                checkGateKeeper(context, inputGateKeeper, answerGateKeeper);
            } else {
                QA qA = mQAManager.getRandomQuestionAnswer(blog);
                context.put(CONTEXT_QA, qA);
                httpServletRequest.getSession().setAttribute(
                        SESSION_ATTR_QA, qA);
            }
        }

        return entries;
    }

    /**
     * cleanup method has an empty implementation in {@link GateKeeperPlugin}.
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
        LOG.info("Destroying GateKeeperPlugin.");
    }

    /**
     * Checks GateKeeper input against expected answer. If they don't match then
     * mark the comment for deletion.
     * @param context the context that holds comment meta data
     * @param inputGateKeeper the GateKeeper input
     * @param answerGateKeeper the GateKeeper answer
     */
    private void checkGateKeeper(
            final Map context,
            final String inputGateKeeper,
            final String answerGateKeeper) {
        if (inputGateKeeper != null && answerGateKeeper != null) {
            if (!inputGateKeeper.equals(answerGateKeeper)) {
                markCommentForDeletion(context);
                LOG.info("GateKeeper input doesn't match the expected answer. "
                        + "Marking comment for deletion.");
            } else {
                LOG.info("GateKeeper input matches the expected answer.");
            }
        } else {
            markCommentForDeletion(context);
            LOG.info("Either GateKeeper input or GateKeeper answer doesn't "
                    + "exist. Marking comment for deletion.");
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
