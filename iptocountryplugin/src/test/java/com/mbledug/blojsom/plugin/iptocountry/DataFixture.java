package com.mbledug.blojsom.plugin.iptocountry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    static final String EXPECTED_COUNTRY_CODE = "AU";

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

    JdbcTemplate createMockJdbcTemplate(String ipAddress) {
        Mock mockJdbcTemplate = mock(JdbcTemplate.class);
        mockJdbcTemplate
                .expects(once())
                .method("queryForObject")
                .will(returnValue(ipAddress));
        return (JdbcTemplate) mockJdbcTemplate.proxy();
    }

    JdbcTemplate createMockJdbcTemplate(Throwable throwable) {
        Mock mockJdbcTemplate = mock(JdbcTemplate.class);
        mockJdbcTemplate
                .expects(once())
                .method("queryForObject")
                .will(throwException(throwable));
        return (JdbcTemplate) mockJdbcTemplate.proxy();
    }

    IpToCountryDao createMockIpToCountryDao(String countryCode) {
        Mock mockIpToCountryDao = mock(IpToCountryDao.class);
        mockIpToCountryDao
                .expects(once())
                .method("getCountryCode")
                .will(returnValue(countryCode));
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
