package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.Trackback;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.blog.database.DatabaseTrackback;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;

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
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        String title = "Sam I Am";
        String url = "http://somedummyurl";
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Trackback trackback = new DatabaseTrackback();
        trackback.setUrl(url);
        trackback.setEntry(entry);
        TrackbackAddedEvent event = new TrackbackAddedEvent(
                "dummy source",
                calendar.getTime(),
                trackback,
                new DatabaseBlog());
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + "Mon Dec 25 00:00:00 UTC 2000"
                + " - New trackback was added from "
                + url
                + " to entry '"
                + title
                + "'",
                new TrackbackMessageCreator().getMessage(event));
    }
}
