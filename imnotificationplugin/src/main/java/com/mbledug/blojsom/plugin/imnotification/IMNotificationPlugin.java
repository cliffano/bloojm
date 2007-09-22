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
package com.mbledug.blojsom.plugin.imnotification;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.blojsom.plugin.admin.event.EntryEvent;
import org.blojsom.util.BlojsomUtils;

import com.mbledug.blojsom.plugin.imnotification.message.MessageFactory;
import com.mbledug.blojsom.plugin.imnotification.service.IMService;

/**
 * {@link IMNotificationPlugin} sends notification messages to Instant Messaging
 * application upon the creation of new entry, comment, trackback, and pingback.
 * @author Cliffano
 */
public class IMNotificationPlugin implements Plugin, Listener {

    /**
     * Logger for {@link IMNotificationPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(
            IMNotificationPlugin.class);

    /**
     * Blog property IM Notification message recipients.
     */
    public static final String PROPERTY_RECIPIENTS_PREFIX =
        "imnotification-recipients-";

    /**
     * Message factory.
     */
    private MessageFactory mMessageFactory;

    /**
     * Event broadcaster.
     */
    private EventBroadcaster mEventBroadcaster;

    /**
     * IM services.
     */
    private Map mServices;

    /**
     * Creates an instance of {@link IMNotificationPlugin}
     * with specified services.
     * @param services the IM services
     */
    public IMNotificationPlugin(final Map services) {
        mServices = services;
        mEventBroadcaster = null;
        mMessageFactory = new MessageFactory();
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
        LOG.info("Initialising IMNotificationPlugin.");
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
     * {@link IMNotificationPlugin}.
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
        LOG.info("Destroying IMNotificationPlugin.");
    }

    /**
     * handleEvent method has an empty implementation in
     * {@link IMNotificationPlugin}.
     * @param event the event to handle asynchronously
     */
    public final void handleEvent(final Event event) {

        if (mMessageFactory.isSupported(event) && event instanceof EntryEvent) {

            Blog blog = ((EntryEvent) event).getBlog();

            for (Iterator it = mServices.keySet().iterator(); it.hasNext();) {
                String id = (String) it.next();
                IMService service = (IMService) mServices.get(id);
                List recipients = BlojsomUtils.csvToList(
                        blog.getProperty(
                        PROPERTY_RECIPIENTS_PREFIX + id));
                if (recipients != null) {
                    String message = new MessageFactory().getMessage(event);
                    service.send(recipients, message);
                }
            }
        }
    }

    /**
     * processEvent method has an empty implementation in
     * {@link IMNotificationPlugin}.
     * @param event the event to process synchronously
     */
    public final void processEvent(final Event event) {
    }
}
