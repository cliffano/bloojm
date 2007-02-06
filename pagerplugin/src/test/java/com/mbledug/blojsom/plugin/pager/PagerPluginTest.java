package com.mbledug.blojsom.plugin.pager;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.plugin.BlojsomPluginException;

public class PagerPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessSetsContextEntries() throws BlojsomPluginException {

        int pageSize = 5;
        Map context = new HashMap();

        PagerPlugin pagerPlugin = new PagerPlugin();
        pagerPlugin.process(
                mDataFixture.createMockHttpServletRequest(null),
                mDataFixture.createMockHttpServletResponse(),
                DataFixture.createBlogUserWithDisplayEntries(pageSize),
                context,
                DataFixture.createBlogEntries(52));
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_PAGE_SIZE))) == pageSize);
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_TOTAL_PAGES))) == 11);
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_CURRENT_PAGE))) == PagerHelper.DEFAULT_PAGE_NUM);

        pagerPlugin.cleanup();
        pagerPlugin.destroy();
    }
}