package com.mbledug.blojsom.plugin.scode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

public class ImageFactoryTest extends TestCase {

    public void testGetImageAllFlavors() throws IOException {
        ImageFactory imageFactory = new ImageFactory(DataFixture.createEngines());
        String text = DataFixture.SCODE_TEXT;
        writePngImageFile("simple.png",
                imageFactory.getImage(text, "simple"));
        writePngImageFile("gradient.png",
                imageFactory.getImage(text, "gradient"));
        writePngImageFile("funky.png",
                imageFactory.getImage(text, "funky"));
        writePngImageFile("default.png",
                imageFactory.getImage(text, null));
    }

    public void testGetImageWithNullTextGivesIllegalArgumentException() {
        ImageFactory imageFactory = new ImageFactory(DataFixture.createEngines());
        try {
            imageFactory.getImage(null, "simple");
            fail("Get image with null text should've thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    private void writePngImageFile(String filename, BufferedImage image) throws IOException {
        File file = new File("target/" + filename);
        ImageIO.write(image, "png", file);
    }
}