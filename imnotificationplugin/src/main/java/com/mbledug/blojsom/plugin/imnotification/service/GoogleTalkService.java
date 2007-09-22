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
 *   * Neither the name of Qoqoa nor the names of its contributors
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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.GoogleTalkConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * {@link GoogleTalkService} sends notification message via
 * <a href="http://en.wikipedia.org/wiki/Google_talk">GoogleTalk</a>.
 * @author Cliffano
 */
public class GoogleTalkService implements IMService {

    /**
     * Logger for {@link GoogleTalkService}.
     */
    private static final Log LOG = LogFactory.getLog(GoogleTalkService.class);

    /**
     * GoogleTalk connection.
     */
    private GoogleTalkConnection mConnection;

    /**
     * Account username.
     */
    private String mUsername;

    /**
     * Account password.
     */
    private String mPassword;

    /**
     * Creates an instance of {@link GoogleTalkService}. If username
     * is null, then an {@link IllegalArgumentException} will be thrown.
     * @param username the account username
     * @param password the account password
     */
    public GoogleTalkService(
            final String username,
            final String password) {

        if (username == null) {
            throw new IllegalArgumentException("Please configure "
                    + "username of GoogleTalk Service bean declaration in "
                    + "blojsom-plugins.xml");
        }

        mUsername = username;
        mPassword = password;
    }

    /**
     * {@inheritDoc}
     */
    public final void send(final List recipients, final String text) {

        try {
            for (Iterator it = recipients.iterator(); it.hasNext();) {
                String recipient = (String) it.next();

                openConnection();
                mConnection.login(mUsername, mPassword);
                mConnection.createChat(recipient).sendMessage(text);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Message with text: " + text + ", has been sent "
                            + "to recipient: " + recipient);
                }
            }
        } catch (XMPPException xmppe) {
            LOG.error("Unable to send GoogleTalk message to recipients: "
                    + recipients, xmppe);
        }
    }

    /**
     * Opens the GoogleTalk connection after making sure that any previously
     * opened connection is closed.
     * @throws XMPPException when there's a problem while openning the
     * connection
     */
    private void openConnection() throws XMPPException {

        if (mConnection != null && mConnection.isConnected()) {
            mConnection.close();
        }
        mConnection = new GoogleTalkConnection();
    }
}
