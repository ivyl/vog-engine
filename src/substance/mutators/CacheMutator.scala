package substance.mutators

import substance.BaseSubstance
import config.CacheManager
import java.awt.image.ImageObserver
import swing.Graphics2D
import swing.Image


/**
 * Mutator which uses image cache to receive image basing on name.
 * @author Ivyl
 */
trait CacheMutator extends Mutator {
  var name = "default"

  private var oldName = name
  private var oldAngle = angle

  var image: Option[Image] = CacheManager.image.retrieveRotated(name, angle.toInt)

  protected abstract override def paint(g: Graphics2D, observer: ImageObserver) {
    if (oldName != name || oldAngle != angle) {
      oldAngle = angle
      oldName = name
      image = CacheManager.image.retrieveRotated(name, angle.toInt)
    }
    super.paint(g, observer)
  }
}