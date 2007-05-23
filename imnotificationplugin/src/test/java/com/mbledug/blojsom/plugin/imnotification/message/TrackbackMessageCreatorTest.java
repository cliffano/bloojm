package com.mbledug.blojsom.plugin.imnotification.message;

import junit.framework.TestCase;

import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;

import com.mbledug.blojsom.plugin.imnotification.DataFixture;
import com.mbledug.blojsom.plugin.imnotification.message.CommentMessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.MessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.TrackbackMessageCreator;

public class TrackbackMessageCreatorTest extends TestCase {

    public void testGetMessageWithNonTrackbackAddedEventThrowsIllegalArgumentException() {
        try {
            new CommentMessageCreator().getMessage(new EntryAddedEvent(null, null, null, null));
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithTrackbackAddedEventCreatesExpectedMessage() {
        String title = "Sam I Am";
        String url = "http://somedummyurl";
        TrackbackAddedEvent event = DataFixture.createTrackbackAddedEvent(title, url);
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + DataFixture.getChristmasDayIn2000AsString()
                + " - New trackback was added from "
                + url
                + " to entry '"
                + title
                + "'",
                new TrackbackMessageCreator().getMessage(event));
    }
}
