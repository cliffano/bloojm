package com.mbledug.blojsom.plugin.pager;

import junit.framework.TestCase;

public class PagerHelperTest extends TestCase {

    public void testGetCurrentPageSuccess() {
        assertTrue(PagerHelper.getCurrentPage("5") > 0);
    }

    public void testGetCurrentPageWithNullPageNumParamValueSetsDefaultPageNum() {
        assertTrue(PagerHelper.DEFAULT_PAGE_NUM == PagerHelper.getCurrentPage(null));
    }

    public void testGetCurrentPageWithNonNumericPageNumParamValueThrowsIllegalArgumentException() {
        try{
            PagerHelper.getCurrentPage("abc");
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }

    public void testGetCurrentPageWithNegativePageNumParamValueThrowsIllegalArgumentException() {
        try{
            PagerHelper.getCurrentPage("-1");
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }

    public void testGetTotalPagesSuccess() {
        assertEquals(10, PagerHelper.getTotalPages(50, 5));
    }

    public void testGetTotalPagesWithBoundaryValues() {
        assertEquals(11, PagerHelper.getTotalPages(52, 5));
    }

    public void testGetTotalPagesWithZeroTotalEntriesGivesZeroTotalPages() {
        assertEquals(0, PagerHelper.getTotalPages(0, 5));
    }

    public void testGetTotalPagesWithNegativeTotalEntriesThrowsIllegalArgumentException() {
        try{
            PagerHelper.getTotalPages(-1, 5);
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }

    public void testGetTotalPagesWithZeroEntriesPerPageThrowsIllegalArgumentException() {
        try{
            PagerHelper.getTotalPages(50, 0);
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }
}