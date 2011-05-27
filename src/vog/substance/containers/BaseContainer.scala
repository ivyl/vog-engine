package vog.substance.containers

import vog.substance.Substance
import swing.Graphics2D
import java.awt.image.ImageObserver


/**
 * Holds substances, paints it and does behaviour. Everything happensin layers.
 * Order is held as in order.
 * @author Ivyl
 */
class BaseContainer {

  /**
   * Order of layers to interact. If some layer isn't here, it isn't used.
   */
  var order = List[String]()

  private var layers = Map[String, Set[Substance]]()

  /**
   * adds to named layer
   * If layer don't exist, it's created.
   */
  def addSubstance(layerName: String, substance: Substance) = synchronized {
    val layer = layers get layerName
    if (layer.isDefined) {
      layers = (layers - layerName) + ((layerName, (layer.get + substance)))
    } else {
      layers = layers + ((layerName, Set(substance)))
    }
  }

  /**
   * Draws all substance layer by layer by order specified in order.
   */
  def drawAll(g: Graphics2D, observer: ImageObserver) {
    foreachOrdered(_.draw(g,observer))
  }

  /**
   * Makes all substance to behave.
   */
  def behaveAll() {
    foreachOrdered( _.behave() )
  }

  /**
   * Removes all substances marked as dead form all layers.
   */
  def removeDead() {
    layers = layers.map { layer =>
      (layer._1, layer._2.filterNot(_.isDead))
    }
  }

  /**
   * Launches function for each substance.
   * @param function function to invoke, gets substance as parameter.
   */
  @throws(classOf[NoLayerException])
  def foreachOrdered(function: Substance => Unit) {
    order.foreach { layerName =>
      val layer = layers.get(layerName)
      if (layer.isDefined) {
        layer.get.foreach(function)
      } else {
        throw new NoLayerException
      }
    }
  }


}