package com.mbledug.blojsom.plugin.galleryr;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

public class FlickrFacadeTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testGetPhotosViaLiveService() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV);
        assertNotNull(photos);
        for (Iterator it = photos.iterator(); it.hasNext();) {
            GalleryrPhoto photo = (GalleryrPhoto) it.next();
            assertPhoto(photo);
        }
    }

    public void testGetPhotosViaMockService() {
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosInterface(null));
        List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV);
        assertNotNull(photos);
        assertTrue(photos.size() == DataFixture.PHOTO_IDS_CSV.split(",").length);
    }


    public void testGetPhotosViaMockServiceHandlesSAXException() {
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosInterface(new SAXException()));
        try {
            List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetPhotosViaMockServiceHandlesIOException() {
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosInterface(new IOException()));
        try {
            List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetPhotosetsViaLiveService() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV);
        assertNotNull(photos);
        for (Iterator it = photos.iterator(); it.hasNext();) {
            GalleryrPhoto photo = (GalleryrPhoto) it.next();
            assertPhoto(photo);
        }
    }

    public void testGetPhotosetsViaMockService() {
        int size = DataFixture.PHOTOSET_IDS_CSV.split(",").length;
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosetsInterface(size, null));
        List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV);
        assertNotNull(photos);
        assertTrue(photos.size() == size * size);
    }

    public void testGetPhotosetsViaMockServiceHandlesSAXException() {
        int size = DataFixture.PHOTOSET_IDS_CSV.split(",").length;
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosetsInterface(size, new SAXException()));
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetPhotosetsViaMockServiceHandlesIOException() {
        int size = DataFixture.PHOTOSET_IDS_CSV.split(",").length;
        FlickrFacade flickrFacade = new FlickrFacade(mDataFixture.createMockFlickrWithPhotosetsInterface(size, new IOException()));
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetUnavailablePhotosViaLiveServiceHandlesFlickrException() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        try {
            List photos = flickrFacade.getPhotos(DataFixture.PHOTO_IDS_CSV + ",xyz");
            assertTrue(photos.size() == DataFixture.PHOTOSET_IDS_CSV.split(",").length);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    public void testGetUnavailablePhotosetsViaLiveServiceHandlesFlickrException() {
        FlickrFacade flickrFacade = new FlickrFacade(DataFixture.API_KEY);
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(DataFixture.PHOTOSET_IDS_CSV + ",abcdef");
            assertNotNull(photos);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }
    }

    private void assertPhoto(GalleryrPhoto photo) {
        assertNotNull(photo.getFlickrPhoto());
        assertNotNull(photo.getId());
        assertNull(photo.getDescription());
        assertNotNull(photo.getLargeUrl());
        assertNotNull(photo.getMediumUrl());
        assertNull(photo.getOriginalUrl());
        assertNotNull(photo.getSmallUrl());
        assertNotNull(photo.getSquareUrl());
        assertNotNull(photo.getThumbnailUrl());
    }
}
