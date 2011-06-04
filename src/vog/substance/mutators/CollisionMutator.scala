package vog.substance.mutators

import java.awt.Rectangle

/**
 * Adds ability to collide and react to collision.
 * @author Ivyl
 */
trait CollisionMutator extends Mutator {

  protected var collisionHandlers = List[CollisionMutator => Unit]()

  def collided(that: CollisionMutator) {
    collisionHandlers.foreach { handler =>
      try {
      handler(that)
      } catch {
        case ex: MatchError => true
      }
    }
  }

  /**
   * Adds collision handler for specified class. Class type must be given separately due to type erasure in Scala.
   * All handler are invoked (they are added to list).
   * No exceptions are thrown, but you should handle other cased (if not handling everything) {{{ match _ => }}}
   * because of performance costs.
   * Usage:
   * {{{ container.addCollisionHandler { case sub: OurSubsance => subs.die() } }}}
   * @param klazz class to handle collision with.
   * @param handler function that handles collision. It should do pattern matching as shown in description.
   */
  def addCollisionHandler(handler: Any => Unit) {
    collisionHandlers = handler :: collisionHandlers
  }

  /**
   * Checks for collision with other substance.
   * for pixel perfect collision see pixelPerfectCollison()
   */
  def collides(that: CollisionMutator): Boolean = {
    if (this.image.isDefined && that.image.isDefined) {

      val thisRectangle = new Rectangle(this.x.toInt, this.y.toInt, this.image.get.width, this.image.get.height)
      val thatRectangle = new Rectangle(that.x.toInt, that.y.toInt, that.image.get.width, that.image.get.height)

      if (thisRectangle.intersects(thatRectangle)) {

        val thisBitmask = this.image.get.alphaBitmask
        val thatBitmask = that.image.get.alphaBitmask

        val deltaX = this.x - that.x
        val deltaY = this.y - that.y

        return true
      }
    }
    false
  }

//  def pixelPerfectCollision(): Boolean = {
//    if (deltaX > 0) {
//      if (deltaY > 0) {
//
//      } else {
//
//      }
//    } else {
//      if (deltaY > 0) {
//
//      } else {
//
//      }
//    }
//
//    for (x <- 1 to 20) {
//      for (y <- 1 to 30) {
//            if ((thisBitmask(x)(y) << thisBitshift) & (thatBitmask(x)(y+deltaY) << thatBitshift) != 0
//              ) {
//              return true
//            }
//      }
//    }
//
//
//    false
//  }
//
}
