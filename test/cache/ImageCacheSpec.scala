package test.cache

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import java.awt.image.BufferedImage
import vog.cache.{Image, ImageCache}

/**
 * @author Ivyl
 */
class ImageCacheSpec extends Spec with ShouldMatchers {

  describe("Image cache") {

    describe("when gets image") {
      val x = 3
      val y = 2

      val image = new BufferedImage(x,y,1)

      val cache = new ImageCache {
        override protected def loadResource(file: File) = Some(new Image(image))
      }

      it("should do tranform (retrieving rotated)") {
        cache.retrieveRotated("", 90).get.image should not equal (image)
      }
    }
  }
}
