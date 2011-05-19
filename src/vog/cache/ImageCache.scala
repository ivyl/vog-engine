package vog.cache

import java.awt.image.AffineTransformOp
import java.awt.geom.AffineTransform
import javax.imageio.ImageIO
import java.io.{IOException, File}
import net.lag.logging.Logger
import vog.config.Configuration

/**
 *  Retrieves and caches images.
 *  Images should be stored as data/images/ *.png
 *  * applies for subdirectories (just pass full relative path without extension).
 *  @author Ivyl
 */
class ImageCache extends ResourceCache[Image] with RotationCache[Image] {

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
   *  Similar as retrieve it caches images, and if cached it is got from vog.cache.
   *  @param   name   file location
   *  @param   degree rotation in degrees
   *  @return  rotated image
   */
  @throws(classOf[NoSuchElementException])
  def retrieveRotated(name: String, degree: Int): Option[Image] = {
    //normalize
    var deg = degree % 360;
    if (deg < 0) deg = 360 - deg

    val fullName = name + "_" + deg

    if (resources contains fullName) {

      resources.get(fullName)

    } else {
      val imageOption = retrieve(name)
      if (imageOption.isDefined) {
        var image = imageOption.get.image
        val tx = new AffineTransform
        val radians = math.toRadians(deg)

        tx.rotate(radians, image.getWidth/2, image.getHeight/2)

        val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)

        image = op.filter(image, null)
        val packedImage = new Image(image)
        resources.put(fullName, packedImage)
        Some(packedImage)
      } else {
        None
      }

    }
  }

  protected def loadResource(file: File) = {
    var img: Option[Image] = None

    try {
      val image = new Image(ImageIO.read(file))
      img = Some(image)
    } catch {
      case e : IOException => Logger.get.warning(e, "Image cache couldn't find/load " + file.getAbsolutePath)
    }

    img
  }
}