package com.mbledug.blojsom.plugin.blogtimes;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
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


    HttpSession createMockHttpSessionSetAttribute() {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession.expects(once()).method("setAttribute");
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpServletRequest createMockHttpServletRequestSetSessionAttribute() {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest.expects(once()).method("getSession").will(returnValue(createMockHttpSessionSetAttribute()));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
