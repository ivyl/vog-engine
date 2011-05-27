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
    val colorInt = 0xFF000000 | (color.getRed << 16) | (color.getGreen << 8) | (color.getBlue)

    for (x <- 0 until width; y <- 0 until height)  {
      img.setRGB(x, y, colorInt)
    }

    img
  }
}
