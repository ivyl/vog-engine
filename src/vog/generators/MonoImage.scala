package vog.generators

import swing.Color
import java.awt.image.BufferedImage

/**
 * Generates monochromatic image.
 * @author Ivyl
 */
object MonoImage {
  /**
   * generates image filled with single color
   * @param width
   * @param height
   * @param color
   */
  def apply(width: Int, height: Int, color: Color) = {
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    for (x <- 0 until width; y <- 0 until height)  {
      img.setRGB(x, y, color.getRGB)
    }

    img
  }
}
