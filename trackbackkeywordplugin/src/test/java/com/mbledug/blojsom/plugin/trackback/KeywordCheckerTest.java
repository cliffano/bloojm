package com.mbledug.blojsom.plugin.trackback;

import junit.framework.TestCase;

public class KeywordCheckerTest extends TestCase {

    private KeywordChecker mChecker;

    protected void setUp() {
        mChecker = new KeywordChecker(new String[]{"foo", "bar"});
    }

    public void testHasAtLeastOneKeywordSuccessWithOneKeyword() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword("This has foo");
        assertTrue(hasAtLeastOneKeyword);
    }

    public void testHasAtLeastOneKeywordSuccessWithMoreThanOneKeyword() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword("This has foo bar");
        assertTrue(hasAtLeastOneKeyword);
    }

    public void testHasAtLeastOneKeywordFailure() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword("This has nothing");
        assertFalse(hasAtLeastOneKeyword);
    }

    public void testHasAllKeywordsSuccess() {
        boolean hasAllKeywords = mChecker.hasAllKeywords("This has foo bar");
        assertTrue(hasAllKeywords);
    }

    public void testHasAllKeywordsFailure() {
        boolean hasAllKeywords = mChecker.hasAllKeywords("This has nothing");
        assertFalse(hasAllKeywords);
    }
}