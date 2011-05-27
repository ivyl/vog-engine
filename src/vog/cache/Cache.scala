package vog.cache

import java.io.File

/**
 * General fully abstract trait representing vog.cache.
 * @author Ivyl
 */
trait Cache[T] {

  /**
   *  Returns resource.
   *  If not cached it should be cached first.
   *  @param  name     name of resource to be loaded
   *  @return resource
   */
  @throws(classOf[NoSuchElementException])
  def retrieve(name: String): Option[T]

  /**
   *  Returns file representing resource.
   *  Instead of loading/retrieving resource.
   *  Should be used by descendant classes to access file.
   *  @param  name resource name
   *  @return file representing resource
   */
  def resourceFile(name: String): File

  /**
   *  Frees resources.
   *  Cached files are removed from memory (may be via GarbageCollector).
   *  Prepares vog.cache for loading resources.
   */
  def free()

}