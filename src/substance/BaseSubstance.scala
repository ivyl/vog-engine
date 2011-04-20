package substance

import scala.swing.{Graphics2D,Image}
import java.awt.image.ImageObserver
import annotation.target.getter

/**
 * Base substance class,
 * @author Ivyl
 */
trait BaseSubstance {

  @getter
  var x: Double

  @getter
  var y: Double

  @getter
  var angle: Double

  var image: Option[Image]

  def paint(g: Graphics2D, observer: ImageObserver)

  def behavior

  def behave{
    synchronized {
      behavior
    }
  }

}