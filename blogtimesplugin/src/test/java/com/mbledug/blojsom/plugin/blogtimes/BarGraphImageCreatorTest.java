package com.mbledug.blojsom.plugin.blogtimes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

public class BarGraphImageCreatorTest extends TestCase {

    public void testCreateImageAllFlavors() throws IOException {
        BarGraphImageCreator creator = new BarGraphImageCreator();
        Date[] dates = DataFixture.createRandomDates(25);
        writePngImageFile("secondOfMinute.png",
                creator.createImage(BlogTimesPlugin.FLAVOR_SECOND_OF_MINUTE, dates));
        writePngImageFile("minuteOfHour.png",
                creator.createImage(BlogTimesPlugin.FLAVOR_MINUTE_OF_HOUR, dates));
        writePngImageFile("hourOfDay.png",
                creator.createImage(BlogTimesPlugin.FLAVOR_HOUR_OF_DAY, dates));
        writePngImageFile("default.png",
                creator.createImage(null, dates));
    }

    public void testCreateImageOverrideConfigurations() throws IOException {
        BarGraphImageCreator creator = new BarGraphImageCreator();
        creator.setBackgroundColor(Color.YELLOW);
        creator.setBarBackgroundColor(Color.CYAN);
        creator.setBorderColor(Color.BLACK);
        creator.setBarHeight(20);
        creator.setBarWidth(200);
        creator.setFontColor(Color.RED);
        creator.setTimelineColor(Color.ORANGE);
        writePngImageFile("override.png",
                creator.createImage(null, DataFixture.createRandomDates(25)));
    }

    public void testCreateImageOverrideConfigurationsWithInvalidBoxDimensionGivesIllegalArgumentException() throws IOException {
        BarGraphImageCreator creator = new BarGraphImageCreator();
        try {
            creator.setBarHeight(0);
            fail("IllegalArgumentException should've been thrown for invalid box height.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
        try {
            creator.setBarWidth(-1);
            fail("IllegalArgumentException should've been thrown for invalid box width.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    private void writePngImageFile(String filename, BufferedImage image) throws IOException {
        File file = new File("target/" + filename);
        ImageIO.write(image, "png", file);
    }
}
