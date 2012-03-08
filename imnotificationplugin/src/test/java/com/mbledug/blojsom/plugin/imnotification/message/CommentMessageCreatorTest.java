package com.mbledug.blojsom.plugin.imnotification.message;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;

public class CommentMessageCreatorTest extends TestCase {

    public void testGetMessageWithNonCommentAddedEventThrowsIllegalArgumentException() {
        try {
            new CommentMessageCreator().getMessage(new EntryAddedEvent(null, null, null, null));
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithCommentAddedEventCreatesExpectedMessage() {
        String title = "Sam I Am";
        String author = "Dr. Seuss";
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Comment comment = new DatabaseComment();
        comment.setAuthor(author);
        comment.setEntry(entry);
        CommentAddedEvent event = new CommentAddedEvent(
                "dummy source",
                calendar.getTime(),
                comment,
                new DatabaseBlog());
        String message = new CommentMessageCreator().getMessage(event);
        assertTrue(message.startsWith(MessageCreator.MESSAGE_PREFIX + "Mon Dec 25 00:00:00 "));
        assertTrue(message.endsWith(" 2000 - New comment was added by " + author + " to entry '" + title + "'"));
    }
}
