/**
 * Copyright (c) 2005-2007, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Qoqoa nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.galleryr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.util.BlojsomUtils;

/**
 * {@link GalleryrPlugin} retrieves photos/photosets stored in Flickr.
 * @author Cliffano Subagio
 */
public class GalleryrPlugin implements Plugin {

    /**
     * Logger for {@link GalleryrPlugin}.
     */
    private static final Log LOG = LogFactory.getLog(GalleryrPlugin.class);

    /**
     * Plugin property for Flickr API key.
     */
    public static final String PROPERTY_API_KEY = "flickr-apikey";

    /**
     * Meta data key for photosets ID.
     */
    public static final String METADATA_PHOTOSET_IDS = "galleryr-photosets-id";

    /**
     * Meta data key for photos ID.
     */
    public static final String METADATA_PHOTO_IDS = "galleryr-photos-id";

    /**
     * Meta data for Flickr photos.
     */
    public static final String METADATA_PHOTOS = "galleryr-photos";

    /**
     * Writes plugin init message.
     * @throws PluginException when there's an error in initialising this plugin
     */
    public final void init() throws PluginException {
        LOG.info("Initialising GalleryrPlugin.");
    }

    /**
     * Checks for the existence of photo and photoset IDs in entry meta data,
     * retrieves Flickr photos for the corresponding IDs and set the photos
     * as an entry meta data.
     * @param httpServletRequest http servlet request
     * @param httpServletResponse http servlet response
     * @param blog blog instance
     * @param context context
     * @param entries blog entries retrieved for the particular request
     * @return entries with Flickr photos added to entry meta data
     * @throws PluginException when Flickr API key is invalid or when there's an
     * error processing the blog entries
     */
    public final Entry[] process(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final Blog blog,
            final Map context,
            final Entry[] entries)
            throws PluginException {

        if (BlojsomUtils.checkNullOrBlank(blog.getProperty(PROPERTY_API_KEY))) {
            throw new PluginException("Invalid Flickr API key: "
                    + blog.getProperty(PROPERTY_API_KEY)
                    + ", for blog id: " + blog.getBlogId());
        }

        FlickrFacade flickrFacade = new FlickrFacade(
                blog.getProperty(PROPERTY_API_KEY));

        for (int i = 0; i < entries.length; i++) {
            Map entryMetaData = entries[i].getMetaData();
            List photos = new ArrayList();
            if (entryMetaData.get(METADATA_PHOTOSET_IDS) != null) {
                photos.addAll(flickrFacade.getPhotosFromPhotosets(
                        String.valueOf(
                                entryMetaData.get(METADATA_PHOTOSET_IDS))));
            }
            if (entryMetaData.get(METADATA_PHOTO_IDS) != null) {
                photos.addAll(flickrFacade.getPhotos(
                        String.valueOf(
                                entryMetaData.get(METADATA_PHOTO_IDS))));
            }
            if (!photos.isEmpty()) {
                entryMetaData.put(METADATA_PHOTOS, photos);
            }
        }

        return entries;
    }

    /**
     * cleanup method has an empty implementation in {@link GalleryrPlugin}.
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
        LOG.info("Destroying GalleryrPlugin.");
    }
}
