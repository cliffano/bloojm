package com.mbledug.blojsom.plugin.param2ctx;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.easymock.classextension.EasyMock;

public class Param2CtxPluginTest extends TestCase {

    public void testProcessPuttingParamsToContext() {

        Map context = new HashMap();
        context.put("dummyKey1", "dummyValue1");
        context.put("dummyKey2", "dummyValue2");
        int startContextSize = context.size();

        Plugin param2CtxPlugin = new Param2CtxPlugin();
        Entry[] entries = new Entry[0];


        Vector paramNames = new Vector();
        paramNames.add("paramName1");
        paramNames.add("paramName2");

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameterNames()).andReturn(paramNames.elements());
        EasyMock.expect(request.getParameter("paramName1")).andReturn("paramValue1");
        EasyMock.expect(request.getParameter("paramName2")).andReturn("paramValue2");

        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);


        try {
            param2CtxPlugin.init();

            assertEquals(2, context.size());

            EasyMock.replay(new Object[]{request, response});
            entries = param2CtxPlugin.process(
                    request,
                    response,
                    new DatabaseBlog(),
                    context,
                    entries);
            EasyMock.verify(new Object[]{request, response});

            assertEquals(4, context.size());

            param2CtxPlugin.cleanup();
            param2CtxPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }
}
