package com.mbledug.blojsom.plugin.galleryr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.PluginException;

import com.aetrion.flickr.photos.Photo;

public class GalleryrPluginTest extends TestCase {

    public void testInitWithNullApiKeyGivesPluginException() {
        GalleryrPlugin galleryrPlugin = new GalleryrPlugin();
        try {
            galleryrPlugin.init();
            fail("PluginException should've been thrown.");
        } catch (PluginException pe) {
            // expected PluginException
        }
    }

    public void testProcessSetsPhotosAsEntryMetadata() {
        DataFixture dataFixture = new DataFixture();

        Properties properties = new Properties();
        properties.put(GalleryrPlugin.PROPERTY_API_KEY, DataFixture.API_KEY);

        GalleryrPlugin galleryrPlugin = new GalleryrPlugin();
        galleryrPlugin.setProperties(properties);
        try {
            galleryrPlugin.init();

            Entry[] entries = new Entry[]{dataFixture.createEntryWithGalleryrMetaData()};

            galleryrPlugin.process(
                    dataFixture.createMockHttpServletRequest(),
                    dataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);

            DatabaseEntry entry = (DatabaseEntry) entries[0];
            List photos = (List) entry.getMetaData().get(GalleryrPlugin.METADATA_PHOTOS);
            assertNotNull(photos);
            assertTrue(photos.size() > 0);
            for (Iterator it = photos.iterator(); it.hasNext();) {
                Photo photo = (Photo) it.next();
                assertNotNull(photo.getId());
                assertNotNull(photo.getThumbnailUrl());
                assertNotNull(photo.getOriginalUrl());
            }

            galleryrPlugin.cleanup();
            galleryrPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException shouldn't have been thrown: " + pe);
        }
    }
}
