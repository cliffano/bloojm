package com.mbledug.blojsom.plugin.param2ctx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

public class Param2CtxHelperTest extends TestCase {

    public void testPutParamsToContextSuccessWithEmptyContext() {

        Map context = new HashMap();

        Vector paramNames = new Vector();
        paramNames.add("paramName1");
        paramNames.add("paramName2");

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameterNames()).andReturn(paramNames.elements());
        EasyMock.expect(request.getParameter("paramName1")).andReturn("paramValue1");
        EasyMock.expect(request.getParameter("paramName2")).andReturn("paramValue2");

        // this test assumes an empty context map
        assertEquals(0, context.size());

        EasyMock.replay(new Object[]{request});

        Param2CtxHelper.putParamsToContext(request, context);

        EasyMock.verify(new Object[]{request});

        // verifies params from request are added to context
        assertEquals(2, context.size());

        // verifies the context names start with the default PREFIX
        Iterator it = context.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String entryName = String.valueOf(entry.getKey());
            assertTrue(entryName.startsWith(Param2CtxHelper.PREFIX));
        }
    }

    public void testPutParamsToContextSuccessWithNonEmptyContext() {

        Map context = new HashMap();
        context.put("dummyKey1", "dummyValue1");
        context.put("dummyKey2", "dummyValue2");
        int startContextSize = context.size();

        Vector paramNames = new Vector();
        paramNames.add("paramName1");
        paramNames.add("paramName2");

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameterNames()).andReturn(paramNames.elements());
        EasyMock.expect(request.getParameter("paramName1")).andReturn("paramValue1");
        EasyMock.expect(request.getParameter("paramName2")).andReturn("paramValue2");

        EasyMock.replay(new Object[]{request});

        Param2CtxHelper.putParamsToContext(request, context);

        EasyMock.verify(new Object[]{request});

        // verifies params from request are added to context
        assertEquals(4, context.size());

        int totalPrefixedKey = 0;

        // verifies the context names start with the default PREFIX
        Iterator it = context.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String entryName = String.valueOf(entry.getKey());
            if (entryName.startsWith(Param2CtxHelper.PREFIX)) {
                totalPrefixedKey++;
            }
        }
        assertEquals(totalPrefixedKey, (context.size() - startContextSize));
    }

    public void testPutParamsToContextWithNullRequestGivesIllegalArgumentException() {

        try {
            Param2CtxHelper.putParamsToContext(null, new HashMap());
            fail("Null context should've already thrown IllegalArgumentException.");
        } catch(IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }

    public void testPutParamsToContextWithNullContextGivesIllegalArgumentException() {

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);

        try {
            EasyMock.replay(new Object[]{request});
            Param2CtxHelper.putParamsToContext(request, null);
            EasyMock.verify(new Object[]{request});
            fail("Null context should've already thrown IllegalArgumentException.");
        } catch(IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }
}