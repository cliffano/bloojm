package com.mbledug.blojsom.plugin.galleryr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photosets.PhotosetsInterface;

public class FlickrFacadeTest extends TestCase {

    public static final String API_KEY = "e7322167b9314f6e600bc7ec86b5bd40";
    public static final String PHOTO_IDS_CSV = "31670708,31671077";
    public static final String PHOTOSET_IDS_CSV = "711101,711155";

//    public void testGetPhotosViaLiveService() {
//        FlickrFacade flickrFacade = new FlickrFacade(API_KEY);
//        List photos = flickrFacade.getPhotos(PHOTO_IDS_CSV);
//        assertNotNull(photos);
//        for (Iterator it = photos.iterator(); it.hasNext();) {
//            GalleryrPhoto photo = (GalleryrPhoto) it.next();
//            assertPhoto(photo);
//        }
//    }

    public void testGetPhotosViaMockService() throws Exception {
    	Photo photo = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo.getId()).andReturn("dummyId1");
    	EasyMock.expect(photo.getId()).andReturn("dummyId2");
    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getInfo("31670708", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andReturn(new ArrayList());
    	EasyMock.expect(photosInterface.getInfo("31671077", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andReturn(new ArrayList());
    	Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosInterface, photo});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        List photos = flickrFacade.getPhotos(PHOTO_IDS_CSV);
        assertNotNull(photos);
        assertTrue(photos.size() == PHOTO_IDS_CSV.split(",").length);

        EasyMock.verify(new Object[]{flickr, photosInterface, photo});
    }


    public void testGetPhotosViaMockServiceHandlesSAXException() throws Exception {

    	Photo photo = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo.getId()).andReturn("dummyId1");
    	EasyMock.expect(photo.getId()).andReturn("dummyId2");
    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getInfo("31670708", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andThrow(new SAXException());
    	EasyMock.expect(photosInterface.getInfo("31671077", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andThrow(new SAXException());
    	Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosInterface, photo});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        try {
            List photos = flickrFacade.getPhotos(PHOTO_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }

        EasyMock.verify(new Object[]{flickr, photosInterface, photo});
    }

    public void testGetPhotosViaMockServiceHandlesIOException() throws Exception {

    	Photo photo = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo.getId()).andReturn("dummyId1");
    	EasyMock.expect(photo.getId()).andReturn("dummyId2");
    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getInfo("31670708", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andThrow(new IOException());
    	EasyMock.expect(photosInterface.getInfo("31671077", null)).andReturn(photo);
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andThrow(new IOException());
    	Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosInterface, photo});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        try {
            List photos = flickrFacade.getPhotos(PHOTO_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }

        EasyMock.verify(new Object[]{flickr, photosInterface, photo});
    }

//    public void testGetPhotosetsViaLiveService() {
//        FlickrFacade flickrFacade = new FlickrFacade(API_KEY);
//        List photos = flickrFacade.getPhotosFromPhotosets(PHOTOSET_IDS_CSV);
//        assertNotNull(photos);
//        for (Iterator it = photos.iterator(); it.hasNext();) {
//            GalleryrPhoto photo = (GalleryrPhoto) it.next();
//            assertPhoto(photo);
//        }
//    }

    public void testGetPhotosetsViaMockService() throws Exception {

    	Photo photo1 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo1.getId()).andReturn("dummyId1");
    	Photo photo2 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo2.getId()).andReturn("dummyId2");

    	List photosetsPhotos1 = new ArrayList();
    	photosetsPhotos1.add(photo1);
    	List photosetsPhotos2 = new ArrayList();
    	photosetsPhotos2.add(photo2);

        PhotosetsInterface photosetsInterface = (PhotosetsInterface) EasyMock.createStrictMock(PhotosetsInterface.class);
        EasyMock.expect(photosetsInterface.getPhotos("711101")).andReturn(photosetsPhotos1);
        EasyMock.expect(photosetsInterface.getPhotos("711155")).andReturn(photosetsPhotos2);

    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andReturn(new ArrayList());
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andReturn(new ArrayList());

        Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosetsInterface()).andReturn(photosetsInterface);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        List photos = flickrFacade.getPhotosFromPhotosets(PHOTOSET_IDS_CSV);
        assertNotNull(photos);
        assertTrue(photos.size() == 2);

        EasyMock.verify(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});
    }

    public void testGetPhotosetsViaMockServiceHandlesSAXException() throws Exception {

    	Photo photo1 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo1.getId()).andReturn("dummyId1");
    	Photo photo2 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo2.getId()).andReturn("dummyId2");

    	List photosetsPhotos1 = new ArrayList();
    	photosetsPhotos1.add(photo1);
    	List photosetsPhotos2 = new ArrayList();
    	photosetsPhotos2.add(photo2);

        PhotosetsInterface photosetsInterface = (PhotosetsInterface) EasyMock.createStrictMock(PhotosetsInterface.class);
        EasyMock.expect(photosetsInterface.getPhotos("711101")).andReturn(photosetsPhotos1);
        EasyMock.expect(photosetsInterface.getPhotos("711155")).andReturn(photosetsPhotos2);

    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andThrow(new SAXException());
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andThrow(new SAXException());

        Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosetsInterface()).andReturn(photosetsInterface);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(PHOTOSET_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }

        EasyMock.verify(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});
    }

    public void testGetPhotosetsViaMockServiceHandlesIOException() throws Exception {

    	Photo photo1 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo1.getId()).andReturn("dummyId1");
    	Photo photo2 = (Photo) EasyMock.createStrictMock(Photo.class);
    	EasyMock.expect(photo2.getId()).andReturn("dummyId2");

    	List photosetsPhotos1 = new ArrayList();
    	photosetsPhotos1.add(photo1);
    	List photosetsPhotos2 = new ArrayList();
    	photosetsPhotos2.add(photo2);

        PhotosetsInterface photosetsInterface = (PhotosetsInterface) EasyMock.createStrictMock(PhotosetsInterface.class);
        EasyMock.expect(photosetsInterface.getPhotos("711101")).andReturn(photosetsPhotos1);
        EasyMock.expect(photosetsInterface.getPhotos("711155")).andReturn(photosetsPhotos2);

    	PhotosInterface photosInterface = (PhotosInterface) EasyMock.createStrictMock(PhotosInterface.class);
    	EasyMock.expect(photosInterface.getSizes("dummyId1")).andThrow(new IOException());
    	EasyMock.expect(photosInterface.getSizes("dummyId2")).andThrow(new IOException());

        Flickr flickr = (Flickr) EasyMock.createStrictMock(Flickr.class);
    	EasyMock.expect(flickr.getPhotosetsInterface()).andReturn(photosetsInterface);
    	EasyMock.expect(flickr.getPhotosInterface()).andReturn(photosInterface);

    	EasyMock.replay(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});

        FlickrFacade flickrFacade = new FlickrFacade(flickr);
        try {
            List photos = flickrFacade.getPhotosFromPhotosets(PHOTOSET_IDS_CSV);
            assertNotNull(photos);
            assertTrue(photos.size() == 0);
        } catch (Exception e) {
            fail("Thrown exception should've been handled by FlickrFacade: " + e);
        }

        EasyMock.verify(new Object[]{flickr, photosetsInterface, photosInterface, photo1, photo2});
    }

//    public void testGetUnavailablePhotosViaLiveServiceHandlesFlickrException() {
//        FlickrFacade flickrFacade = new FlickrFacade(API_KEY);
//        try {
//            List photos = flickrFacade.getPhotos(PHOTO_IDS_CSV + ",xyz");
//            assertTrue(photos.size() == PHOTOSET_IDS_CSV.split(",").length);
//        } catch (Exception e) {
//            fail("Thrown exception should've been handled by FlickrFacade: " + e);
//        }
//    }
//
//    public void testGetUnavailablePhotosetsViaLiveServiceHandlesFlickrException() {
//        FlickrFacade flickrFacade = new FlickrFacade(API_KEY);
//        try {
//            List photos = flickrFacade.getPhotosFromPhotosets(PHOTOSET_IDS_CSV + ",abcdef");
//            assertNotNull(photos);
//        } catch (Exception e) {
//            fail("Thrown exception should've been handled by FlickrFacade: " + e);
//        }
//    }
//
//    private void assertPhoto(GalleryrPhoto photo) {
//        assertNotNull(photo.getFlickrPhoto());
//        assertNotNull(photo.getId());
//        assertNull(photo.getDescription());
//        assertNotNull(photo.getLargeUrl());
//        assertNotNull(photo.getMediumUrl());
//        assertNull(photo.getOriginalUrl());
//        assertNotNull(photo.getSmallUrl());
//        assertNotNull(photo.getSquareUrl());
//        assertNotNull(photo.getThumbnailUrl());
//    }
}
