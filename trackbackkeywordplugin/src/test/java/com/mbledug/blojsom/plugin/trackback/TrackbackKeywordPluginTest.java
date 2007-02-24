package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.event.Listener;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;

public class TrackbackKeywordPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessAddsListenerToEventBroadcaster() {
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        trackbackKeywordPlugin.setEventBroadcaster(mDataFixture.createSimpleEventBroadcaster());
        try {
            trackbackKeywordPlugin.init();
            trackbackKeywordPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    new Entry[0]);
            trackbackKeywordPlugin.cleanup();
            trackbackKeywordPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }
    }

    public void testProcessEventTrackbackNotSpamSuspect() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_ALL_KEYWORDS, false, null);
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackWithUnknownHostException() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_ALL_KEYWORDS, false, new UnknownHostException());
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNotNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackWithIoException() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_ALL_KEYWORDS, false, new IOException());
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackNotSpamSuspectBehindProxy() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_ALL_KEYWORDS, true, null);
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        Properties properties = new Properties();
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, DataFixture.DUMMY_PROXY_HOST);
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(DataFixture.DUMMY_PROXY_PORT));
        trackbackKeywordPlugin.setProperties(properties);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackNotSpamSuspectBehindAuthenticatedProxy() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_ALL_KEYWORDS, true, null);
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        Properties properties = new Properties();
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, DataFixture.DUMMY_PROXY_HOST);
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(DataFixture.DUMMY_PROXY_PORT));
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_USERNAME, DataFixture.DUMMY_PROXY_USERNAME);
        properties.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PASSWORD, DataFixture.DUMMY_PROXY_PASSWORD);
        trackbackKeywordPlugin.setProperties(properties);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackSpamSuspectToBeDeleted() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_NO_KEYWORD, false, null);
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map properties = event.getBlog().getProperties();
        properties.put(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, DataFixture.CSV_MORE_THAN_ONE_KEYWORDS);
        properties.put(TrackbackKeywordPlugin.PROPERTY_ACTION, TrackbackKeywordPlugin.ACTION_DELETE);
        properties.put(TrackbackKeywordPlugin.PROPERTY_CHECK_TYPE, TrackbackKeywordPlugin.CHECK_TYPE_ALL);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNotNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventTrackbackSpamSuspectToBeModerated() {
        UrlTextFetcher urlTextFetcher = mDataFixture.createMockUrlTextFetcher(DataFixture.TEXT_NO_KEYWORD, false, null);
        TrackbackKeywordPlugin trackbackKeywordPlugin = new TrackbackKeywordPlugin(urlTextFetcher);
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPlugin();
        Map properties = event.getBlog().getProperties();
        properties.put(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, DataFixture.CSV_ONE_KEYWORD);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNotNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventWithDisabledPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithDisabledPlugin();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testProcessEventWithTrackbackToBeDeletedMarkedByPriorPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPluginAndToBeDeletedMetaData();
        Map metaData = event.getMetaData();
        String deleteMetaDataBeforeProcessing = metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY).toString();
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        String deleteMetaDataAfterProcessing = metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY).toString();
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        assertEquals(deleteMetaDataAfterProcessing, deleteMetaDataBeforeProcessing);
    }

    public void testProcessEventWithTrackbackToBeModeratedMarkedByPriorPlugin() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        TrackbackResponseSubmissionEvent event = mDataFixture.createTrackbackResponseSubmissionEventWithEnabledPluginAndToBeModeratedMetaData();
        Map metaData = event.getMetaData();
        String deleteMetaDataBeforeProcessing = metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED).toString();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        trackbackKeywordPlugin.processEvent(event);
        String deleteMetaDataAfterProcessing = metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED).toString();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertEquals(deleteMetaDataAfterProcessing, deleteMetaDataBeforeProcessing);
    }

    public void testResponseSubmissionEventLeftAsIs() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        ResponseSubmissionEvent event = mDataFixture.createResponseSubmissionEvent();
        Map metaData = event.getMetaData();
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
        trackbackKeywordPlugin.processEvent(event);
        assertNull(metaData.get(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY));
        assertNull(metaData.get(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED));
    }

    public void testHandleEventLeftAsIs() {
        Listener trackbackKeywordPlugin = new TrackbackKeywordPlugin();
        ResponseSubmissionEvent event = mDataFixture.createResponseSubmissionEvent();
        trackbackKeywordPlugin.handleEvent(event);
    }
}
