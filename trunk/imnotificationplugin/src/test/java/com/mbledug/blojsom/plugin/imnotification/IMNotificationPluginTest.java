package com.mbledug.blojsom.plugin.imnotification;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.event.Event;
import org.blojsom.event.Listener;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentAddedEvent;

public class IMNotificationPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testHandleEventSendsMessageUsingService() {
        String serviceId = "dummy";
        Map services = mDataFixture.createServiceMap(serviceId);
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(services);
        Blog blog = new DatabaseBlog();
        Map properties = new HashMap();
        properties.put(IMNotificationPlugin.PROPERTY_RECIPIENTS_PREFIX + serviceId, "whatever@email.com");
        blog.setProperties(properties);
        CommentAddedEvent event = DataFixture.createCommentAddedEvent("dummy author", "dummy title", blog);
        iMNotificationPlugin.handleEvent(event);
    }

    public void testHandleEventWithNoServicesDoesntDoAnything() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        iMNotificationPlugin.handleEvent(DataFixture.createCommentAddedEvent("dummy author", "dummy title"));
    }

    public void testHandleEventWithUnsupportedEventDoesNothingToServices() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        iMNotificationPlugin.handleEvent(DataFixture.createUnsupportedEvent());
    }

    public void testProcessAddsListenerToEventBroadcaster() {
        IMNotificationPlugin iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        iMNotificationPlugin.setEventBroadcaster(mDataFixture.createSimpleEventBroadcaster());
        try {
            iMNotificationPlugin.init();
            iMNotificationPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    new Entry[0]);
            iMNotificationPlugin.cleanup();
            iMNotificationPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }
    }

    public void testProcessEventLeftAsIs() {
        Listener iMNotificationPlugin = new IMNotificationPlugin(new HashMap());
        Event event = new Event(null, null);
        iMNotificationPlugin.processEvent(event);
    }
}
