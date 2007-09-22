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
package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.Date;

import org.blojsom.blog.Entry;
import org.blojsom.blog.Trackback;
import org.blojsom.event.Event;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;

/**
 * {@link TrackbackMessageCreator} creates message in the event of a new
 * trackback added to an entry.
 * @author Cliffano
 */
class TrackbackMessageCreator implements MessageCreator {

    /**
     * {@inheritDoc}
     */
    public String getMessage(final Event event) {

        if (!(event instanceof TrackbackAddedEvent)) {
            throw new IllegalArgumentException("Unable to create message "
                    + "due to invalid event: " + event);
        }

        TrackbackAddedEvent trackbackAddedEvent = (TrackbackAddedEvent) event;
        Trackback trackback = trackbackAddedEvent.getTrackback();
        Entry entry = trackbackAddedEvent.getEntry();
        Date timestamp = trackbackAddedEvent.getTimestamp();
        return MESSAGE_PREFIX
                + timestamp
                + " - New trackback was added from "
                + trackback.getUrl()
                + " to entry '"
                + entry.getTitle()
                + "'";
    }
}
