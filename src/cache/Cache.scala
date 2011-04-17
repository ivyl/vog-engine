package cache

import java.io.InputStream

/**
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
  def retrieve(name: String): T

  /**
   *  If it is possible, provides InputStream to non-cached file.
   *  Otherwise tries to wrap cached object in InputStream.
   *  @param name name of resource to be loaded
   *  @return input stream representing resource
   */
  def resourceInputStream(name: String): InputStream

  /**
   *  Frees resources.
   *  Cached files are removed from memory (may be via GarbageCollector).
   *  Prepares cache for loading resources.
   */
  def free

}