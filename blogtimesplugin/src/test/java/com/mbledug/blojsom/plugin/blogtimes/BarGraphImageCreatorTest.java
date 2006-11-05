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
                creator.createImage("second-of-minute", dates));
        writePngImageFile("minuteOfHour.png",
                creator.createImage("minute-of-hour", dates));
        writePngImageFile("hourOfDay.png",
                creator.createImage("hour-of-day", dates));
        writePngImageFile("default.png",
                creator.createImage(null, dates));
    }

    public void testCreateImageOverrideConfigurations() throws IOException {
        BarGraphImageCreator creator = new BarGraphImageCreator();
        creator.setBackgroundColor(Color.YELLOW);
        creator.setBoxBackgroundColor(Color.CYAN);
        creator.setBoxBorderColor(Color.BLACK);
        creator.setBoxHeight(20);
        creator.setBoxWidth(200);
        creator.setFontColor(Color.RED);
        creator.setTimelineColor(Color.ORANGE);
        writePngImageFile("override.png",
                creator.createImage(null, DataFixture.createRandomDates(25)));
    }

    public void testCreateImageOverrideConfigurationsWithInvalidBoxDimensionGivesIllegalArgumentException() throws IOException {
        BarGraphImageCreator creator = new BarGraphImageCreator();
        try {
            creator.setBoxHeight(0);
            fail("IllegalArgumentException should've been thrown for invalid box height.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
        try {
            creator.setBoxWidth(-1);
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
