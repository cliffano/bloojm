package com.mbledug.blojsom.plugin.gravatar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.event.Listener;
import org.blojsom.event.SimpleEventBroadcaster;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.easymock.classextension.EasyMock;

public class GravatarPluginTest extends TestCase {

    public void testProcessAddingGravatarIdToComment() {

        GravatarPlugin gravatarPlugin = new GravatarPlugin();

        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail("foo@bar.com");
        comment.setMetaData(new HashMap());
        List comments = new ArrayList();
        comments.add(comment);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        Entry[] entries = new Entry[] {entry};

        try {
            gravatarPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            gravatarPlugin.init();

            entries = gravatarPlugin.process(
                    (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                    (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            for (Iterator it = entries[0].getComments().iterator(); it.hasNext();) {
                Comment itComment = (Comment) it.next();
                Map itMetaData = itComment.getMetaData();
                String gravatarId = String.valueOf(itMetaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
                assertNotNull(gravatarId);
                assertEquals("f3ada405ce890b6f8204094deb12d8a8", gravatarId);
            }
            gravatarPlugin.cleanup();
            gravatarPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessEntryWithCommentHavingEmptyStringGravatarIdLeftAsIs() {

        GravatarPlugin gravatarPlugin = new GravatarPlugin();

        Map metaData =  new HashMap();
        metaData.put(GravatarPlugin.METADATA_GRAVATAR_ID, "");
        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail("foo@bar.com");
        comment.setMetaData(metaData);
        List comments = new ArrayList();
        comments.add(comment);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        Entry[] entries = new Entry[] {entry};

        try {
            gravatarPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            gravatarPlugin.init();

            entries = gravatarPlugin.process(
                    (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                    (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            for (Iterator it = entries[0].getComments().iterator(); it.hasNext();) {
                Comment itComment = (Comment) it.next();
                Map itMetaData = itComment.getMetaData();
                String gravatarId = String.valueOf(itMetaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
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
        CommentResponseSubmissionEvent event = new CommentResponseSubmissionEvent(
                new Object(),
                new Date(),
                new DatabaseBlog(),
                (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                "Dummy Submitter",
                "foo@bar.com",
                "http://dummyurl.org",
                "Dummy Comment Description",
                new DatabaseEntry(),
                new HashMap());
        gravatarPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNotNull(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
        String gravatarId = String.valueOf(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
        assertEquals("f3ada405ce890b6f8204094deb12d8a8", gravatarId);
    }

    public void testProcessResponseSubmissionEventDoesntAddsGravatarIdToMetaData() {

        Listener gravatarPlugin = new GravatarPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        gravatarPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(GravatarPlugin.METADATA_GRAVATAR_ID));
    }

    public void testHandleEventLeftEventAsIs() {

        Listener gravatarPlugin = new GravatarPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        gravatarPlugin.handleEvent(event);
    }

    private ResponseSubmissionEvent createResponseSubmissionEvent() {

        return new ResponseSubmissionEvent(
                new Object(),
                new Date(),
                new DatabaseBlog(),
                (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                "Dummy Submitter",
                "foo@bar.com",
                "http://dummyurl.org",
                "Dummy Comment Description",
                new DatabaseEntry(),
                new HashMap());
    }
}
