package cache

import java.awt.image.AffineTransformOp
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.{IOException, File}
import net.lag.logging.Logger
import config.Configuration

/** Retrieving and caching images.
 *  Images should be stored as data/images/ *.png
 *  * applies for subdirectories (just pass full relative path without extension).
 *  @author Ivyl
 */
class ImageCache extends ResourceCache[BufferedImage] {

  /** Directory used to construct path for image file.
   *  Formula = dir + name + postfix
   *  images.dir in configuration file
   *  @default images/
   */
  val dir = Configuration.config.getString("images.dir", "images/")

  /** Postfix used to construct path for image file.
   *  Formula = dir + name + postfix
   *  image.postfix in configuration file
   * @default .png
   */
  val postfix = Configuration.config.getString("images.postfix", ".png")

  /** Get (and cache) rotated image.
   *  Normal file (not rotated) is read and transfromed, then stored.
   *  Similar as retrieve it caches images, and if cached it is got from cache.
   *  @param   name   file location
   *  @param   degree rotation in degrees
   *  @return
   */
  def retrieveRotated(name: String, degree: Int): BufferedImage = {
    val fullName = name + "_" + degree

    if (resources contains fullName) {

      resources.get(fullName).get

    } else {
      var image = resources.get(name).get
      val tx = new AffineTransform
      val radians = scala.math.toRadians(degree)

      tx.rotate(radians, image.getWidth/2, image.getHeight/2)

      val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)

      image = op.filter(image, null)

      resources.put(fullName, image)

      image
    }
  }

  protected def loadResource(file: File) = {

    var img = new BufferedImage(1,1,1)

    try {
      img = ImageIO.read(file)
    } catch {
      case e : IOException => Logger.get.warning(e, "Sound cache couldn't find/load " + file.getAbsolutePath)
    }

    img
  }
}