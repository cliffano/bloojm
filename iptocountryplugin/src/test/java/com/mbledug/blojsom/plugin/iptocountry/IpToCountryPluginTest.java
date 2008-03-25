package com.mbledug.blojsom.plugin.iptocountry;

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

public class IpToCountryPluginTest extends TestCase {

	static final Country EXPECTED_COUNTRY = new Country("GB", "GBR", "UNITED KINGDOM");

    public void testProcessAddingCountryToComments() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {createEntryWithSingleCommentWithoutCountryCode("111.111.111.111")};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);
        IpToCountryDao ipToCountryDao = (IpToCountryDao) EasyMock.createStrictMock(IpToCountryDao.class);
        EasyMock.expect(ipToCountryDao.getCountry(1869573999)).andReturn(EXPECTED_COUNTRY);

        EasyMock.replay(new Object[]{ipToCountryDao, request, response});

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(ipToCountryDao);
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    request,
                    response,
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
                assertEquals(EXPECTED_COUNTRY.getTwoCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR)));
                assertEquals(EXPECTED_COUNTRY.getThreeCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR)));
                assertEquals(EXPECTED_COUNTRY.getName(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME)));
            }
            ipToCountryPlugin.cleanup();
            ipToCountryPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        EasyMock.verify(new Object[]{ipToCountryDao, request, response});
    }

    public void testProcessDoesntAddCountryToCommentsWhenIpAddressIsIgnored() {

        String ipAddress = "111.111.111.111";
        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {createEntryWithSingleCommentWithoutCountryCode(ipAddress)};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);
        IpToCountryDao ipToCountryDao = (IpToCountryDao) EasyMock.createStrictMock(IpToCountryDao.class);

        EasyMock.replay(new Object[]{ipToCountryDao, request, response});

        try {
            ipToCountryPlugin.setIgnoredIpAddresses(ipAddress + ",222.222.222.222");
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(ipToCountryDao);
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    request,
                    response,
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

        EasyMock.verify(new Object[]{ipToCountryDao, request, response});
    }

    public void testProcessDoesntAddCountryToCommentWhenCommentIpIsNull() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();
        Entry[] entries = new Entry[] {createEntryWithSingleCommentWithoutCountryCode(null)};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);
        IpToCountryDao ipToCountryDao = (IpToCountryDao) EasyMock.createStrictMock(IpToCountryDao.class);

        EasyMock.replay(new Object[]{ipToCountryDao, request, response});

        try {
            ipToCountryPlugin.setEventBroadcaster(new SimpleEventBroadcaster());
            ipToCountryPlugin.setIpToCountryDao(ipToCountryDao);
            ipToCountryPlugin.init();
            entries = ipToCountryPlugin.process(
                    request,
                    response,
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

        EasyMock.verify(new Object[]{ipToCountryDao, request, response});
    }

    public void testProcessCommentResponseSubmissionEventAddsCountryToMetaData() {

        IpToCountryPlugin ipToCountryPlugin = new IpToCountryPlugin();

        IpToCountryDao ipToCountryDao = (IpToCountryDao) EasyMock.createStrictMock(IpToCountryDao.class);
        ipToCountryPlugin.setIpToCountryDao(ipToCountryDao);
        EasyMock.expect(ipToCountryDao.getCountry(1869573999)).andReturn(EXPECTED_COUNTRY);

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getRemoteAddr()).andReturn("111.111.111.111");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        CommentResponseSubmissionEvent event = new CommentResponseSubmissionEvent(
                new Object(),
                new Date(),
                new DatabaseBlog(),
                request,
                response,
                "Dummy Submitter",
                "foo@bar.com",
                "http://dummyurl.org",
                "Dummy Comment Description",
                new DatabaseEntry(),
                new HashMap());

        EasyMock.replay(new Object[]{ipToCountryDao, request, response});

        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
        assertNotNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
        assertEquals(EXPECTED_COUNTRY.getTwoCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR)));
        assertEquals(EXPECTED_COUNTRY.getThreeCharCode(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR)));
        assertEquals(EXPECTED_COUNTRY.getName(), String.valueOf(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME)));

        EasyMock.verify(new Object[]{ipToCountryDao, request, response});
    }

    public void testProcessResponseSubmissionEventDoesntAddCountryToMetaData() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        ipToCountryPlugin.processEvent(event);
        Map metaData = event.getMetaData();
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_2CHAR));
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_CODE_3CHAR));
        assertNull(metaData.get(IpToCountryPlugin.METADATA_COUNTRY_NAME));
    }

    public void testHandleEventLeftEventAsIs() {

        Listener ipToCountryPlugin = new IpToCountryPlugin();
        ResponseSubmissionEvent event = createResponseSubmissionEvent();
        ipToCountryPlugin.handleEvent(event);
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

    private Entry createEntryWithSingleCommentWithoutCountryCode(String ipAddress) {

        List comments = new ArrayList();
        comments.add(createCommentWithoutCountryCode(ipAddress));
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        return entry;
    }

    private DatabaseComment createCommentWithoutCountryCode(String ipAddress) {

        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail("foo@bar.com");
        comment.setMetaData(new HashMap());
        comment.setIp(ipAddress);
        return comment;
    }
}
