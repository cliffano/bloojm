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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.Size;

/**
 * {@link GalleryrPhoto} is a wrapper to com.aetrion.flickr.photos.Photo and
 * provides convenient methods to retrieve ID, description, and URLs in various
 * sizes.
 * @author Cliffano
 */
public class GalleryrPhoto {

    /**
     * com.aetrion.flickr.photos.Photo object retrieved via Flickrj.
     */
    private Photo mFlickrPhoto;

    /**
     * A map of URLs with the size label as the key, and the size URL as the
     * value.
     */
    private Map mUrls;

    /**
     * Creates an instance of {@link GalleryrPhoto} with specified Flickr photo
     * and the sizes associated to the photo.
     * @param flickrPhoto the Flickr photo
     * @param sizes the photo sizes information
     */
    public GalleryrPhoto(final Photo flickrPhoto, final Collection sizes) {
        mFlickrPhoto = flickrPhoto;

        mUrls = new HashMap();
        for (Iterator it = sizes.iterator(); it.hasNext();) {
            Size size = (Size) it.next();
            mUrls.put(size.getLabel(), size.getSource());
        }
    }

    /**
     * Gets Flickr photo.
     * @return the Flickr photo
     */
    public final Photo getFlickrPhoto() {
        return mFlickrPhoto;
    }

    /**
     * Gets photo ID.
     * @return the photo ID
     */
    public final String getId() {
        return mFlickrPhoto.getId();
    }

    /**
     * Gets photo description.
     * @return the photo description
     */
    public final String getDescription() {
        return mFlickrPhoto.getDescription();
    }

    /**
     * Gets URL for Large image size.
     * @return the URL for Large image size
     */
    public final String getLargeUrl() {
        return getUrlString(mUrls.get("Large"));
    }

    /**
     * Gets URL for Medium image size.
     * @return the URL for Medium image size
     */
    public final String getMediumUrl() {
        return getUrlString(mUrls.get("Medium"));
    }

    /**
     * Gets URL for Original image size.
     * @return the URL for Original image size
     */
    public final String getOriginalUrl() {
        return getUrlString(mUrls.get("Original"));
    }

    /**
     * Gets URL for Small image size.
     * @return the URL for Small image size
     */
    public final String getSmallUrl() {
        return getUrlString(mUrls.get("Small"));
    }

    /**
     * Gets URL for Square image size.
     * @return the URL for Square image size
     */
    public final String getSquareUrl() {
        return getUrlString(mUrls.get("Square"));
    }

    /**
     * Gets URL for Thumbnail image size.
     * @return the URL for Thumbnail image size
     */
    public final String getThumbnailUrl() {
        return getUrlString(mUrls.get("Thumbnail"));
    }

    /**
     * Gets a URL string from an object.
     * @param urlObject the url object
     * @return the URL string
     */
    private String getUrlString(final Object urlObject) {
        String urlString;
        if (urlObject != null) {
            urlString = String.valueOf(urlObject);
        } else {
            urlString = null;
        }
        return urlString;
    }
}
