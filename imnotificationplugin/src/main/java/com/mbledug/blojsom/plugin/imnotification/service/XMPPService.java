/**
 * Copyright (c) 2007, Cliffano Subagio
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
 *   * Neither the name of Studio Cliffano nor the names of its contributors
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
package com.mbledug.blojsom.plugin.imnotification.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.MultipleRecipientManager;

/**
 * {@link XMPPService} sends notification message via
 * <a href="http://en.wikipedia.org/wiki/XMPP">XMPP</a>.
 * @author Cliffano
 */
public class XMPPService implements IMService {

    /**
     * Logger for {@link XMPPService}.
     */
    private static final Log LOG = LogFactory.getLog(XMPPService.class);

    /**
     * Message thread title.
     */
    private static final String MESSAGE_THREAD = "Blojsom";

    /**
     * XMPP connection.
     */
    private XMPPConnection mConnection;

    /**
     * Server host.
     */
    private String mHost;

    /**
     * Server port.
     */
    private Integer mPort;

    /**
     * Account username.
     */
    private String mUsername;

    /**
     * Account password.
     */
    private String mPassword;

    /**
     * Creates an instance of {@link XMPPService}. If either host or username
     * is null, then an {@link IllegalArgumentException} will be thrown.
     * @param host the server host
     * @param port the server port
     * @param username the account username
     * @param password the account password
     */
    public XMPPService(
            final String host,
            final Integer port,
            final String username,
            final String password) {

        if (host == null || username == null) {
            throw new IllegalArgumentException("Please configure host and "
                    + "username of XMPP Service bean declaration in "
                    + "blojsom-plugins.xml");
        }

        mHost = host;
        mPort = port;
        mUsername = username;
        mPassword = password;
    }

    /**
     * {@inheritDoc}
     */
    public final void send(final List recipients, final String text) {

        Message message = new Message();
        message.setType(Message.Type.CHAT);
        message.setBody(text);
        message.setThread(MESSAGE_THREAD);

        try {
            openConnection();
            mConnection.login(mUsername, mPassword);
            MultipleRecipientManager.send(
                    mConnection, message, recipients, null, null);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Message with text: " + text + ", has been sent "
                        + "to recipients: " + recipients);
            }
        } catch (XMPPException xmppe) {
            LOG.error("Unable to send XMPP message to recipients: "
                    + recipients, xmppe);
        }
    }

    /**
     * Opens the XMPP connection after making sure that any previously opened
     * connection is closed.
     * @throws XMPPException when there's a problem while openning the
     * connection
     */
    private void openConnection() throws XMPPException {

        if (mConnection != null && mConnection.isConnected()) {
            mConnection.close();
        }

        if (mPort == null) {
            mConnection = new XMPPConnection(mHost);
        } else {
            mConnection = new XMPPConnection(mHost, mPort.intValue());
        }
    }
}
