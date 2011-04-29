package vog.screen

import swing.Graphics2D
import java.awt.Color

/**
 * @author Ivyl
 */
class Screen extends swing.Component {

  /**
   * Painters that are invoked each time component is painted.
   * New painters are prepended so they paints sooner.
   */
  private var painters = List[Graphics2D => Unit]()

  override def paint(g: Graphics2D) = synchronized {
    super.paint(g)
    painters.foreach(f => f(g))
  }

  def addPainter(painter: Graphics2D => Unit) = synchronized {
    painters = painters ::: List(painter)
  }

  def removePainter(painter: Graphics2D => Unit) = synchronized {
    painters = painters.filterNot(_ == painter)
  }

  def tick {

  }

  def nextScreen: Option[Screen] = None

  def draw {

  }

}