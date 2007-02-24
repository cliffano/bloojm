package com.mbledug.blojsom.plugin.galleryr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.PluginException;

public class GalleryrPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessWithNullApiKeyGivesPluginException() {
        GalleryrPlugin galleryrPlugin = new GalleryrPlugin();
        try {
            galleryrPlugin.init();

            Entry[] entries = new Entry[]{mDataFixture.createEntryWithGalleryrMetaData()};

            Map properties = new HashMap();
            properties.put(GalleryrPlugin.PROPERTY_API_KEY, "");
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);

            galleryrPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
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

            Entry[] entries = new Entry[]{mDataFixture.createEntryWithGalleryrMetaData()};

            Map properties = new HashMap();
            properties.put(GalleryrPlugin.PROPERTY_API_KEY, DataFixture.API_KEY);
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);

            galleryrPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
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
}
