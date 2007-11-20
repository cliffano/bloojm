package com.mbledug.blojsom.plugin.blogtimes;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.Fetcher;
import org.blojsom.fetcher.FetcherException;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.easymock.classextension.EasyMock;

public class BlogTimesPluginTest extends TestCase {

    public void testProcessStoresBarGraphImageCreatorAndDatesAsSessionAttribute() throws Exception {
        Date[] dates = createDates(10);
        Entry[] entries = createEntryWithDates(dates);
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(new HashMap());

        Fetcher fetcher = (Fetcher) EasyMock.createStrictMock(Fetcher.class);
        EasyMock.expect(fetcher.loadEntries(blog, BlogTimesHelper.getNumOfLatestEntries(blog), 1)).andReturn(entries);
        Plugin blogTimesPlugin = new BlogTimesPlugin(fetcher);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);

        EasyMock.expect(request.getSession()).andReturn(session);
        session.setAttribute((String) EasyMock.isA(String.class), EasyMock.isA(BarGraphImageCreator.class));
        EasyMock.expect(request.getSession()).andReturn(session);
        session.setAttribute((String) EasyMock.isA(String.class), EasyMock.aryEq(dates));

        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response, fetcher});

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    request,
                    response,
                    blog,
                    new HashMap(),
                    entries);
            blogTimesPlugin.cleanup();
            blogTimesPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response, fetcher});
    }

    public void testProcessWithValidBlogProperties() throws Exception {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BORDER_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIMELINE_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIME_INTERVAL_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_FONT_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_HEIGHT, "20");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_WIDTH, "10");

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);
        Date[] dates = createDates(10);
        Entry[] entries = createEntryWithDates(dates);

        Fetcher fetcher = (Fetcher) EasyMock.createStrictMock(Fetcher.class);
        EasyMock.expect(fetcher.loadEntries(blog, BlogTimesHelper.getNumOfLatestEntries(blog), 1)).andReturn(entries);
        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin(fetcher);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);

        EasyMock.expect(request.getSession()).andReturn(session);
        session.setAttribute((String) EasyMock.isA(String.class), EasyMock.isA(BarGraphImageCreator.class));
        EasyMock.expect(request.getSession()).andReturn(session);
        session.setAttribute((String) EasyMock.isA(String.class), EasyMock.aryEq(dates));

        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response, fetcher});

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    request,
                    response,
                    blog,
                    new HashMap(),
                    entries);
            blogTimesPlugin.cleanup();
            blogTimesPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response, fetcher});
    }

    public void testProcessWithInvalidPropertiesGivesPluginException() throws Exception {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_BACKGROUND_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BORDER_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_BACKGROUND_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_TIMELINE_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_TIME_INTERVAL_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_FONT_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_HEIGHT, "-1");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_WIDTH, "-1");

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);
        Date[] dates = createDates(10);
        Entry[] entries = createEntryWithDates(dates);

        Fetcher fetcher = (Fetcher) EasyMock.createStrictMock(Fetcher.class);
        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin(fetcher);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);

        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response, fetcher});

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    request,
                    response,
                    blog,
                    new HashMap(),
                    entries);
            fail("PluginException should've occured.");
        } catch (PluginException pe) {
            // expected PluginException
        }

        EasyMock.verify(new Object[]{session, request, response, fetcher});
    }

    public void testProcessWithFetcherErrorGivesPluginException() throws Exception {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BORDER_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIMELINE_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIME_INTERVAL_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_FONT_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_HEIGHT, "20");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_WIDTH, "10");

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);
        Date[] dates = createDates(10);
        Entry[] entries = createEntryWithDates(dates);

        Fetcher fetcher = (Fetcher) EasyMock.createStrictMock(Fetcher.class);
        EasyMock.expect(fetcher.loadEntries(blog, BlogTimesHelper.getNumOfLatestEntries(blog), 1)).andThrow(new FetcherException("dummy error"));
        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin(fetcher);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);

        EasyMock.expect(request.getSession()).andReturn(session);
        session.setAttribute((String) EasyMock.isA(String.class), EasyMock.isA(BarGraphImageCreator.class));

        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response, fetcher});

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    request,
                    response,
                    blog,
                    new HashMap(),
                    entries);
            fail("PluginException should've occured.");
        } catch (PluginException pe) {
            // expected PluginException
        }

        EasyMock.verify(new Object[]{session, request, response, fetcher});
    }

    Date[] createDates(int numberOfDates) {
        Calendar cal = Calendar.getInstance();
        Date[] dates = new Date[numberOfDates];
        for (int i = 0; i < dates.length; i++) {
            cal.set(2007, 8, i);
            dates[i] = cal.getTime();
        }
        return dates;
    }

    Entry[] createEntryWithDates(Date[] dates) {
        Entry[] entries = new Entry[dates.length];
        for (int i = 0; i < dates.length; i++) {
            entries[i] = new DatabaseEntry();
            entries[i].setDate(dates[i]);
        }
        return entries;
    }
}
