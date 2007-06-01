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
package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.event.Event;
import org.blojsom.event.EventBroadcaster;
import org.blojsom.event.Listener;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;
import org.blojsom.util.BlojsomUtils;

/**
 * Checks the content of trackback URL for the existence of all or at least one
 * of the specified keywords which will determine whether the trackback is a
 * spam or not. A trackback spam will then be deleted or moderated.
 * {@link TrackbackKeywordPlugin} will not overwrite trackback
 * deletion/moderation status set up by other priorly executed plugins.
 * @author Cliffano Subagio
 */
public class TrackbackKeywordPlugin implements Plugin, Listener {

    /**
     * Logger for {@link TrackbackKeywordPlugin}.
     */
    private static final Log LOG = LogFactory.
            getLog(TrackbackKeywordPlugin.class);

    /**
     * Blog property whether plugin is enabled.
     */
    public static final String PROPERTY_ENABLED = "trackbackkeyword-enabled";

    /**
     * Plugin property for comma separated keywords.
     */
    public static final String PROPERTY_KEYWORDS = "trackbackkeyword-keywords";

    /**
     * Plugin property for action on spam suspect.
     */
    public static final String PROPERTY_ACTION = "trackbackkeyword-action";

    /**
     * Plugin property for keyword check type.
     */
    public static final String PROPERTY_CHECK_TYPE =
            "trackbackkeyword-checktype";

    /**
     * Plugin property for proxy host.
     */
    public static final String PROPERTY_PROXY_HOST = "proxy-host";

    /**
     * Plugin property for proxy port.
     */
    public static final String PROPERTY_PROXY_PORT = "proxy-port";

    /**
     * Plugin property for proxy username.
     */
    public static final String PROPERTY_PROXY_USERNAME = "proxy-username";

    /**
     * Plugin property for proxy password.
     */
    public static final String PROPERTY_PROXY_PASSWORD = "proxy-password";

    /**
     * Delete action on spam suspect.
     */
    public static final String ACTION_DELETE = "delete";

    /**
     * Check type for checking the existence of all keywords.
     */
    public static final String CHECK_TYPE_ALL = "all";

    /**
     * Separator for keywords.
     */
    private static final String KEYWORDS_SEPARATOR = ",";

    /**
     * UrlTextFetcher.
     */
    private UrlTextFetcher mUrlTextFetcher;

    /**
     * Plugin properties.
     */
    private Properties mProperties;

    /**
     * Event broadcaster.
     */
    private EventBroadcaster mEventBroadcaster;

    /**
     * Creates an instance of {@link TrackbackKeywordPlugin} with default
     * {@link UrlTextFetcher}.
     */
    public TrackbackKeywordPlugin() {
        mUrlTextFetcher = new UrlTextFetcher();
        mProperties = new Properties();
        mEventBroadcaster = null;
    }

    /**
     * Creates an instance of {@link TrackbackKeywordPlugin} with specified
     * {@link UrlTextFetcher}.
     * @param urlTextFetcher the specified {@link UrlTextFetcher}
     */
    public TrackbackKeywordPlugin(final UrlTextFetcher urlTextFetcher) {
        mUrlTextFetcher = urlTextFetcher;
        mProperties = new Properties();
        mEventBroadcaster = null;
    }

    /**
     * Sets {@link TrackbackKeywordPlugin} properties.
     * @param properties the plugin properties
     */
    public final void setProperties(final Properties properties) {
        mProperties = properties;
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
     * Writes plugin init message. Adds this plugin to event broadcaster.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising TrackbackKeywordPlugin.");
        mEventBroadcaster.addListener(this);
    }

    /**
     * Returns unmodified entries.
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return unmodified entries
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {
        return entries;
    }

    /**
     * cleanup method has an empty implementation in
     * {@link TrackbackKeywordPlugin}.
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
        LOG.info("Destroying TrackbackKeywordPlugin.");
    }

    /**
     * handleEvent method has an empty implementation in
     * {@link TrackbackKeywordPlugin}.
     * @param event the event to handle asynchronously
     */
    public final void handleEvent(final Event event) {
    }

    /**
     * Checks whether newly submitted trackback is a suspected spam or not.
     * Suspected spam will then be deleted or moderated.
     * @param event the event to process synchronously
     */
    public final void processEvent(final Event event) {

        if (event instanceof TrackbackResponseSubmissionEvent) {
            TrackbackResponseSubmissionEvent trackbackEvent =
                    (TrackbackResponseSubmissionEvent) event;
            Blog blog = trackbackEvent.getBlog();
            Map metaData = trackbackEvent.getMetaData();

            boolean isEnabled = ("true".equalsIgnoreCase(
                    blog.getProperty(PROPERTY_ENABLED)));
            boolean isToBeDeleted = (metaData.get(TrackbackPlugin
                    .BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY)
                    != null);
            boolean isToBeModerated = (metaData.get(TrackbackModerationPlugin.
                    BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED)
                    != null);

            if (isEnabled && !isToBeDeleted && !isToBeModerated) {
                String url = trackbackEvent.getSubmitterItem2();
                setProxy(
                        mUrlTextFetcher,
                        mProperties.getProperty(PROPERTY_PROXY_HOST),
                        mProperties.getProperty(PROPERTY_PROXY_PORT),
                        mProperties.getProperty(PROPERTY_PROXY_USERNAME),
                        mProperties.getProperty(PROPERTY_PROXY_PASSWORD));

                String action = blog.getProperty(PROPERTY_ACTION);
                try {
                    if (isSpamSuspect(
                            blog.getProperty(PROPERTY_CHECK_TYPE),
                            mUrlTextFetcher.fetchText(url),
                            blog.getProperty(PROPERTY_KEYWORDS))) {
                        addTrackbackAction(action, metaData);
                    }
                } catch (IOException ioe) {
                    LOG.error("Unable to retrieve text for trackback with url: "
                            + url, ioe);
                    addTrackbackAction(action, metaData);
                }
            }
        }
    }

    /**
     * Sets proxy to {@link UrlTextFetcher}.
     * @param urlTextFetcher the {@link UrlTextFetcher} to set proxy to.
     * @param proxyHost proxy host name
     * @param proxyPort proxy port number
     * @param proxyUsername proxy username
     * @param proxyPassword proxy password
     */
    private void setProxy(
            final UrlTextFetcher urlTextFetcher,
            final String proxyHost,
            final String proxyPort,
            final String proxyUsername,
            final String proxyPassword) {
        if (proxyHost != null && proxyPort != null) {
            if (proxyUsername != null && proxyPassword != null) {
                urlTextFetcher.setProxy(
                        proxyHost, Integer.parseInt(proxyPort),
                        proxyUsername, proxyPassword);
            } else {
                urlTextFetcher.setProxy(proxyHost, Integer.parseInt(proxyPort));
            }
        }
    }
    /**
     * Determines whether text is a spam suspect by checking the existence
     * certain of keywords within the text. If keywords is unspecified, text
     * will automatically considered to be not spam.
     * @param checkType text check type, can be 'has all' or 'has at least one',
     * defaulted to 'has at least one' if unspecified
     * @param text the text to check
     * @param keywordsCsv comma separated keywords
     * @return true when text is suspected to be a spam, false otherwise
     */
    private boolean isSpamSuspect(
            final String checkType,
            final String text,
            final String keywordsCsv) {

        boolean isSpamSuspect;

        if (BlojsomUtils.checkNullOrBlank(keywordsCsv)) {
            isSpamSuspect = false;
        } else {
            String[] keywords;
            if (keywordsCsv.indexOf(KEYWORDS_SEPARATOR) >= 0) {
                keywords = keywordsCsv.split(KEYWORDS_SEPARATOR);
            } else {
                keywords = new String[]{keywordsCsv};
            }
            KeywordChecker keywordChecker = new KeywordChecker(keywords);
            if (CHECK_TYPE_ALL.equalsIgnoreCase(checkType)) {
                isSpamSuspect = !keywordChecker.hasAllKeywords(text);
            } else {
                isSpamSuspect = !keywordChecker.hasAtLeastOneKeyword(text);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("is spam suspect: " + isSpamSuspect
                    + ", check type: " + checkType
                    + ", keywords: " + keywordsCsv
                    + ", text: " + text);
        }
        return isSpamSuspect;
    }

    /**
     * Add meta data depending on the action, either delete or moderate. If
     * unspecified, it's defaulted to moderate.
     * @param action the action to be applied to trackback meta data
     * @param metaData trackback meta data
     */
    private void addTrackbackAction(final String action, final Map metaData) {
        if (ACTION_DELETE.equalsIgnoreCase(action)) {
            metaData.put(
                    TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY,
                    Boolean.TRUE);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Marking trackback to be deleted.");
            }
        } else {
            metaData.put(
                    TrackbackModerationPlugin.
                        BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED,
                    Boolean.FALSE);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Marking trackback to be moderated.");
            }
        }
    }
}
