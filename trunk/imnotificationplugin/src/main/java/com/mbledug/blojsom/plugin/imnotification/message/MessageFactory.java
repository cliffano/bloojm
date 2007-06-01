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
package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.event.Event;


/**
 * {@link MessageFactory} creates a message by calling a {@link MessageCreator}
 * corresponding to the event.
 * @author Cliffano
 */
public class MessageFactory {

    /**
     * Logger for {@link MessageFactory}.
     */
    private static final Log LOG = LogFactory.getLog(MessageFactory.class);

    /**
     * A map of supported events where the key-value pair consists of
     * a String value of the supported event class name
     * and
     * its corresponding message creator instance.
     */
    private Map mSupportedEvents;

    /**
     * Creates an instance of {@link MessageFactory}, initialises supported
     * events.
     */
    public MessageFactory() {
        mSupportedEvents = new HashMap();
        mSupportedEvents.put(
                "org.blojsom.plugin.comment.event.CommentAddedEvent",
                new CommentMessageCreator());
        mSupportedEvents.put(
                "org.blojsom.plugin.admin.event.EntryAddedEvent",
                new EntryMessageCreator());
        mSupportedEvents.put(
                "org.blojsom.plugin.trackback.event.TrackbackAddedEvent",
                new TrackbackMessageCreator());
        mSupportedEvents.put(
                "org.blojsom.plugin.pingback.event.PingbackAddedEvent",
                new PingbackMessageCreator());
    }

    /**
     * Checks whether the event is supported by this factory.
     * @param event the event to check
     * @return true if event is supported, false otherwise
     */
    public final boolean isSupported(final Event event) {
        boolean isSupportedEvent = false;
        for (Iterator it = mSupportedEvents.keySet().iterator();
                it.hasNext();) {
            String className = (String) it.next();
            try {
                if (Class.forName(className).isInstance(event)) {
                    isSupportedEvent = true;
                    break;
                }
            } catch (ClassNotFoundException cnfe) {
                LOG.error("Class: " + className + ", does not exist.", cnfe);
            }
        }
        return isSupportedEvent;
    }

    /**
     * Gets the {@link MessageCreator} for a specified event.
     * @param event the event
     * @return the message creator for the event
     */
    private MessageCreator getMessageCreator(final Event event) {
        MessageCreator creator = null;
        for (Iterator it = mSupportedEvents.keySet().iterator();
                it.hasNext();) {
            String className = (String) it.next();
            try {
                if (Class.forName(className).isInstance(event)) {
                    creator = (MessageCreator) mSupportedEvents.get(className);
                    break;
                }
            } catch (ClassNotFoundException cnfe) {
                LOG.error("Class: " + className + ", does not exist.", cnfe);
            }
        }
        return creator;
    }

    /**
     * Gets the message based on the event. An {@link IllegalArgumentException}
     * will be thrown for unsupported event.
     * @param event the event to base the message on
     * @return the message text
     */
    public final String getMessage(final Event event) {

        MessageCreator creator;
        if (!isSupported(event)) {
            throw new IllegalArgumentException("Unable to create message "
            + "for unsupported event: " + event);
        } else {
            creator = getMessageCreator(event);
        }
        return creator.getMessage(event);
    }
}
