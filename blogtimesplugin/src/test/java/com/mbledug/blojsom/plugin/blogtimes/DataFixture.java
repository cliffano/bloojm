package com.mbledug.blojsom.plugin.blogtimes;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.Fetcher;
import org.blojsom.fetcher.FetcherException;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static Date createRandomDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Random().nextLong());
        return cal.getTime();
    }

    static Date[] createRandomDates(int numberOfDates) {
        Date[] dates = new Date[numberOfDates];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = createRandomDate();
        }
        return dates;
    }

    static Entry[] createEntryWithDates(Date[] dates) {
        Entry[] entries = new Entry[dates.length];
        for (int i = 0; i < dates.length; i++) {
            entries[i] = new DatabaseEntry();
            entries[i].setDate(dates[i]);
        }
        return entries;
    }

    static Map createPropertiesWithValidValues() {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BORDER_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_BACKGROUND_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIMELINE_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_TIME_INTERVAL_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_FONT_COLOR, "ff00ff");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_HEIGHT, "20");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_WIDTH, "10");
        return properties;
    }

    static Map createPropertiesWithInvalidValues() {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_BACKGROUND_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BORDER_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_BACKGROUND_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_TIMELINE_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_TIME_INTERVAL_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_FONT_COLOR, "ff00ffaa");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_HEIGHT, "-1");
        properties.put(BlogTimesPlugin.PROPERTY_BAR_WIDTH, "-1");
        return properties;
    }

    Fetcher createMockFetcher() {
        Mock mockFetcher = mock(Fetcher.class);
        mockFetcher.expects(once()).method("loadEntries").will(returnValue(createEntryWithDates(createRandomDates(100))));
        return (Fetcher) mockFetcher.proxy();
    }

    Fetcher createMockFetcherWithException() {
        Mock mockFetcher = mock(Fetcher.class);
        mockFetcher.expects(once()).method("loadEntries").will(throwException(new FetcherException("")));
        return (Fetcher) mockFetcher.proxy();
    }

    HttpSession createMockHttpSessionSetAttribute() {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession.expects(atLeastOnce()).method("setAttribute");
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpServletRequest createMockHttpServletRequestSetSessionAttribute() {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest.expects(atLeastOnce()).method("getSession").will(returnValue(createMockHttpSessionSetAttribute()));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
