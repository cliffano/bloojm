package com.mbledug.blojsom.plugin.trackback;

import junit.framework.TestCase;

public class KeywordCheckerTest extends TestCase {

    private KeywordChecker mChecker;

    protected void setUp() {
        mChecker = new KeywordChecker(DataFixture.KEYWORDS);
    }

    public void testHasAtLeastOneKeywordSuccessWithOneKeyword() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword(DataFixture.TEXT_ONE_KEYWORD);
        assertTrue(hasAtLeastOneKeyword);
    }

    public void testHasAtLeastOneKeywordSuccessWithMoreThanOneKeyword() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword(DataFixture.TEXT_ALL_KEYWORDS);
        assertTrue(hasAtLeastOneKeyword);
    }

    public void testHasAtLeastOneKeywordFailure() {
        boolean hasAtLeastOneKeyword = mChecker
                .hasAtLeastOneKeyword(DataFixture.TEXT_NO_KEYWORD);
        assertFalse(hasAtLeastOneKeyword);
    }

    public void testHasAllKeywordsSuccess() {
        boolean hasAllKeywords = mChecker.hasAllKeywords(DataFixture.TEXT_ALL_KEYWORDS);
        assertTrue(hasAllKeywords);
    }

    public void testHasAllKeywordsFailure() {
        boolean hasAllKeywords = mChecker.hasAllKeywords(DataFixture.TEXT_NO_KEYWORD);
        assertFalse(hasAllKeywords);
    }
}