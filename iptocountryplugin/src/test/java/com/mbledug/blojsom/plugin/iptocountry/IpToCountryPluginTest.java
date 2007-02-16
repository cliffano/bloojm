package com.mbledug.blojsom.plugin.iptocountry;

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
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;

public class IpToCountryPluginTest extends TestCase {

    DataFixture mDataFixture;

    public void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessAddingCountryCodeToComments() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode("111.111.111.111")};
        String expectedCountryCode = DataFixture.EXPECTED_COUNTRY_CODE;

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountryCode));
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            List comments = entries[0].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                Map metaData = comment.getMetaData();
                String countryCode = String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
                assertNotNull(countryCode);
                assertEquals(expectedCountryCode, countryCode);
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessDoesntAddCountryCodeToCommentsWhenIpAddressIsIgnored() {

        String ipAddress = "111.111.111.111";
        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode(ipAddress)};
        String expectedCountryCode = DataFixture.EXPECTED_COUNTRY_CODE;

        try {
            ipToCountryPlugin.setIgnoredIpAddresses(ipAddress + ",222.222.222.222");
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountryCode));
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            List comments = entries[0].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                Map metaData = comment.getMetaData();
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessDoesntAddCountryCodeToCommentWhenCommentIpIsNull() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode(null)};
        String expectedCountryCode = DataFixture.EXPECTED_COUNTRY_CODE;

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountryCode));
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            List comments = entries[0].getComments();
            for (Iterator it = comments.iterator(); it.hasNext();) {
                Comment comment = (Comment) it.next();
                Map metaData = comment.getMetaData();
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessCommentResponseSubmissionEventAddsCountryCodeToMetaData() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        String expectedCountryCode = DataFixture.EXPECTED_COUNTRY_CODE;
        ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountryCode));
        CommentResponseSubmissionEvent event = DataFixture.createCommentResponseSubmissionEventWithCommentHavingNoCountryCode();
        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
        String countryCode = String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
        assertEquals(expectedCountryCode, countryCode);
    }

    public void testProcessResponseSubmissionEventDoesntAddCountryCodeToMetaData() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE));
    }

    public void testHandleEventLeftEventAsIs() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        ipToCountryPlugin.handleEvent(event);
    }
}
