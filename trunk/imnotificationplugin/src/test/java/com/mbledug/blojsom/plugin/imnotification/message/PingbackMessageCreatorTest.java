package com.mbledug.blojsom.plugin.imnotification.message;

import junit.framework.TestCase;

import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.pingback.event.PingbackAddedEvent;

import com.mbledug.blojsom.plugin.imnotification.DataFixture;
import com.mbledug.blojsom.plugin.imnotification.message.CommentMessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.MessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.PingbackMessageCreator;

public class PingbackMessageCreatorTest extends TestCase {

    public void testGetMessageWithNonPingbackAddedEventThrowsIllegalArgumentException() {
        try {
            new CommentMessageCreator().getMessage(new EntryAddedEvent(null, null, null, null));
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithPingbackAddedEventCreatesExpectedMessage() {
        String title = "Sam I Am";
        String sourceUri = "http://somedummyurl";
        PingbackAddedEvent event = DataFixture.createPingbackAddedEvent(title, sourceUri);
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + DataFixture.getChristmasDayIn2000AsString()
                + " - New pingback was added from "
                + sourceUri
                + " to entry '"
                + title
                + "'",
                new PingbackMessageCreator().getMessage(event));
    }
}
