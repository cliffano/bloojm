package com.mbledug.blojsom.plugin.galleryr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.PluginException;
import org.easymock.EasyMock;

public class GalleryrPluginTest extends TestCase {

	public static final String API_KEY = "e7322167b9314f6e600bc7ec86b5bd40";
    public static final String PHOTO_IDS_CSV = "31670708,31671077";
    public static final String PHOTOSET_IDS_CSV = "711101,711155";

    public void testProcessWithNullApiKeyGivesPluginException() {
        GalleryrPlugin galleryrPlugin = new GalleryrPlugin();
        try {
            galleryrPlugin.init();

            Entry[] entries = new Entry[]{createEntryWithGalleryrMetaData()};

            Map properties = new HashMap();
            properties.put(GalleryrPlugin.PROPERTY_API_KEY, "");
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);

            galleryrPlugin.process(
            		(HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
            		(HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                    blog,
                    new HashMap(),
                    entries);

            fail("PluginException should've been thrown.");
        } catch (PluginException pe) {
            // expected PluginException
        }
    }

    public void testProcessSetsPhotosAsEntryMetadata() {

        GalleryrPlugin galleryrPlugin = new GalleryrPlugin();
        try {
            galleryrPlugin.init();

            Entry[] entries = new Entry[]{createEntryWithGalleryrMetaData()};

            Map properties = new HashMap();
            properties.put(GalleryrPlugin.PROPERTY_API_KEY, API_KEY);
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);

            galleryrPlugin.process(
            		(HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
            		(HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
                    blog,
                    new HashMap(),
                    entries);

            DatabaseEntry entry = (DatabaseEntry) entries[0];
            List photos = (List) entry.getMetaData().get(GalleryrPlugin.METADATA_PHOTOS);
            assertNotNull(photos);
            assertTrue(photos.size() > 0);
            for (Iterator it = photos.iterator(); it.hasNext();) {
                GalleryrPhoto photo = (GalleryrPhoto) it.next();
                assertNotNull(photo);
            }

            galleryrPlugin.cleanup();
            galleryrPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException shouldn't have been thrown: " + pe);
        }
    }

    private Entry createEntryWithGalleryrMetaData() {
        Map metaData = new HashMap();
        metaData.put(GalleryrPlugin.METADATA_PHOTOSET_IDS, PHOTOSET_IDS_CSV);
        metaData.put(GalleryrPlugin.METADATA_PHOTO_IDS, PHOTO_IDS_CSV);

        DatabaseEntry entry = new DatabaseEntry();
        entry.setMetaData(metaData);

        return entry;
    }
}
