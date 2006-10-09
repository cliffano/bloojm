package com.mbledug.blojsom.plugin.gravatar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.event.Listener;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.comment.event.CommentEvent;

public class GravatarPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessAddingGravatarIdToComment() {

        Plugin gravatarPlugin = new GravatarPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutGravatarId()};

        try {
            gravatarPlugin.init();
            entries = gravatarPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            List comments = entries[0].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                Map metaData = comment.getMetaData();
                String gravatarId = String.valueOf(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
                assertNotNull(gravatarId);
                assertEquals(DataFixture.EXPECTED_GRAVATAR_ID, gravatarId);
            }
            gravatarPlugin.cleanup();
            gravatarPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessEntryWithCommentHavingEmptyStringGravatarIdLeftAsIs() {

        Plugin gravatarPlugin = new GravatarPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithEmptyStringGravatarId()};

        try {
            gravatarPlugin.init();
            entries = gravatarPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            List comments = entries[0].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                Map metaData = comment.getMetaData();
                String gravatarId = String.valueOf(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
                assertEquals("", gravatarId);
            }
            gravatarPlugin.cleanup();
            gravatarPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessCommentAddedEventWithGravatarId() {

        Listener gravatarPlugin = new GravatarPlugin();
        CommentAddedEvent event = DataFixture.createCommentAddedEventWithCommentHavingNoGravatarId();
        gravatarPlugin.processEvent(event);
        Comment comment = event.getComment();
        Map metaData = comment.getMetaData();
        String gravatarId = String.valueOf(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
        assertNotNull(gravatarId);
        assertEquals(DataFixture.EXPECTED_GRAVATAR_ID, gravatarId);
    }

    public void testProcessCommentEventLeftCommentAsIs() {

        Listener gravatarPlugin = new GravatarPlugin();
        CommentEvent event = DataFixture.createCommentEvent();
        Comment commentBeforeProcessing = event.getComment();
        gravatarPlugin.processEvent(event);
        Comment commentAfterProcessing = event.getComment();
        assertEquals(commentAfterProcessing, commentBeforeProcessing);
    }

    public void testHandleEventLeftEventAsIs() {

        Listener gravatarPlugin = new GravatarPlugin();
        CommentEvent event = DataFixture.createCommentEvent();
        gravatarPlugin.handleEvent(event);
    }
}
