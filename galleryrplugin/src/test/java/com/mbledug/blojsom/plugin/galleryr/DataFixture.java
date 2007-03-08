package com.mbledug.blojsom.plugin.galleryr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photosets.PhotosetsInterface;

public class DataFixture extends MockObjectTestCase {

    public static final String API_KEY = "e7322167b9314f6e600bc7ec86b5bd40";
    public static final String PHOTO_IDS_CSV = "31670708,31671077";
    public static final String PHOTOSET_IDS_CSV = "711101,711155";

    Entry createEntryWithGalleryrMetaData() {
        Map metaData = new HashMap();
        metaData.put(GalleryrPlugin.METADATA_PHOTOSET_IDS, PHOTOSET_IDS_CSV);
        metaData.put(GalleryrPlugin.METADATA_PHOTO_IDS, PHOTO_IDS_CSV);

        DatabaseEntry entry = new DatabaseEntry();
        entry.setMetaData(metaData);

        return entry;
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }

    Flickr createMockFlickrWithPhotosInterface(Exception exception) {
        Mock mockFlickr = mock(Flickr.class, new Class[]{String.class}, new Object[]{API_KEY});
        mockFlickr
                .expects(once())
                .method("getPhotosInterface")
                .will(returnValue(createMockPhotosInterface(exception)));
        return (Flickr) mockFlickr.proxy();
    }

    Flickr createMockFlickrWithPhotosetsInterface(int size, Exception exception) {
        Mock mockFlickr = mock(Flickr.class, new Class[]{String.class}, new Object[]{API_KEY});
        mockFlickr
                .expects(once())
                .method("getPhotosetsInterface")
                .will(returnValue(createMockPhotosetsInterface(size)));
        mockFlickr
                .expects(once())
                .method("getPhotosInterface")
                .will(returnValue(createMockPhotosInterface(exception)));
        return (Flickr) mockFlickr.proxy();
    }

    PhotosInterface createMockPhotosInterface(Exception exception) {
        Mock mockPhotosInterface = mock(PhotosInterface.class, new Class[]{String.class, REST.class}, new Object[]{API_KEY, null});
        mockPhotosInterface
                .expects(atLeastOnce())
                .method("getInfo")
                .will(returnValue(createMockPhoto()));
        if (exception == null) {
            mockPhotosInterface
                    .expects(atLeastOnce())
                    .method("getSizes")
                    .will(returnValue(new ArrayList()));
        } else {
            mockPhotosInterface
                    .expects(atLeastOnce())
                    .method("getSizes")
                    .will(throwException(exception));
        }
        return (PhotosInterface) mockPhotosInterface.proxy();
    }

    PhotosetsInterface createMockPhotosetsInterface(int size) {
        Mock mockPhotosetsInterface = mock(PhotosetsInterface.class, new Class[]{String.class, REST.class}, new Object[]{API_KEY, null});
        mockPhotosetsInterface
                .expects(atLeastOnce())
                .method("getPhotos")
                .will(returnValue(createMockPhotos(size)));
        return (PhotosetsInterface) mockPhotosetsInterface.proxy();
    }

    Collection createMockPhotos(int size) {
        List photos = new ArrayList();
        for (int i = 0; i < size; i++) {
            photos.add(createMockPhoto());
        }
        return photos;
    }

    Photo createMockPhoto() {
        Mock mockPhoto = mock(Photo.class);
        mockPhoto
                .expects(atLeastOnce())
                .method("getId")
                .will(returnValue("dummyId"));
        return (Photo) mockPhoto.proxy();
    }
}
