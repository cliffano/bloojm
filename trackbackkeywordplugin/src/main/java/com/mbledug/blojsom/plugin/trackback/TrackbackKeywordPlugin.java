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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.event.Event;
import org.blojsom.event.Listener;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;
import org.blojsom.util.BlojsomUtils;

/**
 * Checks the content of trackback URL for the existence of all or at least one
 * of the specified keywords which will determine whether the trackback is a
 * spam or not. A trackback spam will then be deleted or moderated.
 * TrackbackKeywordPlugin will not overwrite trackback deletion/moderation
 * status set up by other priorly executed plugins.
 * @author Cliffano Subagio
 */
public class TrackbackKeywordPlugin implements Listener {

    /**
     * Logger for TrackbackKeywordPlugin.
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
    public static final String PROPERTY_KEYWORDS = "keywords";

    /**
     * Plugin property for action on spam suspect.
     */
    public static final String PROPERTY_ACTION = "action";

    /**
     * Plugin property for keyword check type.
     */
    public static final String PROPERTY_CHECK_TYPE = "checktype";

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
     * Creates an instance of TrackbackKeywordPlugin with default
     * UrlTextFetcher.
     */
    public TrackbackKeywordPlugin() {
        mUrlTextFetcher = new UrlTextFetcher();
        mProperties = new Properties();
    }

    /**
     * Creates an instance of TrackbackKeywordPlugin with specified
     * UrlTextFetcher.
     * @param urlTextFetcher the specified UrlTextFetcher
     */
    public TrackbackKeywordPlugin(final UrlTextFetcher urlTextFetcher) {
        mUrlTextFetcher = urlTextFetcher;
        mProperties = new Properties();
    }

    /**
     * Sets TrackbackKeywordPlugin properties.
     * @param properties the plugin properties
     */
    public final void setProperties(final Properties properties) {
        mProperties = properties;
    }

    /**
     * handleEvent method has an empty implementation in TrackbackKeywordPlugin.
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
                try {
                    if (isSpamSuspect(
                            mProperties.getProperty(PROPERTY_CHECK_TYPE),
                            mUrlTextFetcher.fetchText(url),
                            mProperties.getProperty(PROPERTY_KEYWORDS))) {
                        addTrackbackAction(mProperties
                                .getProperty(PROPERTY_ACTION), metaData);
                    }
                } catch (IOException ioe) {
                    LOG.error("Unable to retrieve text for trackback with url: "
                            + url + ", due to exception: " + ioe);
                }
            }
        }
    }

    /**
     * Sets proxy to UrlTextFetcher.
     * @param urlTextFetcher the UrlTextFetcher to set proxy to.
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
