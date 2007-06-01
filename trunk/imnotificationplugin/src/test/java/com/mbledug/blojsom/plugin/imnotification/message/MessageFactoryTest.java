package com.mbledug.blojsom.plugin.imnotification.message;

import junit.framework.TestCase;

import com.mbledug.blojsom.plugin.imnotification.DataFixture;

public class MessageFactoryTest extends TestCase {

    public void testGetMessageWithUnsupportedEventThrowsIllegalArgumentException() {
        try {
            new MessageFactory().getMessage(DataFixture.createUnsupportedEvent());
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetMessageWithEntryAddedEvent() {
        assertNotNull(new MessageFactory().getMessage(DataFixture.createEntryAddedEvent("dummy author", "dummy title")));
    }

    public void testGetMessageWithCommentAddedEvent() {
        assertNotNull(new MessageFactory().getMessage(DataFixture.createCommentAddedEvent("dummy author", "dummy title")));
    }

    public void testGetMessageWithTrackbackAddedEvent() {
        assertNotNull(new MessageFactory().getMessage(DataFixture.createTrackbackAddedEvent("dummy title", "dummy url")));
    }

    public void testGetMessageWithPingbackAddedEvent() {
        assertNotNull(new MessageFactory().getMessage(DataFixture.createPingbackAddedEvent("dummy title", "dummy sourceUri")));
    }
}
