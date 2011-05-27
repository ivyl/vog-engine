package vog.cache

import java.awt.image.BufferedImage

object Image {
  /** Number of bits of integer used for bitmask. */
  val bitWidth = 30
}

/**
 * Holds buffered image and it's alphachannel bitmask.
 * USING 30 BITS because jvm uses signed integers and there are some problems when bitshifting 0x80000000
 * @author Ivyl
 */
class Image(val image: BufferedImage) {

  protected var theBitmask: Option[Array[Array[Int]]] = None

  /**
   * Returns alpha channel bitmask.
   * Builds it first when not build.
   * (x)(y)
   * Holds 32bit ints where each bit represents alpha transparency of pixel in row (x).
   * Additional pixels filled with zeros
   */
  def alphaBitmask = {
    if (theBitmask.isEmpty) {
      buildBitmask
    }
    theBitmask.get
  }

  /**
   * Builds alpha bitmask of holded buffered image.
   * (x)(y)
   * Holds 30bit ints where each bit represents alpha transparency of pixel in row (x).
   * Additional pixels filled with zeros.
   */
  def buildBitmask {
    val width = (image.getWidth.toFloat/Image.bitWidth).ceil.toInt
    val height = image.getHeight
    val bitmask: Array[Array[Int]] = Array.ofDim(width, height)

    for (y <- 0 until height) {
      for (xint <- 0 until width) {

        var value = 1

        for(bit <- 0 until Image.bitWidth) {
          value = (value << 1)

          val position = xint*Image.bitWidth+bit //current position on image

          if (position < this.width) { //are we still in range?
            val alphaValue = image.getRGB(position, y) >> 24
            if (alphaValue != 0) {
              value = value | 1
            }
          }
        }

        bitmask(xint)(y) = value
      }
    }

    theBitmask = Some(bitmask)
  }

  def width = image.getWidth

  def height = image.getHeight

}
