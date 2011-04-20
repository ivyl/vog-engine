package substance

import scala.swing.Graphics2D
import java.awt.image.ImageObserver
/**
 * Base substance class,
 * @author Ivyl
 */
trait Substance extends BaseSubstance {

  var x = 0.0
  var y = 0.0
  var angle = 0.0

  protected def paint(g: Graphics2D, observer: ImageObserver) {
      if (image.isDefined) {
        g.drawImage(image.get, x.toInt, y.toInt, observer)
      }
  }


}