package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.easymock.classextension.EasyMock;

public class UrlTextFetcherTest extends TestCase {

    private HttpClient httpClient;
    private HttpMethod httpMethod;
    private UrlTextFetcher mFetcher;

    protected void setUp() {
        httpClient = (HttpClient) EasyMock.createStrictMock(HttpClient.class);
        httpMethod = (HttpMethod) EasyMock.createStrictMock(HttpMethod.class);
        mFetcher = new UrlTextFetcher(httpClient, httpMethod);
    }

    public void testFetchTextSuccessWithValidUrl() throws Exception {
        httpMethod.setURI(new URI("http://thisisavalidurl", true));
        httpMethod.setFollowRedirects(true);
        EasyMock.expect(new Integer(httpClient.executeMethod(httpMethod))).andReturn(new Integer(1));
        EasyMock.expect(httpMethod.getResponseBodyAsString()).andReturn("some response");
        httpMethod.releaseConnection();

        EasyMock.replay(new Object[]{httpClient, httpMethod});
        try {
            String text = mFetcher.fetchText("http://thisisavalidurl");
            assertNotNull(text);
        } catch (IOException ioe) {
            fail("Valid URL should not give an exception: " + ioe);
        }
        EasyMock.verify(new Object[]{httpClient, httpMethod});
    }

    public void testFetchTextFailureWithInvalidUrl() {


        try {
            httpMethod.setURI(new URI("(*&", true));
            httpMethod.setFollowRedirects(true);
            EasyMock.expect(new Integer(httpClient.executeMethod(httpMethod))).andThrow(new URIException("dummy exception"));

            EasyMock.replay(new Object[]{httpClient, httpMethod});
            String text = mFetcher.fetchText("(*&");
            fail("Invalid URL should've thrown an exception instead of " +
                    "returning text: " + text);
        } catch (URIException urie) {
            // exception is thrown as expected
        } catch (IOException ioe) {
            fail("URIException should've been thrown instead of exception: " + ioe);
        }
        EasyMock.verify(new Object[]{httpClient, httpMethod});
    }

    public void testFetchSuccessWithProxy() throws Exception {
        HostConfiguration hostConfiguration = (HostConfiguration) EasyMock.createStrictMock(HostConfiguration.class);
        HttpState httpState = (HttpState) EasyMock.createStrictMock(HttpState.class);

        EasyMock.expect(httpClient.getHostConfiguration()).andReturn(hostConfiguration);
        hostConfiguration.setProxy("http://@#&$(@$&@$&#@&", 8080);
        EasyMock.expect(httpClient.getState()).andReturn(httpState);
        httpState.setCredentials((AuthScope) EasyMock.isA(AuthScope.class), (UsernamePasswordCredentials) EasyMock.isA(UsernamePasswordCredentials.class));
        httpMethod.setURI(new URI("http://thisisavalidurl", true));
        httpMethod.setFollowRedirects(true);
        EasyMock.expect(new Integer(httpClient.executeMethod(httpMethod))).andReturn(new Integer(1));
        EasyMock.expect(httpMethod.getResponseBodyAsString()).andReturn("some response");
        httpMethod.releaseConnection();

        EasyMock.replay(new Object[]{httpClient, httpMethod, hostConfiguration});
        try {
            mFetcher.setProxy(
                    "http://@#&$(@$&@$&#@&",
                    8080,
                    "Some Username",
                    "Some Password");
            mFetcher.fetchText("http://thisisavalidurl");
        } catch (IOException ioe) {
            fail("Valid URL should not give an exception: " + ioe);
        }
        EasyMock.verify(new Object[]{httpClient, httpMethod, hostConfiguration});
    }

    public void testExecuteFailureInvalidProxy() {
        try {
            mFetcher.setProxy("http://@#&$(@$&@$&#@&", 8080);
            mFetcher.fetchText("http://thisisavalidurl");
            fail("Test with invalid proxy should have failed at this point.");
        } catch (Exception e) {
            // exception is thrown as expected
        }
    }

    public void testExecuteFailureInvalidAuthenticatedProxy() {
        try {
            mFetcher.setProxy(
                    "http://@#&$(@$&@$&#@&",
                    8080,
                    "Some Username",
                    "Some Password");
            mFetcher.fetchText("http://thisisavalidurl");
            fail("Test with invalid authenticated proxy should have failed at this point.");
        } catch (Exception e) {
            // exception is thrown as expected
        }
    }
}