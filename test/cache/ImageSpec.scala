package test.cache

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.awt.image.BufferedImage
import vog.cache.Image

/**
 * @author Ivyl
 */
class ImageSpec extends Spec with ShouldMatchers {

  describe("Image") {
    it("should hold image") {
      val bufferedImage = new BufferedImage(20,21, BufferedImage.TYPE_4BYTE_ABGR)
      val image = new Image(bufferedImage)

      image.image should equal (bufferedImage)
    }

    it("should build bitmap") {
      val bufferedImage = new BufferedImage(20,21, BufferedImage.TYPE_4BYTE_ABGR)
      val image = new Image(bufferedImage)

      image.alphaBitmask
    }

    it("should build proper bitmask") {
      val width = 211
      val height = 223
      val rand = new java.util.Random
      val bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_4BYTE_ABGR)

      //build image
      for(x <- 0 until width) {
        for (y <- 0 until height) {
          if (rand.nextBoolean) {
            bufferedImage.setRGB(x, y, 0xFFFFFFFF)
          } else {
            bufferedImage.setRGB(x, y, 0)
          }
        }
      }

      val image = new Image(bufferedImage)
      val bitmask = image.alphaBitmask

      //checking pixel by pixel
      for (x <- 0 until width) {
        for (y <- 0 until height) {
          val bitseq = bitmask(x/Image.bitWidth)(y);

          //shifting to right position first bit is Image.bitWidth
          val bit = (bitseq & 1 << (Image.bitWidth - x%Image.bitWidth - 1))

          if (bufferedImage.getRGB(x,y) != 0) {
            bit should not equal (0)
          } else {
            bit should equal (0)
          }
        }
      }
    }

  }
}