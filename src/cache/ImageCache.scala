package cache

import java.awt.image.AffineTransformOp
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.{IOException, File}
import net.lag.logging.Logger
import config.Configuration

/**
 *  Retrieves and caches images.
 *  Images should be stored as data/images/ *.png
 *  * applies for subdirectories (just pass full relative path without extension).
 *  @author Ivyl
 */
class ImageCache extends ResourceCache[BufferedImage] with RotationCache[BufferedImage] {

  /**
   *  Directory used to construct path for image file.
   *  Formula = dir + name + postfix
   *  images.dir in configuration file
   *  @default images/
   */
  val dir = Configuration.config.getString("images.dir", "images/")

  /**
   *  Postfix used to construct path for image file.
   *  Formula = dir + name + postfix
   *  image.postfix in configuration file
   * @default .png
   */
  val postfix = Configuration.config.getString("images.postfix", ".png")

  /**
   *  Returns rotated image.
   *  Normal file (not rotated) is read and transfromed, then stored.
   *  Similar as retrieve it caches images, and if cached it is got from cache.
   *  @param   name   file location
   *  @param   degree rotation in degrees
   *  @return  rotated image
   */
  @throws(classOf[NoSuchElementException])
  def retrieveRotated(name: String, degree: Int): Option[BufferedImage] = {
    //normalize
    var deg = degree % 360;
    if (deg < 0) deg = 360 - deg

    val fullName = name + "_" + deg

    if (resources contains fullName) {

      resources.get(fullName)

    } else {
      val imageOption = retrieve(name)
      if (imageOption.isDefined) {
        var image = imageOption.get
        val tx = new AffineTransform
        val radians = math.toRadians(deg)

        tx.rotate(radians, image.getWidth/2, image.getHeight/2)

        val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)

        image = op.filter(image, null)

        resources.put(fullName, image)
        Some(image)
      } else {
        None
      }

    }
  }

  protected def loadResource(file: File) = {
    var img: Option[BufferedImage] = None

    try {
      img = Some(ImageIO.read(file))
    } catch {
      case e : IOException => Logger.get.warning(e, "Image cache couldn't find/load " + file.getAbsolutePath)
    }

    img
  }
}