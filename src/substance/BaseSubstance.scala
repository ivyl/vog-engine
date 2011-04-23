package substance

import scala.swing.{Graphics2D,Image}
import java.awt.image.ImageObserver
import annotation.target.getter

/**
 * Shouldn't be used unless you know what you do. If you don't, use [[Substance]].
 * Base substance class.
 * Substances are basics of game. They are everything what is displayed.
 * Implements custom behaviour, can interact.
 * There are few implementations/preprepered Traits having "Substance" in name.
 * Extensible with mutators.
 * This is so abstract because it's ancestor to mutators.
 * @author Ivyl
 */
trait BaseSubstance {

  /** x position */
  @getter
  var x: Double

  /** y position */
  @getter
  var y: Double

  /** angle in degrees */
  @getter
  var angle: Double

  var name: String

  /** image of this substance */
  var image: Option[Image]

  /**
   * Implements drawing on screen.
   * Used internally by drawing.
   * Drawing implementation in substance (not mutators).
   *
   * @param g         graphics on which this object will be pasted
   * @param observer  imageObserver which will be notified
   */
  protected def paint(g: Graphics2D, observer: ImageObserver)

  /**
   * Draws this substance on graphics and notifies observer (thread-safe).
   * protected pain implements it's behaviour
   *
   * @param g         graphics on which this object will be pasted
   * @param observer  imageObserver which will be notified
   */
  def draw(g: Graphics2D, observer: ImageObserver) {
    synchronized {
      paint(g, observer)
    }
  }

  /**
   * Implements behaviour of this substance. Should be "steppable".
   * Will be invoked each time framework needs to update state.
   */
  protected def behavior

  /**
   * Makes next step in behaviour.
   * Thread-save method that should be used to make another
   */
  def behave {
    synchronized {
      internalBehavior
    }
  }

  /**
   * Chaining behavior defined by programmer.
   * Used by mutators.
   */
  protected def internalBehavior
}