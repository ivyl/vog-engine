package cache

import java.io.File
import scala.collection.mutable._
import config.Configuration
import java.util.NoSuchElementException


object ResourceCache {
  var dataDir = Configuration.dataDirectory

}

/** Abstract trait, which represents general resource cache
 *  @tparm T      Stored element type.
 *  @author Ivyl
 */
trait ResourceCache[T] {
  protected var resources: Map[String, T] = new HashMap;

  protected val postfix: String
  protected val dir: String

  /** Wipes collection, freeing resources.
   *  Should be used wisely.
   */
  def wipe {
    resources = new HashMap;
  }

  /** To override. Should handle loading and caching.
   *  @param file file to be loaded into cache.
   */
  protected def loadResource(file: File): Option[T];

  /** Retrieve resources.
   *  If not cache it is cached first.
   *  @param   file     File instance that represents resource to be loaded
   *  @returns resource
   */
  @throws(classOf[NoSuchElementException])
  def retrieve(name: String): T = {
    if (resources contains name) {
      resources.get(name).get
    } else {
      val file = new File(ResourceCache.dataDir+dir+name+postfix)
      val resource = loadResource(file).get

      resources.put(name, resource)
      resource
    }
  }
}