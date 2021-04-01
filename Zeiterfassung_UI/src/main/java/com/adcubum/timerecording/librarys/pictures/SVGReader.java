package com.adcubum.timerecording.librarys.pictures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class SVGReader {
   private SVGReader() {
      // private
   }

   /**
    * Loads a WritableImage at the given path
    * 
    * @param svgFilePath
    *        the given path
    * @return a WriteableImage
    */
   public static WritableImage loadSVG(String svgFilePath) {
      try (InputStream inputStream = PictureLibrary.class.getResource(svgFilePath).openStream()) {
         TranscoderInput transcoderInput = new TranscoderInput(inputStream);
         BufferedImageTranscoder bufferedImgTranscoder = new BufferedImageTranscoder();
         bufferedImgTranscoder.transcode(transcoderInput, null);
         return SwingFXUtils.toFXImage(bufferedImgTranscoder.getBufferedImage(), null);
      } catch (IOException | TranscoderException e) {
         throw new IllegalStateException("Unable to load svg '" + svgFilePath + "'", e);
      }
   }

   private static class BufferedImageTranscoder extends ImageTranscoder {
      private BufferedImage img = null;

      @Override
      public BufferedImage createImage(int width, int height) {
         return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      }

      @Override
      public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
         this.img = img;
      }

      public BufferedImage getBufferedImage() {
         return img;
      }
   }
}
