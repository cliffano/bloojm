package com.mbledug.blojsom.plugin.param2ctx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;

public class Param2CtxPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessPuttingParamsToContext() {

        Plugin param2CtxPlugin = new Param2CtxPlugin();
        Entry[] entries = new Entry[0];
        Map context = mDataFixture.createContextWithParams();
        HttpServletRequest request = mDataFixture.createMockHttpServletRequestWithParams();

        int initialContextSize = context.size();
        int expectedContextSizeWithParams = initialContextSize + request.getParameterMap().size();

        try {
            param2CtxPlugin.init();
            entries = param2CtxPlugin.process(
                    request,
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    context,
                    entries);
            assertEquals(expectedContextSizeWithParams, context.size());
            assertTrue(expectedContextSizeWithParams > initialContextSize);

            param2CtxPlugin.cleanup();
            param2CtxPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }
}
