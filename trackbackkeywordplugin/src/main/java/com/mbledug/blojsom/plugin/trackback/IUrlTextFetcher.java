package com.mbledug.blojsom.plugin.trackback;

import java.io.IOException;

/**
 * {@link IUrlTextFetcher} fetches URL content.
 * @author Cliffano Subagio
 */
interface IUrlTextFetcher {
    /**
     * Sets proxy details to HttpClient instance.
     * @param proxyHost proxy host name
     * @param proxyPort proxy port number
     */
    void setProxy(final String proxyHost, final int proxyPort);

    /**
     * Sets authenticated proxy details to HttpClient instance.
     * @param proxyHost proxy host name
     * @param proxyPort proxy port number
     * @param proxyUsername proxy username
     * @param proxyPassword proxy password
     */
    void setProxy(
            final String proxyHost,
            final int proxyPort,
            final String proxyUsername,
            final String proxyPassword);

    /**
     * Creates a connection to a url and fetches its text content.
     * @param url the url to connect to
     * @return the text content
     * @throws IOException when there's a problem with retrieving the text
     * content
     */
    String fetchText(final String url) throws IOException;
}
