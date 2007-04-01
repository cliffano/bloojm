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
package com.mbledug.blojsom.plugin.previousentries;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.fetcher.FetcherException;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;

/**
 * {@link PreviousEntriesPlugin} retrieves previous entries on a permalink view
 * of an entry.
 * @author Cliffano Subagio
 */
public class PreviousEntriesPlugin implements Plugin {

    /**
     * Logger for {@link PreviousEntriesPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(
            PreviousEntriesPlugin.class);

    /**
     * Context name for the previous entries to pass to velocity template.
     */
    public static final String CONTEXT_PREVIOUS_ENTRIES = "PREVIOUS_ENTRIES";

    /**
     * Plugin property for the number of previous entries to load.
     */
    public static final String PROPERTY_PREVIOUS_ENTRIES_NUM =
            "previous-entries-num";

    /**
     * Default number of previous entries to load.
     */
    private static final int DEFAULT_PREVIOUS_ENTRIES_NUM = 10;

    /**
     * The fetcher used for loading previous entries.
     */
    private PreviousEntriesDatabaseFetcher mFetcher;

    /**
     * Creates an instance of {@link PreviousEntriesPlugin} with specified
     * fetcher.
     * @param fetcher the fetcher used for loading previous entries
     */
    public PreviousEntriesPlugin(final PreviousEntriesDatabaseFetcher fetcher) {
        mFetcher = fetcher;
    }

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising PreviousEntriesPlugin.");
    }

    /**
     * Retrieves previous entries and stores them in the context map.
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

        if (entries != null && entries.length == 1) {
            try {

                int numPreviousEntries = DEFAULT_PREVIOUS_ENTRIES_NUM;
                if (blog.getProperty(PROPERTY_PREVIOUS_ENTRIES_NUM) != null) {
                    numPreviousEntries = Integer.parseInt(blog.getProperty(
                            PROPERTY_PREVIOUS_ENTRIES_NUM));
                }

                context.put(CONTEXT_PREVIOUS_ENTRIES,
                        mFetcher.loadPreviousEntries(
                                blog,
                                entries[0],
                                numPreviousEntries));
            } catch (FetcherException fe) {
                LOG.error("An error occured while loading previous entries",
                        fe);
            }
        }

        return entries;
    }

    /**
     * cleanup method has an empty implementation in
     * {@link PreviousEntriesPlugin}.
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
        LOG.info("Destroying PreviousEntriesPlugin.");
    }
}
