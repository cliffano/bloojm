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

import java.awt.Color;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.util.BlojsomUtils;

/**
 * {@link BlogTimesPlugin} generates a graph image summary of the blog entries'
 * posting times with configurable colors and sizes.
 * This class retrieves the entries dates and passes it as session attribute to
 * {@link BlogTimesPlugin}.
 * @author Cliffano Subagio
 */
public class BlogTimesPlugin implements Plugin {

    /**
     * Log for BlogTimesPlugin.
     */
    private static final Log LOG = LogFactory.getLog(BlogTimesPlugin.class);

    /**
     * Session attribute name for storing an array of Dates presenting
     * blog times.
     */
    public static final String SESSION_ATTR_DATES = "blogtimes-dates";

    /**
     * Session attribute name for storing {@link BarGraphImageCreator}.
     */
    public static final String SESSION_ATTR_CREATOR = "blogtimes-creator";

    /**
     * Plugin property for background color.
     */
    public static final String PROPERTY_BACKGROUND_COLOR =
            "image-background-color";

    /**
     * Plugin property for box border color.
     */
    public static final String PROPERTY_BOX_BORDER_COLOR = "bar-border-color";

    /**
     * Plugin property for box background color.
     */
    public static final String PROPERTY_BOX_BACKGROUND_COLOR =
            "bar-background-color";

    /**
     * Plugin property for timeline color.
     */
    public static final String PROPERTY_TIMELINE_COLOR = "bar-timeline-color";

    /**
     * Plugin property for time interval color.
     */
    public static final String PROPERTY_TIME_INTERVAL_COLOR =
            "bar-timeinterval-color";

    /**
     * Plugin property for font color.
     */
    public static final String PROPERTY_FONT_COLOR = "font-color";

    /**
     * Plugin property for box height.
     */
    public static final String PROPERTY_BOX_HEIGHT = "bar-height";

    /**
     * Plugin property for box width.
     */
    public static final String PROPERTY_BOX_WIDTH = "bar-width";

    /**
     * Plugin properties.
     */
    private Properties mProperties;

    /**
     * Bar graph image creator.
     */
    private BarGraphImageCreator mBarGraphImageCreator;

    /**
     * Creates an instance of {@link BlogTimesPlugin}}.
     */
    public BlogTimesPlugin() {
        mProperties = new Properties();
    }

    /**
     * Sets {@link BlogTimesPlugin} properties.
     * @param properties the plugin properties
     */
    public final void setProperties(final Properties properties) {
        mProperties = properties;
    }

    /**
     * Writes plugin init message. Initialises {@link BarGraphImageCreator}..
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising BlogTimesPlugin.");
        try {
            initBarGraphCreator();
        } catch (IllegalArgumentException iae) {
            throw new PluginException(
                    "Invalid BlogTimesPlugin configuration properties. ");
        }
    }

    /**
     * Retrieves the entries' dates and stores them in an array within a session
     * attribute.
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return the original/unmodified blog entries
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {

        Date[] dates = new Date[entries.length];
        for (int i = 0; i < entries.length; i++) {
            dates[i] = entries[i].getDate();
        }
        httpServletRequest.getSession().setAttribute(SESSION_ATTR_DATES, dates);

        httpServletRequest.getSession().setAttribute(SESSION_ATTR_CREATOR,
                mBarGraphImageCreator);

        return entries;
    }

    /**
     * cleanup method has an empty implementation in BlogTimesPlugin.
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
        LOG.info("Destroying BlogTimesPlugin.");
    }

    /**
     * Initialises bar graph creator and applies configuration properties.
     */
    private void initBarGraphCreator() {
        mBarGraphImageCreator = new BarGraphImageCreator();
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_BACKGROUND_COLOR))) {
            Color backgroundColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_BACKGROUND_COLOR));
            mBarGraphImageCreator.setBackgroundColor(backgroundColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_BOX_BORDER_COLOR))) {
            Color boxBorderColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_BOX_BORDER_COLOR));
            mBarGraphImageCreator.setBoxBorderColor(boxBorderColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_BOX_BACKGROUND_COLOR))) {
            Color boxBackgroundColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_BOX_BACKGROUND_COLOR));
            mBarGraphImageCreator.setBoxBackgroundColor(boxBackgroundColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_TIMELINE_COLOR))) {
            Color timelineColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_TIMELINE_COLOR));
            mBarGraphImageCreator.setTimelineColor(timelineColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_TIME_INTERVAL_COLOR))) {
            Color timeIntervalColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_TIME_INTERVAL_COLOR));
            mBarGraphImageCreator.setTimeIntervalColor(timeIntervalColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_FONT_COLOR))) {
            Color fontColor = BlogTimesHelper.hexToColor(
                    mProperties.getProperty(PROPERTY_FONT_COLOR));
            mBarGraphImageCreator.setFontColor(fontColor);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_BOX_HEIGHT))) {
            int boxHeight = Integer.parseInt(
                    mProperties.getProperty(PROPERTY_BOX_HEIGHT));
            mBarGraphImageCreator.setBoxHeight(boxHeight);
        }
        if (!BlojsomUtils.checkNullOrBlank(
                mProperties.getProperty(PROPERTY_BOX_WIDTH))) {
            int boxWidth = Integer.parseInt(
                    mProperties.getProperty(PROPERTY_BOX_WIDTH));
            mBarGraphImageCreator.setBoxWidth(boxWidth);
        }
    }
}
