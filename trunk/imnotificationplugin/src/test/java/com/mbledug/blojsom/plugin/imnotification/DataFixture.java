package com.mbledug.blojsom.plugin.imnotification;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Blog;
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
import org.blojsom.event.SimpleEventBroadcaster;
import org.blojsom.plugin.admin.event.EntryAddedEvent;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.pingback.event.PingbackAddedEvent;
import org.blojsom.plugin.trackback.event.TrackbackAddedEvent;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import com.mbledug.blojsom.plugin.imnotification.service.IMService;

public class DataFixture extends MockObjectTestCase {

    SimpleEventBroadcaster createSimpleEventBroadcaster() {
        Mock mockSimpleEventBroadcaster = mock(SimpleEventBroadcaster.class);
        mockSimpleEventBroadcaster.expects(once()).method("addListener");
        return (SimpleEventBroadcaster) mockSimpleEventBroadcaster.proxy();
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }

    public static Date getChristmasDayIn2000() {
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        return calendar.getTime();
    }

    public static String getChristmasDayIn2000AsString() {
        return "Mon Dec 25 00:00:00 EST 2000";
    }

    Map createServiceMap(String serviceId) {
        Mock mockIMService = mock(IMService.class);
        mockIMService
                .expects(once())
                .method("send");
        Map services = new HashMap();
        services.put(serviceId, (IMService) mockIMService.proxy());
        return services;
    }

    public static Event createUnsupportedEvent() {
        return new Event(null, null);
    }

    public static CommentAddedEvent createCommentAddedEvent(String author, String title) {
        return createCommentAddedEvent(author, title, new DatabaseBlog());
    }

    public static CommentAddedEvent createCommentAddedEvent(String author, String title, Blog blog) {
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Comment comment = new DatabaseComment();
        comment.setAuthor(author);
        comment.setEntry(entry);
        return new CommentAddedEvent(
                "dummy source",
                DataFixture.getChristmasDayIn2000(),
                comment,
                blog);
    }

    public static EntryAddedEvent createEntryAddedEvent(String author, String title) {
        Entry entry = new DatabaseEntry();
        entry.setAuthor(author);
        entry.setTitle(title);
        return new EntryAddedEvent(
                "dummy source",
                DataFixture.getChristmasDayIn2000(),
                entry,
                new DatabaseBlog());
    }

    public static PingbackAddedEvent createPingbackAddedEvent(String title, String sourceUri) {
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Pingback pingback = new DatabasePingback();
        pingback.setSourceURI(sourceUri);
        pingback.setEntry(entry);
        return new PingbackAddedEvent(
                "dummy source",
                DataFixture.getChristmasDayIn2000(),
                pingback,
                new DatabaseBlog());
    }

    public static TrackbackAddedEvent createTrackbackAddedEvent(String title, String url) {
        Entry entry = new DatabaseEntry();
        entry.setTitle(title);
        Trackback trackback = new DatabaseTrackback();
        trackback.setUrl(url);
        trackback.setEntry(entry);
        return new TrackbackAddedEvent(
                "dummy source",
                DataFixture.getChristmasDayIn2000(),
                trackback,
                new DatabaseBlog());
    }
}
