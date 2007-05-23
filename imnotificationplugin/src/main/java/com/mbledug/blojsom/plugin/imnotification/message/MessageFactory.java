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

import org.blojsom.event.Event;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.pingback.event.PingbackAddedEvent;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;


/**
 * {@link MessageFactory} creates a message by calling a {@link MessageCreator}
 * corresponding to the event.
 * @author Cliffano
 */
public class MessageFactory {

    /**
     * Gets the message based on the event. An {@link IllegalArgumentException}
     * will be thrown for unsupported event.
     * @param event the event to base the message on
     * @return the message text
     */
    public final String getMessage(final Event event) {

        MessageCreator creator;
        if (event instanceof EntryAddedEvent) {
            creator = new EntryMessageCreator();
        } else if (event instanceof CommentAddedEvent) {
            creator = new CommentMessageCreator();
        } else if (event instanceof TrackbackAddedEvent) {
            creator = new TrackbackMessageCreator();
        } else if (event instanceof PingbackAddedEvent) {
            creator = new PingbackMessageCreator();
        } else {
            throw new IllegalArgumentException("Unable to create message "
                    + "for unsupported event: " + event);
        }
        return creator.getMessage(event);
    }
}
