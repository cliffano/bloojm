package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;

public class EntryMessageCreatorTest extends TestCase {

    public void testGetMessageWithNonEntryAddedEventThrowsIllegalArgumentException() {
        String title = "Sam I Am";
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Comment comment = new DatabaseComment();
        comment.setEntry(entry);
        try {
            new EntryMessageCreator().getMessage(new CommentAddedEvent(null, null, comment, null));
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithEntryAddedEventCreatesExpectedMessage() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        calendar.setTimeZone(TimeZone.getTimeZone("EST"));
        String title = "Sam I Am";
        String author = "Dr. Seuss";
        Entry entry = new DatabaseEntry();
        entry.setAuthor(author);
        entry.setTitle(title);
        EntryAddedEvent entryAddedEvent = new EntryAddedEvent(
                "dummy source",
                calendar.getTime(),
                entry,
                new DatabaseBlog());
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + "Mon Dec 25 00:00:00 EST 2000"
                + " - New entry '"
                + title
                + "' was added by "
                + author,
                new EntryMessageCreator().getMessage(entryAddedEvent));
    }
}
