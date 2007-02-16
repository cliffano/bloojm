/**
 * Copyright (c) 2005-2007, Cliffano Subagio
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
package com.mbledug.blojsom.plugin.iptocountry;

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
import org.blojsom.event.EventBroadcaster;
import org.blojsom.event.Listener;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * {@link IpToCountryPlugin} attaches a country code to each blog entry comment.
 * The process of attaching country code is done at 2 places:
 * - when a new comment is added, country code will be added to comments with
 *   IP address, and country code will be persisted as comment meta data.
 * - when blog entries is going to be displayed, comments with IP address
 *   which doesn't yet have a country code will be provided one.
 * @author Cliffano Subagio
 */
public class IpToCountryPlugin implements Plugin, Listener {

    /**
     * Logger for {@link IpToCountryPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(IpToCountryPlugin.class);

    /**
     * Meta data key for country code.
     */
    public static final String METADATA_COUNTRY_CODE = "country-code";

    /**
     * Event broadcaster.
     */
    private EventBroadcaster mEventBroadcaster;

    /**
     * {@link IpToCountryDao} used to retrieve country data.
     */
    private IpToCountryDao mIpToCountryDao;

    /**
     * Array of IP addresses to ignore from country code retrieval process.
     */
    private String[] mIgnoredIpAddresses;

    /**
     * Creates an instance of {@link IpToCountryPlugin}.
     */
    public IpToCountryPlugin() {
        mEventBroadcaster = null;
        mIpToCountryDao = null;
        mIgnoredIpAddresses = new String[0];
    }

    /**
     * Sets the event broadcaster.
     * @param eventBroadcaster the event broadcaster
     */
    public final void setEventBroadcaster(
            final EventBroadcaster eventBroadcaster) {
        mEventBroadcaster = eventBroadcaster;
    }

    /**
     * Sets the DAO for IpToCountry.
     * @param ipToCountryDao the DAO
     */
    public final void setIpToCountryDao(
            final IpToCountryDao ipToCountryDao) {
        mIpToCountryDao = ipToCountryDao;
    }

    /**
     * Sets the ignored IP addresses.
     * @param ignoredIpAddressesCsv comma separated value of the ignored IP
     * addresses.
     */
    public final void setIgnoredIpAddresses(
            final String ignoredIpAddressesCsv) {
        mIgnoredIpAddresses = ignoredIpAddressesCsv.split(",");
    }

    /**
     * Writes plugin init message. Adds this plugin to event broadcaster.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising IpToCountryPlugin.");
        mEventBroadcaster.addListener(this);
    }

    /**
     * Adds country code to comments which don't already have one.
     *
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries with country code added to the comments
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
                if (comment.getMetaData().get(METADATA_COUNTRY_CODE) == null) {
                    addCountryCodeToComment(
                            comment.getIp(), comment.getMetaData());
                }
            }
        }
        return entries;
    }

    /**
     * cleanup method has an empty implementation in {@link IpToCountryPlugin}.
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
        LOG.info("Destroying IpToCountryPlugin.");
    }

    /**
     * handleEvent method has an empty implementation in
     * {@link IpToCountryPlugin}.
     * @param event the event to handle asynchronously
     */
    public final void handleEvent(final Event event) {
    }

    /**
     * Adds country code to comment when there's a new added comment.
     * @param event the event to process synchronously
     */
    public final void processEvent(final Event event) {
        if (event instanceof CommentResponseSubmissionEvent) {
            ResponseSubmissionEvent responseSubmissionEvent =
                (ResponseSubmissionEvent) event;
            Map metaData = responseSubmissionEvent.getMetaData();
            addCountryCodeToComment(responseSubmissionEvent.
                    getHttpServletRequest().getRemoteAddr(), metaData);
        }
    }

    /**
     * Adds country code to meta data. This is done only when IP address is not
     * null and it doesn't exist in the ignored IP addresses array.
     * @param ipAddress the IP address for the country code
     * @param metaData the meta data map which the country code  will be added
     * to
     */
    private void addCountryCodeToComment(
            final String ipAddress, final Map metaData) {

        if (ipAddress != null
                && !IpToCountryHelper.isIgnoredIpAddress(
                        String.valueOf(ipAddress), mIgnoredIpAddresses)) {
            long ipNumber = IpToCountryHelper.calculateIpNumber(
                    String.valueOf(ipAddress));
            try {
                String countryCode = mIpToCountryDao.getCountryCode(ipNumber);
                metaData.put(METADATA_COUNTRY_CODE, countryCode);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Setting country code: " + countryCode
                            + ", for IP address: " + ipAddress
                            + ", with IP number: " + ipNumber);
                }
            } catch (EmptyResultDataAccessException erdae) {
                LOG.error("Unable to retrieve country code for IP address: "
                        + ipAddress + ", IP number: " + ipNumber);
            }
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("Ignoring IP address: " + ipAddress);
        }
    }
}
