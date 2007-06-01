package com.mbledug.blojsom.plugin.iptocountry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataFixture extends MockObjectTestCase {

    static final String EMAIL = "foo@bar.com";
    static final Country EXPECTED_COUNTRY = new Country("GB", "GBR", "UNITED KINGDOM");

    static Entry createEntryWithSingleCommentWithoutCountryCode(String ipAddress) {

        List comments = new ArrayList();
        comments.add(createCommentWithoutCountryCode(ipAddress));
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        return entry;
    }

    static DatabaseComment createCommentWithoutCountryCode(String ipAddress) {

        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail(EMAIL);
        comment.setMetaData(new HashMap());
        comment.setIp(ipAddress);
        return comment;
    }

    static CommentResponseSubmissionEvent createCommentResponseSubmissionEventWithCommentHavingNoCountryCode() {

        return new CommentResponseSubmissionEvent(
                new Object(),
                new Date(),
                new DatabaseBlog(),
                new DataFixture().createMockHttpServletRequest("111.111.111.111"),
                new DataFixture().createMockHttpServletResponse(),
                "Dummy Submitter",
                EMAIL,
                "http://dummyurl.org",
                "Dummy Comment Description",
                new DatabaseEntry(),
                new HashMap());
    }

    static ResponseSubmissionEvent createResponseSubmissionEvent() {

        return new ResponseSubmissionEvent(
                new Object(),
                new Date(),
                new DatabaseBlog(),
                new DataFixture().createMockHttpServletRequest(),
                new DataFixture().createMockHttpServletResponse(),
                "Dummy Submitter",
                EMAIL,
                "http://dummyurl.org",
                "Dummy Comment Description",
                new DatabaseEntry(),
                new HashMap());
    }

    JdbcTemplate createMockJdbcTemplate(Country country) {
        Map result = new HashMap();
        result.put(IpToCountryDao.COLUMN_COUNTRY_CODE2, country.getTwoCharCode());
        result.put(IpToCountryDao.COLUMN_COUNTRY_CODE3, country.getThreeCharCode());
        result.put(IpToCountryDao.COLUMN_COUNTRY_NAME, country.getName());

        Mock mockJdbcTemplate = mock(JdbcTemplate.class);
        mockJdbcTemplate
                .expects(once())
                .method("queryForMap")
                .will(returnValue(result));
        return (JdbcTemplate) mockJdbcTemplate.proxy();
    }

    JdbcTemplate createMockJdbcTemplate(Throwable throwable) {
        Mock mockJdbcTemplate = mock(JdbcTemplate.class);
        mockJdbcTemplate
                .expects(once())
                .method("queryForMap")
                .will(throwException(throwable));
        return (JdbcTemplate) mockJdbcTemplate.proxy();
    }

    IpToCountryDao createMockIpToCountryDao(Country country) {
        Mock mockIpToCountryDao = mock(IpToCountryDao.class);
        mockIpToCountryDao
                .expects(once())
                .method("getCountry")
                .will(returnValue(country));
        return (IpToCountryDao) mockIpToCountryDao.proxy();
    }

    HttpServletRequest createMockHttpServletRequest(String ipAddress) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getRemoteAddr")
                .will(returnValue(ipAddress));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
