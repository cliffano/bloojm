package com.mbledug.blojsom.plugin.param2ctx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

public class Param2CtxHelperTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testPutParamsToContextSuccessWithEmptyContext() {

        Map context = new HashMap();

        // this test assumes an empty context map
        assertEquals(context.size(), 0);
        HttpServletRequest request = mDataFixture.createMockHttpServletRequestWithParams();
        Param2CtxHelper.putParamsToContext(request, context);

        // verifies params from request are added to context
        assertEquals(request.getParameterMap().size(), context.size());

        // verifies the context names start with the default PREFIX
        Iterator it = context.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String entryName = String.valueOf(entry.getKey());
            assertTrue(entryName.startsWith(Param2CtxHelper.PREFIX));
        }
    }

    public void testPutParamsToContextSuccessWithNonEmptyContext() {

        Map context = mDataFixture.createContextWithParams();
        int startContextSize = context.size();

        HttpServletRequest request = mDataFixture.createMockHttpServletRequestWithParams();
        Param2CtxHelper.putParamsToContext(request, context);

        // verifies params from request are added to context
        assertEquals(request.getParameterMap().size(), context.size() - startContextSize);

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
        try {
            Param2CtxHelper.putParamsToContext(mDataFixture.createMockHttpServletRequestWithParams(), null);
            fail("Null context should've already thrown IllegalArgumentException.");
        } catch(IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        }
    }
}