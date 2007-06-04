package com.mbledug.blojsom.plugin.gatekeeper.provider;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ContextBlojsomQAProviderTest extends TestCase {

    public void testConstructorWithNullQuestionAnswerListThrowsIllegalArgumentException() {
        try {
            new ContextBlojsomQAProvider(null);
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetQuestionAnswerListGivesTheListPassedFromConstructor() {
        List list = new ArrayList();
        ContextBlojsomQAProvider provider = new ContextBlojsomQAProvider(list);
        assertEquals(list, provider.getQuestionAnswerList());
    }
}
