package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URIException;
import org.blojsom.blog.Blog;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static final String[] KEYWORDS = {"foo", "bar"};
    static final String CSV_MORE_THAN_ONE_KEYWORDS = "foo,bar";
    static final String CSV_ONE_KEYWORD = "foo";
    static final String TEXT_ALL_KEYWORDS = "This has foo bar";
    static final String TEXT_ONE_KEYWORD = "This has foo";
    static final String TEXT_NO_KEYWORD = "This has nothing";

    static final String INVALID_URL = "@#!#*";
    static final String VALID_URL = "http://thisisavalidurl";
    static final String DUMMY_PROXY_HOST = "http://@#&$(@$&@$&#@&";
    static final int DUMMY_PROXY_PORT = 8080;
    static final String DUMMY_PROXY_USERNAME = "Some Username";
    static final String DUMMY_PROXY_PASSWORD = "Some Password";

    HttpClient createMockHttpClient(boolean withProxy, Exception executeMethodException) {
        Mock mockHttpClient = mock(HttpClient.class);
        if (executeMethodException == null) {
            mockHttpClient.expects(once()).method("executeMethod").will(returnValue(1));
        } else {
            mockHttpClient.expects(once()).method("executeMethod").will(throwException(executeMethodException));
        }
        if (withProxy) {
            mockHttpClient.expects(once()).method("getHostConfiguration").will(returnValue(new HostConfiguration()));
            mockHttpClient.expects(once()).method("getState").will(returnValue(new HttpState()));
        }
        return (HttpClient) mockHttpClient.proxy();
    }

    HttpMethod createMockHttpMethod(String url, String responseBody) {
        Mock mockHttpMethod = mock(HttpMethod.class);
        if (VALID_URL.equals(url)) {
            mockHttpMethod.expects(once()).method("getResponseBodyAsString").will(returnValue(responseBody));
            mockHttpMethod.expects(once()).method("setURI");
        } else {
            mockHttpMethod.expects(once()).method("getResponseBodyAsString").will(throwException(new IOException("Some Dummy Error")));
            mockHttpMethod.expects(once()).method("setURI").will(throwException(new URIException()));
        }
        mockHttpMethod.expects(once()).method("releaseConnection");
        mockHttpMethod.expects(once()).method("setFollowRedirects");
        return (HttpMethod) mockHttpMethod.proxy();
    }

    UrlTextFetcher createMockUrlTextFetcher(String text) {
        Mock mockUrlTextFetcher = mock(
                UrlTextFetcher.class,
                new Class[]{HttpClient.class, HttpMethod.class},
                new Object[]{createMockHttpClient(false, null), createMockHttpMethod(VALID_URL, text)});
        mockUrlTextFetcher.expects(once()).method("fetchText").will(returnValue(text));
        return (UrlTextFetcher) mockUrlTextFetcher.proxy();
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEvent() {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(new HashMap());
        TrackbackResponseSubmissionEvent event = new TrackbackResponseSubmissionEvent(
                DataFixture.class,
                new Date(),
                blog,
                createMockHttpServletRequest(),
                createMockHttpServletResponse(),
                "Some blog name",
                "Some title",
                VALID_URL,
                "Some excerpt",
                new DatabaseEntry(),
                new HashMap());
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPlugin() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginBehindProxy() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, DUMMY_PROXY_HOST);
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(DUMMY_PROXY_PORT));
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginBehindAuthenticatedProxy() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_HOST, DUMMY_PROXY_HOST);
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PORT, String.valueOf(DUMMY_PROXY_PORT));
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_USERNAME, DUMMY_PROXY_USERNAME);
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_PROXY_PASSWORD, DUMMY_PROXY_PASSWORD);
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithDisabledPlugin() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "false");
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginAndTextHasAllKeywordsForDeletion() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, CSV_MORE_THAN_ONE_KEYWORDS);
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ACTION, TrackbackKeywordPlugin.ACTION_DELETE);
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_CHECK_TYPE, TrackbackKeywordPlugin.CHECK_TYPE_ALL);
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginAndTextHasOneKeywordForModeration() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_KEYWORDS, CSV_ONE_KEYWORD);
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginAndToBeDeletedMetaData() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map metaData = event.getMetaData();
        metaData.put(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY, Boolean.TRUE);
        return event;
    }

    TrackbackResponseSubmissionEvent createTrackbackResponseSubmissionEventWithEnabledPluginAndToBeModeratedMetaData() {
        TrackbackResponseSubmissionEvent event = createTrackbackResponseSubmissionEvent();
        Blog blog = event.getBlog();
        blog.setProperty(TrackbackKeywordPlugin.PROPERTY_ENABLED, "true");
        Map metaData = event.getMetaData();
        metaData.put(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED, Boolean.FALSE);
        return event;
    }

    ResponseSubmissionEvent createResponseSubmissionEvent() {
        return (ResponseSubmissionEvent) createTrackbackResponseSubmissionEvent();
    }
}
