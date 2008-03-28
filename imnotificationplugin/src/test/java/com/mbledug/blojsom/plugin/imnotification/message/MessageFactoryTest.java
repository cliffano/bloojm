package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.Pingback;
import org.blojsom.blog.Trackback;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.blog.database.DatabasePingback;
import org.blojsom.blog.database.DatabaseTrackback;
import org.blojsom.event.Event;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.pingback.event.PingbackAddedEvent;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;

public class MessageFactoryTest extends TestCase {

    public void testGetMessageWithUnsupportedEventThrowsIllegalArgumentException() {
        try {
            new MessageFactory().getMessage(new Event(null, null));
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithEntryAddedEvent() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        Entry entry = new DatabaseEntry();
        entry.setAuthor("dummy author");
        entry.setTitle("dummy title");
        EntryAddedEvent entryAddedEvent = new EntryAddedEvent(
                "dummy source",
                calendar.getTime(),
                entry,
                new DatabaseBlog());
        assertNotNull(new MessageFactory().getMessage(entryAddedEvent));
    }

    public void testGetMessageWithCommentAddedEvent() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        Entry entry = new DatabaseEntry();
        entry.setTitle("dummy title");
        Comment comment = new DatabaseComment();
        comment.setAuthor("dummy author");
        comment.setEntry(entry);
        CommentAddedEvent commentAddedEvent = new CommentAddedEvent(
                "dummy source",
                calendar.getTime(),
                comment,
                new DatabaseBlog());
        assertNotNull(new MessageFactory().getMessage(commentAddedEvent));
    }

    public void testGetMessageWithTrackbackAddedEvent() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        Entry entry = new DatabaseEntry();
        entry.setTitle("dummy title");
        Trackback trackback = new DatabaseTrackback();
        trackback.setUrl("dummy url");
        trackback.setEntry(entry);
        TrackbackAddedEvent trackbackAddedEvent = new TrackbackAddedEvent(
                "dummy source",
                calendar.getTime(),
                trackback,
                new DatabaseBlog());
        assertNotNull(new MessageFactory().getMessage(trackbackAddedEvent));
    }

    public void testGetMessageWithPingbackAddedEvent() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        Entry entry = new DatabaseEntry();
        entry.setTitle("dummy title");
        Pingback pingback = new DatabasePingback();
        pingback.setSourceURI("dummy sourceUri");
        pingback.setEntry(entry);
        PingbackAddedEvent pingbackAddedEvent = new PingbackAddedEvent(
                "dummy source",
                calendar.getTime(),
                pingback,
                new DatabaseBlog());
        assertNotNull(new MessageFactory().getMessage(pingbackAddedEvent));
    }
}
