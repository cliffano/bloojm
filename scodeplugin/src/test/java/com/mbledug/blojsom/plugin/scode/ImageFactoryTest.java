package com.mbledug.blojsom.plugin.scode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import com.mbledug.blojsom.plugin.scode.engine.FishEyeImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.FunkyImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.GradientImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.KinkImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.ShadowImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.SimpleImageEngine;

public class ImageFactoryTest extends TestCase {

    public void testCreatingNewImageFactoryWithNullImageEnginesGivesIllegalArgumentException() {
        try {
            ImageFactory imageFactory = new ImageFactory(null);
            fail("Image factory construction with null image engines should've thrown IllegalArgumentException. ImageFactory: " + imageFactory);
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    public void testGetImageAllFlavors() throws IOException {
        ImageFactory imageFactory = new ImageFactory(createEngines());
        String text = "456789";
        writePngImageFile("simple.png",
                imageFactory.getImage(text, "simple"));
        writePngImageFile("gradient.png",
                imageFactory.getImage(text, "gradient"));
        writePngImageFile("funky.png",
                imageFactory.getImage(text, "funky"));
        writePngImageFile("kink.png",
                imageFactory.getImage(text, "kink"));
        writePngImageFile("fisheye.png",
                imageFactory.getImage(text, "fisheye"));
        writePngImageFile("shadow.png",
                imageFactory.getImage(text, "shadow"));
        writePngImageFile("default.png",
                imageFactory.getImage(text, null));
    }

    public void testGetImageWithNullTextGivesIllegalArgumentException() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
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

    private Map createEngines() {
        Map engines = new HashMap();
        engines.put("simple", new SimpleImageEngine());
        engines.put("gradient", new GradientImageEngine());
        engines.put("funky", new FunkyImageEngine());
        engines.put("kink", new KinkImageEngine());
        engines.put("fisheye", new FishEyeImageEngine());
        engines.put("shadow", new ShadowImageEngine());
        return engines;
    }
}
