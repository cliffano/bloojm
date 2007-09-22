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
 *   * Neither the name of Qoqoa nor the names of its contributors
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
package com.mbledug.blojsom.plugin.pager;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.BlogEntry;
import org.blojsom.blog.BlogUser;
import org.blojsom.blog.BlojsomConfiguration;
import org.blojsom.plugin.BlojsomPlugin;
import org.blojsom.plugin.BlojsomPluginException;

/**
 * PagerPlugin sets context entries required to generate page by page
 * navigation.
 * @author Cliffano Subagio
 */
public class PagerPlugin implements BlojsomPlugin {

    /**
     * Logger for {@link PagerPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(PagerPlugin.class);

    /**
     * Context entry for current page number.
     */
    public static final String CONTEXT_ENTRY_CURRENT_PAGE =
        "BLOJSOM_PLUGIN_PAGER_CURRENT_PAGE";

    /**
     * Context entry for total number of pages.
     */
    public static final String CONTEXT_ENTRY_TOTAL_PAGES =
        "BLOJSOM_PLUGIN_PAGER_TOTAL_PAGES";

    /**
     * Context entry for page size.
     */
    public static final String CONTEXT_ENTRY_PAGE_SIZE =
        "BLOJSOM_PLUGIN_PAGER_PAGE_SIZE";

    /**
     * Paramater name of current page number.
     */
    public static final String PARAM_PAGE_NUM = "page-num";

    /**
     * Writes plugin init message.
     * @param servletConfig servlet configuration
     * @param blojsomConfiguration Blojsom configuration
     * @throws BlojsomPluginException when there's an error in initialising this
     * plugin
     */
    public final void init(
            final ServletConfig servletConfig,
            final BlojsomConfiguration blojsomConfiguration)
            throws BlojsomPluginException {
        LOG.info("Initialising PagerPlugin.");
    }

    /**
     * Puts the values of current page, last page, and page size to context map.
     *
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blogUser blog user instance
     * @param context context
     * @param blogEntries blog entries retrieved for the particular request
     * @return the original entries "as is"
     * @throws BlojsomPluginException when there's an error processing the blog
     * entries
     */
    public final BlogEntry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final BlogUser blogUser,
            final Map context,
            final BlogEntry[] blogEntries)
            throws BlojsomPluginException {

        int pageSize = blogUser.getBlog().getBlogDisplayEntries();
        int totalPages = PagerHelper.getTotalPages(
                blogEntries.length, pageSize);
        int currentPage = PagerHelper.getCurrentPage(
                httpServletRequest.getParameter(PARAM_PAGE_NUM));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Page size: " + pageSize
                    + ", total pages: " + totalPages
                    + ", current page: " + currentPage);
        }

        context.put(CONTEXT_ENTRY_PAGE_SIZE, new Integer(pageSize));
        context.put(CONTEXT_ENTRY_TOTAL_PAGES, new Integer(totalPages));
        context.put(CONTEXT_ENTRY_CURRENT_PAGE, new Integer(currentPage));

        return blogEntries;
    }

    /**
     * cleanup method has an empty implementation in Pager Plugin.
     * @throws BlojsomPluginException when there's an error performing cleanup
     * of this plugin
     */
    public final void cleanup() throws BlojsomPluginException {
    }

    /**
     * Writes plugin destroy message.
     * @throws BlojsomPluginException when there's an error in finalising this
     * plugin
     */
    public final void destroy() throws BlojsomPluginException {
        LOG.info("Destroying PagerPlugin.");
    }
}
