package com.mbledug.blojsom.plugin.imnotification.message;

import junit.framework.TestCase;

import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;

import com.mbledug.blojsom.plugin.imnotification.DataFixture;
import com.mbledug.blojsom.plugin.imnotification.message.CommentMessageCreator;
import com.mbledug.blojsom.plugin.imnotification.message.MessageCreator;

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
        CommentAddedEvent event = DataFixture.createCommentAddedEvent(author, title);
        assertEquals(
                MessageCreator.MESSAGE_PREFIX
                + DataFixture.getChristmasDayIn2000AsString()
                + " - New comment was added by "
                + author
                + " to entry '"
                + title
                + "'",
                new CommentMessageCreator().getMessage(event));
    }
}
