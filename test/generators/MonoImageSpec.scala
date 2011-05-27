package test.generators

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import vog.generators.MonoImage
import java.awt.Color
import java.awt.image.BufferedImage


/**
 * @author Ivyl
 */
class MonoImageSpec extends Spec with ShouldMatchers {
  describe("MonoImage") {
    it("should generate buffered image with given specs") {
      val width = 10
      val height = 200
      val color = Color.cyan

      val img = MonoImage(width, height, color)

      img.getWidth should equal (width)
      img.getHeight should equal (height)
      img.getType should equal (BufferedImage.TYPE_INT_ARGB)

      for(x <- 0 until img.getWidth; y <- 0 until img.getHeight) {

        img.getRGB(x,y) should equal (color.getRGB)

      }
    }
  }
}