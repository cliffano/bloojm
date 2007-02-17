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

    public void testProcessAddingCountryToComments() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode("111.111.111.111")};
        Country expectedCountry = DataFixture.EXPECTED_COUNTRY;

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountry));
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
                assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
                assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
                assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
                assertEquals(expectedCountry.getTwoCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR)));
                assertEquals(expectedCountry.getThreeCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR)));
                assertEquals(expectedCountry.getName(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME)));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessDoesntAddCountryToCommentsWhenIpAddressIsIgnored() {

        String ipAddress = "111.111.111.111";
        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode(ipAddress)};
        Country expectedCountry = DataFixture.EXPECTED_COUNTRY;

        try {
            ipToCountryPlugin.setIgnoredIpAddresses(ipAddress + ",222.222.222.222");
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountry));
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
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessDoesntAddCountryToCommentWhenCommentIpIsNull() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {DataFixture.createEntryWithSingleCommentWithoutCountryCode(null)};
        Country expectedCountry = DataFixture.EXPECTED_COUNTRY;

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountry));
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
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
                assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessCommentResponseSubmissionEventAddsCountryToMetaData() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Country expectedCountry = DataFixture.EXPECTED_COUNTRY;
        ipToCountryPlugin.setIpToCountryDao(mDataFixture.createMockIpToCountryDao(expectedCountry));
        CommentResponseSubmissionEvent event = DataFixture.createCommentResponseSubmissionEventWithCommentHavingNoCountryCode();
        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
        assertEquals(expectedCountry.getTwoCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR)));
        assertEquals(expectedCountry.getThreeCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR)));
        assertEquals(expectedCountry.getName(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME)));
    }

    public void testProcessResponseSubmissionEventDoesntAddCountryToMetaData() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
    }

    public void testHandleEventLeftEventAsIs() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = DataFixture.createResponseSubmissionEvent();
        ipToCountryPlugin.handleEvent(event);
    }
}
