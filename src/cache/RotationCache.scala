package cache


/**
 * Extends cache with rotation.
 * Used for images.
 * @author Ivyl
 */
trait RotationCache[T] extends Cache[T] {
  def retrieveRotated(name: String, degree: Int): Option[T]
}