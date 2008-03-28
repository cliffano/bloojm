package com.mbledug.blojsom.plugin.imnotification;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Comment;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.event.Event;
import org.blojsom.event.Listener;
import org.blojsom.event.SimpleEventBroadcaster;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.easymock.classextension.EasyMock;

import com.mbledug.blojsom.plugin.imnotification.service.IMService;

public class IMNotificationPluginTest extends TestCase {

    public void testHandleEventSendsMessageUsingService() {
        String serviceId = "dummy";
        IMService imService = (IMService) EasyMock.createStrictMock(IMService.class);
        List recipients = new ArrayList();
        recipients.add("whatever@email.com");
        imService.send(recipients, "BLOJSOM - Mon Dec 25 00:00:00 EST 2000 - New comment was added by dummy author to entry 'dummy title'");
        Map services = new HashMap();
        services.put(serviceId, imService);
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(services);
        Blog blog = new DatabaseBlog();
        Map properties = new HashMap();
        properties.put(IMNotificationPlugin.PROPERTY_RECIPIENTS_PREFIX + serviceId, "whatever@email.com");
        blog.setProperties(properties);
        Entry entry = new DatabaseEntry();
        entry.setTitle("dummy title");
        Comment comment = new DatabaseComment();
        comment.setAuthor("dummy author");
        comment.setEntry(entry);
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        CommentAddedEvent event = new CommentAddedEvent(
                "dummy source",
                calendar.getTime(),
                comment,
                blog);
        EasyMock.replay(new Object[]{imService});
        iMNotificationPlugin.handleEvent(event);
        EasyMock.verify(new Object[]{imService});
    }

    public void testHandleEventWithNoServicesDoesntDoAnything() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        Entry entry = new DatabaseEntry();
        entry.setTitle("dummy title");
        Comment comment = new DatabaseComment();
        comment.setAuthor("dummy author");
        comment.setEntry(entry);
        GregorianCalendar calendar = new GregorianCalendar(2000, 11, 25);
        CommentAddedEvent commentAddedEvent = new CommentAddedEvent(
                "dummy source",
                calendar.getTime(),
                comment,
                new DatabaseBlog());
        iMNotificationPlugin.handleEvent(commentAddedEvent);
    }

    public void testHandleEventWithUnsupportedEventDoesNothingToServices() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        iMNotificationPlugin.handleEvent(new Event(null, null));
    }

    public void testProcessAddsListenerToEventBroadcaster() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        SimpleEventBroadcaster simpleEventBroadcaster = (SimpleEventBroadcaster) EasyMock.createStrictMock(SimpleEventBroadcaster.class);
        simpleEventBroadcaster.addListener((Listener) EasyMock.isA(Listener.class));
        iMNotificationPlugin.setEventBroadcaster(simpleEventBroadcaster);

        EasyMock.replay(new Object[]{simpleEventBroadcaster, request, response});
        try {
            iMNotificationPlugin.init();
            iMNotificationPlugin.process(
                    request,
                    response,
                    new DatabaseBlog(),
                    new HashMap(),
                    new Entry[0]);
            iMNotificationPlugin.cleanup();
            iMNotificationPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }
        EasyMock.verify(new Object[]{simpleEventBroadcaster, request, response});
    }

    public void testProcessEventLeftAsIs() {
        Listener iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        Event event = new Event(null, null);
        iMNotificationPlugin.processEvent(event);
    }
}
