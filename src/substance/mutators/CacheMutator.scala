package substance.mutators

import substance.BaseSubstance
import config.CacheManager
import java.awt.image.ImageObserver
import swing.Graphics2D
import swing.Image


/**
 * @author Ivyl
 */
trait CacheMutator extends BaseSubstance {
  var name = "default"

  private var oldName = name
  private var oldAngle = angle

  var image: Option[Image] = CacheManager.image.retrieveRotated(name, angle.toInt)

  abstract override def paint(g: Graphics2D, observer: ImageObserver) {
    super.paint(g, observer)
    if (oldName != name || oldAngle != angle) {
      oldAngle = angle
      oldName = name
      image = CacheManager.image.retrieveRotated(name, angle.toInt)
    }
  }
}