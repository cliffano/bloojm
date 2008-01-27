package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.event.EventBroadcaster;
import org.blojsom.event.Listener;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;
import org.easymock.EasyMock;

public class TrackbackKeywordPluginTest extends TestCase {

    public void testProcessAddsListenerToEventBroadcaster() {
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        EventBroadcaster eventBroadcaster = (EventBroadcaster) EasyMock.createStrictMock(EventBroadcaster.class);
        eventBroadcaster.addListener(trackbackKeywordPlugin);
        trackbackKeywordPlugin.setEventBroadcaster(eventBroadcaster);
        try {
            trackbackKeywordPlugin.init();
            trackbackKeywordPlugin.process(
                    (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                    (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                    new DatabaseBlog(),
                    new HashMap(),
                    new Entry[0]);
            trackbackKeywordPlugin.cleanup();
            trackbackKeywordPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }
    }

    public void testProcessEventTrackbackNotSpamSuspect() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andReturn("This has foo bar");

        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");

        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackWithIoExceptionAddsTrackbackAction() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andThrow(new IOException());
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");

        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNotNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackNotSpamSuspectBehindProxy() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        urlTextFetcher.setProxy("http://@#&$(@$&@$&#@&", 8080);
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andReturn("This has foo bar");

        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);

        Properties properties = new Properties();
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, "http://@#&$(@$&@$&#@&");
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(8080));
        trackbackKeywordPlugin.setProperties(properties);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");

        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackNotSpamSuspectBehindAuthenticatedProxy() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        urlTextFetcher.setProxy("http://@#&$(@$&@$&#@&", 8080, "Some Username", "Some Password");
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andReturn("This has foo bar");

        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        Properties properties = new Properties();
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, "http://@#&$(@$&@$&#@&");
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(8080));
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_USERNAME, "Some Username");
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PASSWORD, "Some Password");
        trackbackKeywordPlugin.setProperties(properties);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackSpamSuspectToBeDeleted() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andReturn("This has nothing");

        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map properties = event.getBlog().getProperties();
        properties.put(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, "foo,bar");
        properties.put(TrackbackKeywordPlugin.PROPERTY_ACTION, TrackbackKeywordPlugin.ACTION_DELETE);
        properties.put(TrackbackKeywordPlugin.PROPERTY_CHECK_TYPE, TrackbackKeywordPlugin.CHECK_TYPE_ALL);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNotNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackSpamSuspectToBeModerated() throws Exception {
        IUrlTextFetcher urlTextFetcher = (IUrlTextFetcher) EasyMock.createStrictMock(IUrlTextFetcher.class);
        EasyMock.expect(urlTextFetcher.fetchText("http://thisisavalidurl")).andReturn("This has nothing");

        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);

        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map properties = event.getBlog().getProperties();
        properties.put(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, "foo");
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));

        EasyMock.replay(new Object[]{urlTextFetcher});
        trackbackKeywordPlugin.processEvent(event);
        EasyMock.verify(new Object[]{urlTextFetcher});

        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNotNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventWithDisabledPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "false");
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventWithTrackbackToBeDeletedMarkedByPriorPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map metaData = event.getMetaData();
        metaData.put(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY, Boolean.TRUE);
        String deleteMetaDataBeforeProcessing = metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY).toString();
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        String deleteMetaDataAfterProcessing = metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY).toString();
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        assertEquals(deleteMetaDataAfterProcessing, deleteMetaDataBeforeProcessing);
    }

    public void testProcessEventWithTrackbackToBeModeratedMarkedByPriorPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map metaData = event.getMetaData();
        metaData.put(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED, Boolean.FALSE);
        String deleteMetaDataBeforeProcessing = metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED).toString();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        trackbackKeywordPlugin.processEvent(event);
        String deleteMetaDataAfterProcessing = metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED).toString();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertEquals(deleteMetaDataAfterProcessing, deleteMetaDataBeforeProcessing);
    }

    public void testResponseSubmissionEventLeftAsIs() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testHandleEventLeftAsIs() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        trackbackKeywordPlugin.handleEvent(event);
    }

    ResponseSubmissionEvent createResponseSubmissionEvent() {
        return (ResponseSubmissionEvent) createTrackbackResponseSubmissionEvent();
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEvent() {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(new HashMap());
        TrackbackResponseSubmissionEvent event = new TrackbackResponseSubmissionEvent(
                this,
                new Date(),
                blog,
                (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                "Some blog name",
                "Some title",
                "http://thisisavalidurl",
                "Some excerpt",
                new DatabaseEntry(),
                new HashMap());
        return event;
    }
}
