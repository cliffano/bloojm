package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URIException;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static final String[] KEYWORDS = {"foo", "bar"};
    static final String TEXT_ALL_KEYWORDS = "This has foo bar";
    static final String TEXT_ONE_KEYWORD = "This has foo";
    static final String TEXT_NO_KEYWORD = "This has nothing";

    static final String INVALID_URL = "@#!#*";
    static final String VALID_URL = "http://thisisavalidurl";
    static final String DUMMY_PROXY_HOST = "http://@#&$(@$&@$&#@&";
    static final int DUMMY_PROXY_PORT = 8080;
    static final String DUMMY_PROXY_USERNAME = "Some Username";
    static final String DUMMY_PROXY_PASSWORD = "Some Password";

    final HttpClient createMockHttpClient(final boolean withProxy, final Exception executeMethodException) {
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

    final HttpMethod createMockHttpMethod(final String url) {
        Mock mockHttpMethod = mock(HttpMethod.class);
        if (VALID_URL.equals(url)) {
            mockHttpMethod.expects(once()).method("getResponseBodyAsString").will(returnValue(TEXT_ALL_KEYWORDS));
            mockHttpMethod.expects(once()).method("setURI");
        } else {
            mockHttpMethod.expects(once()).method("getResponseBodyAsString").will(throwException(new IOException("Some Dummy Error")));
            mockHttpMethod.expects(once()).method("setURI").will(throwException(new URIException()));
        }
        mockHttpMethod.expects(once()).method("getURI");
        mockHttpMethod.expects(once()).method("releaseConnection");
        mockHttpMethod.expects(once()).method("setFollowRedirects");
        return (HttpMethod) mockHttpMethod.proxy();
    }
}
