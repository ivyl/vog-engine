package vog.substance.mutators

import java.awt.Rectangle

/**
 * Adds ability to collide and react to collision.
 */
trait CollisionMutator extends Mutator {

  var collisionHandlers = Map[Class[_], CollisionMutator => Unit]()

  def collided(that: CollisionMutator) {
    collisionHandlers(that.getClass)(that)
  }

  /**
   * Adds collision handler for specified class. Class type must be given separately due to type erasure in Scala.
   * @param klazz class to handle collision with.
   * @handler function that handles collision.
   */
  def handleCollision[T <: CollisionMutator](klazz: Class[_])(handler: T => Unit) {
    val function = (obj: CollisionMutator) => handler(obj.asInstanceOf[T])
    collisionHandlers = collisionHandlers + ((klazz, function))
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