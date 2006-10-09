package com.mbledug.blojsom.plugin.param2ctx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;

/**
 * Param2CtxPlugin puts request parameters into context map for convenient
 * retrieval from Velocity templates.
 * @author Cliffano Subagio
 */
public class Param2CtxPlugin implements Plugin {

    /**
     * Logger for Param2CtxPlugin.
     */
    private static final Log LOG = LogFactory.getLog(Param2CtxPlugin.class);

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising Param2CtxPlugin.");
    }

    /**
     * Puts request parameters into context map.
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries with Gravatar ID added to the comments
     * @throws PluginException when there's an error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {
        Param2CtxHelper.putParamsToContext(httpServletRequest, context);
        return entries;
    }

    /**
     * cleanup method has an empty implementation in Param2CtxPlugin.
     * @throws PluginException when there's an error performing cleanup of
     * this plugin
     */
    public final void cleanup() throws PluginException {
    }

    /**
     * Writes plugin destroy message.
     * @throws PluginException when there's an error in finalising this plugin
     */
    public final void destroy() throws PluginException {
        LOG.info("Destroying Param2CtxPlugin.");
    }
}
