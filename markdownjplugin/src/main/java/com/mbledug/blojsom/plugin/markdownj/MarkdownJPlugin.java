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
package com.mbledug.blojsom.plugin.markdownj;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.util.BlojsomUtils;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * {@link MarkdownJPlugin} converts Markdown syntax to HTML.
 * @author Cliffano Subagio
 */
public class MarkdownJPlugin implements Plugin {

    /**
     * Logger for {@link MarkdownJPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(MarkdownJPlugin.class);

    /**
     * Metadata key to identify a Markdown post.
     */
    public static final String METADATA_RUN_MARKDOWN = "run-markdown";

    /**
     * Extension of Markdown post.
     */
    public static final String MARKDOWN_EXTENSION = ".markdown";

    /**
     * MarkdownJ processor.
     */
    private MarkdownProcessor mProcessor;

    /**
     * Creates an instance of {@link MarkdownJPlugin}, initialises MarkdownJ
     * processor.
     */
    public MarkdownJPlugin() {
        mProcessor = new MarkdownProcessor();
    }

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising MarkdownJPlugin.");
    }

    /**
     * Converts Markdown entries to HTML.
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
            Entry entry = entries[i];
            if ((entry.getPostSlug().endsWith(MARKDOWN_EXTENSION)
                    || BlojsomUtils.checkMapForKey(
                            entry.getMetaData(), METADATA_RUN_MARKDOWN))) {

                String markdownDescription = entry.getDescription();
                String htmlDescription = mProcessor.markdown(
                        markdownDescription);
                entry.setDescription(htmlDescription);
            }
        }
        return entries;
    }

    /**
     * cleanup method has an empty implementation in {@link MarkdownJPlugin}.
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
        LOG.info("Destroying MarkdownJPlugin.");
    }
}
