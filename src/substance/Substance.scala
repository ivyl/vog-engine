package substance

import scala.swing.{Graphics2D,Image}
import java.awt.image.ImageObserver
import annotation.target.getter

/**
 * Base substance class,
 * @author Ivyl
 */
trait Substance extends BaseSubstance {

  var x = 0.0
  var y = 0.0
  var angle = 0.0


  abstract override def paint(g: Graphics2D, observer: ImageObserver) {
    super.paint(g, observer)
    synchronized {
      if (image.isDefined) {
        g.drawImage(image.get, x.toInt, y.toInt, observer)
      }
    }
  }

  def behavior

}