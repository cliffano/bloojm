/**
 * Copyright (c) 2005-2006, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Mbledug nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * UrlTextFetcher manages the connection to a url and fetches its text content.
 * @author Cliffano Subagio
 */
class UrlTextFetcher {

    /**
     * The client used to send request and receive response.
     */
    private HttpClient mHttpClient;

    /**
     * Method used to make the http connection.
     */
    private HttpMethod mHttpMethod;

    /**
     * Creates a ConnectionManager instance. Initialises HttpClient and
     * HttpMethod.
     */
    UrlTextFetcher() {
        mHttpClient = new HttpClient();
        mHttpMethod = new GetMethod();
    }

    /**
     * Create a UrlTextFetcher instance with specified HttpClient and
     * HttpMethod.
     * @param httpClient the http client
     * @param httpMethod the http method
     */
    UrlTextFetcher(final HttpClient httpClient, final HttpMethod httpMethod) {
        mHttpClient = httpClient;
        mHttpMethod = httpMethod;
    }

    /**
     * Sets proxy details to HttpClient instance.
     * @param proxyHost proxy host name
     * @param proxyPort proxy port number
     */
    final void setProxy(final String proxyHost, final int proxyPort) {
        mHttpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
    }

    /**
     * Sets authenticated proxy details to HttpClient instance.
     * @param proxyHost proxy host name
     * @param proxyPort proxy port number
     * @param proxyUsername proxy username
     * @param proxyPassword proxy password
     */
    final void setProxy(
            final String proxyHost,
            final int proxyPort,
            final String proxyUsername,
            final String proxyPassword) {
        setProxy(proxyHost, proxyPort);
        mHttpClient.getState().setProxyCredentials(
                new AuthScope(proxyHost, proxyPort, null),
                new UsernamePasswordCredentials(
                        proxyUsername,
                        proxyPassword));
    }

    /**
     * Creates a connection to a url and fetches its text content.
     * @param url the url to connect to
     * @return the text content
     * @throws IOException when there's a problem with retrieving the text
     * content
     */
    final String fetchText(final String url) throws IOException {

        mHttpMethod.setURI(new URI(url, true));
        mHttpMethod.setFollowRedirects(true);

        mHttpClient.executeMethod(mHttpMethod);
        String text = mHttpMethod.getResponseBodyAsString();
        mHttpMethod.releaseConnection();

        return text;
    }
}
