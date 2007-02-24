/**
 * Copyright (c) 2005-2006, Cliffano Subagio
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
 *   * Neither the name of Mbledug nor the names of its contributors
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photosets.PhotosetsInterface;

/**
 * {@link FlickrFacade} provides convenient methods for {@link GalleryrPlugin}
 * to retrieve photos from specified photo or photoset IDs.
 * @author Cliffano Subagio
 */
class FlickrFacade {

    /**
     * Logger for {@link FlickrFacade}.
     */
    private static final Log LOG = LogFactory.getLog(FlickrFacade.class);

    /**
     * Separator for photo and photoset IDs.
     */
    private static final String IDS_SEPARATOR = ",";

    /**
     * Flickr client.
     */
    private Flickr mFlickr;

    /**
     * Creates an instance of {@link FlickrFacade} with specified API key.
     * @param apiKey Flickr API key
     */
    FlickrFacade(final String apiKey) {
        mFlickr = new Flickr(apiKey);
    }

    /**
     * Retrieves a list of photos whichs IDs specified in the comma
     * separated photo IDs.
     * @param photoIdsCsv comma separated photo IDs
     * @return a list of Flickr photos
     */
    List getPhotos(final String photoIdsCsv) {

        PhotosInterface photosInterface = mFlickr.getPhotosInterface();
        List photos = new ArrayList();

        String[] photoIds = photoIdsCsv.split(IDS_SEPARATOR);
        for (int i = 0; i < photoIds.length; i++) {
            try {
                Photo photo = photosInterface.getInfo(photoIds[i], null);
                Collection sizes = photosInterface.getSizes(photo.getId());
                GalleryrPhoto galleryrPhoto = new GalleryrPhoto(photo, sizes);
                photos.add(galleryrPhoto);
            } catch (FlickrException fe) {
                LOG.error("Unable to retrieve photo with ID: " + photoIds[i],
                        fe);
            } catch (SAXException saxe) {
                LOG.error("Unable to retrieve photo with ID: " + photoIds[i],
                        saxe);
            } catch (IOException ioe) {
                LOG.error("Unable to retrieve photo with ID: " + photoIds[i],
                        ioe);
            }
        }

        return photos;
    }

    /**
     * Retrieves a list of photos containing the photos within photosets
     * which IDs specified in the comma separated photoset IDs.
     * @param photosetIdsCsv comma separated photoset IDs
     * @return a list of Flickr photos
     */
    List getPhotosFromPhotosets(final String photosetIdsCsv) {

        PhotosetsInterface photosetsInterface = mFlickr.getPhotosetsInterface();
        PhotosInterface photosInterface = mFlickr.getPhotosInterface();
        List photos = new ArrayList();

        String[] photosetIds = photosetIdsCsv.split(IDS_SEPARATOR);
        for (int i = 0; i < photosetIds.length; i++) {
            try {
                Collection photoset = photosetsInterface.getPhotos(
                        photosetIds[i]);
                for (Iterator it = photoset.iterator(); it.hasNext();) {
                    Photo photo = (Photo) it.next();
                    Collection sizes = photosInterface.getSizes(photo.getId());
                    GalleryrPhoto galleryrPhoto = new GalleryrPhoto(
                            photo, sizes);
                    photos.add(galleryrPhoto);
                }
            } catch (FlickrException fe) {
                LOG.error("Unable to retrieve photoset with ID: "
                        + photosetIds[i], fe);
            } catch (SAXException saxe) {
                LOG.error("Unable to retrieve photoset with ID: "
                        + photosetIds[i], saxe);
            } catch (IOException ioe) {
                LOG.error("Unable to retrieve photoset with ID: "
                        + photosetIds[i], ioe);
            }
        }

        return photos;
    }
}
