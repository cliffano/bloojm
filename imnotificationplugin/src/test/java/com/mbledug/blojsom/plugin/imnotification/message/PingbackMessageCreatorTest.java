package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.Pingback;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.blog.database.DatabasePingback;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.pingback.event.PingbackAddedEvent;

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
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
    	String title = "Sam I Am";
        String sourceUri = "http://somedummyurl";
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Pingback pingback = new DatabasePingback();
        pingback.setSourceURI(sourceUri);
        pingback.setEntry(entry);
        PingbackAddedEvent event = new PingbackAddedEvent(
                "dummy source",
                calendar.getTime(),
                pingback,
                new DatabaseBlog());
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + "Mon Dec 25 00:00:00 UTC 2000"
                + " - New pingback was added from "
                + sourceUri
                + " to entry '"
                + title
                + "'",
                new PingbackMessageCreator().getMessage(event));
    }
}
