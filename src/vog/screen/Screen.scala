package vog.screen

import swing.Graphics2D

/**
 * Represents single screen type.
 * Reactions through Scala reactor (as in swing)
 * {{{
 *  listenTo(mouse)
 *  listenTo(keys)
 *  //...
 *  listenTo(mouse.clicks)
 *
 *  reactions += {
 *    case e: MouseClicked => println(e.point)
 *  }
 * }}}
 * reactions
 * @author Ivyl
 */
abstract class Screen extends swing.Component {

  /**
   * Painters that are invoked each time component is painted.
   */
  private var painters = List[Graphics2D => Unit]()

  /** returns peer laying under this wrapper, used as observer */
  def observer = this.peer

  def width = this.size.getWidth.toInt
  def height = this.size.getHeight.toInt

  /** Used by Main to paint this component */
  override def paint(g: Graphics2D) = synchronized {
    super.paint(g)

    painters.foreach(f => f(g))

    repaint() //forcing constant repainting
  }

  /**
   * Painter is functions which is invoked each time component is painted.
   * Painted is appended. Painters of object which are "on top" should be added first.
   *
   * @param painter function that paints on given graphic each time screen is refreshed.
   */
  def addPainter(painter: Graphics2D => Unit) = synchronized {
    painters = painters ::: List(painter)
  }

  /**
   * Remove painter that was added first.
   * It should be THE SAME painter that was added.
   *
   * @param painter painter to be deleted.
   */
  def removePainter(painter: Graphics2D => Unit) = synchronized {
    painters = painters.filterNot(_ == painter)
  }

  /**
   * Single screen "tick". Represents atomic calculations.
   */
  def tick()

  /**
   * Returns next screen to be displayed.
   * If None then this screen is used in next tick.
   */
  def nextScreen: Option[Screen]
}
