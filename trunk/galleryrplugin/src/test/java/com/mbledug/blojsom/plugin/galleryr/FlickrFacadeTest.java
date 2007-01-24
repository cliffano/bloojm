package com.mbledug.blojsom.plugin.galleryr;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.aetrion.flickr.photos.Photo;

public class FlickrFacadeTest extends TestCase {

    public void testGetPhotos() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV);
        assertNotNull(photos);
        for (Iterator it = photos.iterator(); it.hasNext();) {
            Photo photo = (Photo) it.next();
            assertNotNull(photo.getId());
            assertNotNull(photo.getThumbnailUrl());
            assertNotNull(photo.getOriginalUrl());
        }
    }

    public void testGetPhotosets() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV);
        assertNotNull(photos);
        for (Iterator it = photos.iterator(); it.hasNext();) {
            Photo photo = (Photo) it.next();
            assertNotNull(photo.getId());
            assertNotNull(photo.getThumbnailUrl());
            assertNotNull(photo.getOriginalUrl());
        }
    }

    public void testGetUnavailablePhotosHandlesFlickrException() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        try {
            List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV + ",xyz");
            assertTrue(photos.size() == 2);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetUnavailablePhotosetsHandlesFlickrException() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV + ",abcdef");
            assertNotNull(photos);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }
}
