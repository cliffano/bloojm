package com.mbledug.blojsom.plugin.imnotification.message;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;

import com.mbledug.blojsom.plugin.imnotification.DataFixture;
import com.mbledug.blojsom.plugin.imnotification.message.EntryMessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.MessageCreator;

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
        String title = "Sam I Am";
        String author = "Dr. Seuss";
        EntryAddedEvent event = DataFixture.createEntryAddedEvent(author, title);
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + DataFixture.getChristmasDayIn2000AsString()
                + " - New entry '"
                + title
                + "' was added by "
                + author,
                new EntryMessageCreator().getMessage(event));
    }
}
