package com.mbledug.blojsom.plugin.galleryr;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    public static final String API_KEY = "e7322167b9314f6e600bc7ec86b5bd40";
    public static final String PHOTO_IDS_CSV = "31670708,31671077";
    public static final String PHOTOSET_IDS_CSV = "711101,711155";

    Entry createEntryWithGalleryrMetaData() {
        Map metaData = new HashMap();
        metaData.put("galleryr-photosets-id", PHOTOSET_IDS_CSV);
        metaData.put("galleryr-photos-id", PHOTO_IDS_CSV);

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
}
