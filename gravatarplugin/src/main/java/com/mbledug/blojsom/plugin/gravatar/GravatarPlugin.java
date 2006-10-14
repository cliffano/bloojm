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
package com.mbledug.blojsom.plugin.gravatar;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.event.Event;
import org.blojsom.event.Listener;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.comment.event.CommentEvent;

/**
 * GravatarPlugin attaches a Gravatar ID to each blog entry comment, which can
 * then be used to display Gravatar (Globally Recognized Avatar) image available
 * from <a href="http://gravatar.com">gravatar.com</a> .
 * The process of attaching Gravatar ID is done at 2 places:
 * - when a new comment is added, Gravatar ID will be added to comments with
 *   author email and Gravatar ID will be persisted as comment meta data.
 * - when blog entries is going to be displayed, comments with author email
 *   which doesn't yet have Gravatar ID will be provided one.
 * @author Cliffano Subagio
 */
public class GravatarPlugin implements Plugin, Listener {

    /**
     * Logger for GravatarPlugin.
     */
    private static final Log LOG = LogFactory.getLog(GravatarPlugin.class);

    /**
     * Meta data key for Gravatar ID.
     */
    public static final String METADATA_GRAVATAR_ID = "gravatar-id";

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising GravatarPlugin.");
    }

    /**
     * Adds Gravatar ID to comments without one.
     *
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries with Gravatar ID added to the comments
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {

        for (int i = 0; i < entries.length; i++) {
            List comments = entries[i].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                if (comment.getMetaData().get(METADATA_GRAVATAR_ID) == null) {
                    addGravatarIdToComment(comment);
                }
            }
        }

        return entries;
    }

    /**
     * cleanup method has an empty implementation in GravatarPlugin.
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
        LOG.info("Destroying GravatarPlugin.");
    }

    /**
     * handleEvent method has an empty implementation in GravatarPlugin.
     * @param event the event to handle asynchronously
     */
    public final void handleEvent(final Event event) {
    }

    /**
     * Adds Gravatar ID to comment when there's a new added comment.
     * @param event the event to process synchronously
     */
    public final void processEvent(final Event event) {
        if (event instanceof CommentAddedEvent) {
            Comment comment = ((CommentEvent) event).getComment();
            addGravatarIdToComment(comment);
        }
    }

    /**
     * Adds Gravatar ID to comment meta data. This is done only when comment
     * author's email is not null.
     * @param comment the comment which the Gravatar ID  will be added to
     */
    private void addGravatarIdToComment(final Comment comment) {
        String email = comment.getAuthorEmail();
        if (email != null) {
            try {
                String gravatarId = GravatarHelper.getGravatarId(email);
                comment.getMetaData().put(METADATA_GRAVATAR_ID, gravatarId);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Setting Gravatar ID: " + gravatarId
                            + " for email: " + email);
                }
            } catch (NoSuchAlgorithmException nae) {
                LOG.error("Unable to get Gravatar ID for email: " + email
                        + ", due to exception: " + nae);
            } catch (UnsupportedEncodingException uee) {
                LOG.error("Unable to get Gravatar ID for email: " + email
                        + ", due to exception: " + uee);
            }
        }
    }
}
