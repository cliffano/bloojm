package com.mbledug.blojsom.plugin.pager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.BlogEntry;
import org.blojsom.blog.BlogUser;
import org.blojsom.blog.BlojsomConfigurationException;
import org.blojsom.blog.FileBackedBlogEntry;
import org.blojsom.plugin.BlojsomPluginException;
import org.easymock.classextension.EasyMock;

public class PagerPluginTest extends TestCase {

    public void testProcessSetsContextEntries() throws BlojsomPluginException {

        int pageSize = 5;
        Map context = new HashMap();

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter("page-num")).andReturn(null);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{request, response});

        PagerPlugin pagerPlugin = new PagerPlugin();
        pagerPlugin.process(
                request,
                response,
                createBlogUserWithDisplayEntries(pageSize),
                context,
                createBlogEntries(52));
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_PAGE_SIZE))) == pageSize);
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_TOTAL_PAGES))) == 11);
        assertTrue(Integer.parseInt(String.valueOf(context.get(PagerPlugin.CONTEXT_ENTRY_CURRENT_PAGE))) == PagerHelper.DEFAULT_PAGE_NUM);

        pagerPlugin.cleanup();
        pagerPlugin.destroy();

        EasyMock.verify(new Object[]{request, response});
    }

    private BlogEntry[] createBlogEntries(int totalEntries) {
        BlogEntry[] blogEntries = new BlogEntry[totalEntries];
        for (int i = 0; i < blogEntries.length; i++) {
            blogEntries[i] = new FileBackedBlogEntry();
        }
        return blogEntries;
    }

    private BlogUser createBlogUserWithDisplayEntries(int displayEntries) {
        Blog blog = null;
        try {
            Properties properties = new Properties();
            properties.put("blog-home", "someDummyBlogHome");
            properties.put("blog-url", "someDummyBlogUrl");
            properties.put("blog-base-url", "someDummyBlogBaseUrl");
            blog = new Blog(properties);
        } catch (BlojsomConfigurationException bce) {

        }
        blog.setBlogDisplayEntries(displayEntries);
        BlogUser blogUser = new BlogUser();
        blogUser.setBlog(blog);
        return blogUser;
    }
}