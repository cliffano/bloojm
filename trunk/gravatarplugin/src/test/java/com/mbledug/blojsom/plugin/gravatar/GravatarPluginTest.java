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
import org.blojsom.event.SimpleEventBroadcaster;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;

public class GravatarPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessAddingGravatarIdToComment() {

        GravatarPlugin gravatarPlugin = new GravatarPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutGravatarId()};

        try {
            gravatarPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
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

        GravatarPlugin gravatarPlugin = new GravatarPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithEmptyStringGravatarId()};

        try {
            gravatarPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
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

    public void testProcessCommentResponseSubmissionEventAddsGravatarIdToMetaData() {

        Listener gravatarPlugin = new GravatarPlugin();
        CommentResponseSubmissionEvent event = DataFixture.createCommentResponseSubmissionEventWithCommentHavingNoGravatarId();
        gravatarPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNotNull(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
        String gravatarId = String.valueOf(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
        assertEquals(DataFixture.EXPECTED_GRAVATAR_ID, gravatarId);
    }

    public void testProcessResponseSubmissionEventDoesntAddsGravatarIdToMetaData() {

        Listener gravatarPlugin = new GravatarPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        gravatarPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
    }

    public void testHandleEventLeftEventAsIs() {

        Listener gravatarPlugin = new GravatarPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        gravatarPlugin.handleEvent(event);
    }
}
