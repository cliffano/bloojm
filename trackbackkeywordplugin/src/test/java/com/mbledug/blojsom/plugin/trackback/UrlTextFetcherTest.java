package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;

public class UrlTextFetcherTest extends TestCase {

    private UrlTextFetcher mFetcher;
    private DataFixture mDataFixture;
    private HttpClient mMockHttpClient;
    private HttpMethod mMockHttpMethod;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testFetchTextSuccessWithValidUrl() {
        mMockHttpClient = mDataFixture.createMockHttpClient(false, null);
        mMockHttpMethod = mDataFixture.createMockHttpMethod(DataFixture.VALID_URL);
        mFetcher = new UrlTextFetcher(mMockHttpClient, mMockHttpMethod);
        try {
            String text = mFetcher.fetchText(DataFixture.VALID_URL);
            assertNotNull(text);
        } catch (IOException ioe) {
            fail("Valid URL should not give an exception: " + ioe);
        }
    }

    public void testFetchTextFailureWithInvalidUrl() {
        mMockHttpClient = mDataFixture.createMockHttpClient(false, new URIException());
        mMockHttpMethod = mDataFixture.createMockHttpMethod(DataFixture.INVALID_URL);
        mFetcher = new UrlTextFetcher(mMockHttpClient, mMockHttpMethod);
        try {
            String text = mFetcher.fetchText(DataFixture.INVALID_URL);
            fail("Invalid URL should've thrown an exception instead of " +
                    "returning text: " + text);
        } catch (URIException urie) {
            // exception is thrown as expected
        } catch (IOException ioe) {
            fail("URIException should've been thrown instead of exception: " + ioe);
        }
    }

    public void testFetchSuccessWithProxy() {
        mMockHttpClient = mDataFixture.createMockHttpClient(true, null);
        mMockHttpMethod = mDataFixture.createMockHttpMethod(DataFixture.VALID_URL);
        mFetcher = new UrlTextFetcher(mMockHttpClient, mMockHttpMethod);
        try {
          mFetcher.setProxy(
                  DataFixture.DUMMY_PROXY_HOST,
                  DataFixture.DUMMY_PROXY_PORT,
                  DataFixture.DUMMY_PROXY_USERNAME,
                  DataFixture.DUMMY_PROXY_PASSWORD);
            mFetcher.fetchText(DataFixture.VALID_URL);
        } catch (IOException ioe) {
            fail("Valid URL should not give an exception: " + ioe);
        }
    }

//    public void testExecuteFailureInvalidProxy() {
//        try {
//            mFetcher.setProxy(DUMMY_PROXY_HOST, DUMMY_PROXY_PORT);
//            mFetcher.fetchText(VALID_URL);
//            fail("Test with invalid proxy should have failed at this point.");
//        } catch (Exception e) {
//            // exception is thrown as expected
//        }
//    }
//
//    public void testExecuteFailureInvalidAuthenticatedProxy() {
//        try {
//            mFetcher.setProxy(
//                    DUMMY_PROXY_HOST,
//                    DUMMY_PROXY_PORT,
//                    DUMMY_PROXY_USERNAME,
//                    DUMMY_PROXY_PASSWORD);
//            mFetcher.fetchText(VALID_URL);
//            fail("Test with invalid authenticated proxy should have failed at this point.");
//        } catch (Exception e) {
//            // exception is thrown as expected
//        }
//    }
}