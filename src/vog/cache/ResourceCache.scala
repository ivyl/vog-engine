package vog.cache

import scala.collection.mutable._
import vog.config.Configuration
import java.io.File

object ResourceCache {
  var dataDir = Configuration.dataDirectory
}

/**
 *  General resource vog.cache, providing partial implementation.
 *  Caching in [[scala.collection.mutable.HashMap]].
 *
 *  postfix, dir needs to be specified
 *  loadResource needs to be implemented
 *
 *  building file path: dataDir + dir + name + postfix
 *
 *  @tparam T      Stored element type.
 *  @author Ivyl
 */
trait ResourceCache[T] extends Cache[T] {
  protected var resources: Map[String, T] = new HashMap;

  /** postfix used when building path */
  protected val postfix: String
  /** dir (prefix) used when building path */
  protected val dir: String

  /**
   * Frees resources by removing reference to [[scala.collection.mutable.HashMap]].
   * New HashMap is created.
   */
  def free() {
    resources = new HashMap;
  }

  /**
   *  Handles loading resources.
   *  To implement.
   *  Invoked when not found in vog.cache.
   *  @param file file to be loaded into vog.cache.
   */
  protected def loadResource(file: File): Option[T];

  /**
   *  Returns resource.
   *  Retrieves from HashMap.
   *  If not cached it loaded via loadResource first and then stored.
   *  @param  name     name of resource to be loaded
   *  @return resource
   */
  @throws(classOf[NoSuchElementException])
  def retrieve(name: String): Option[T] = {
    if (resources contains name) {
      resources.get(name)
    } else {
      val file = resourceFile(name)
      val resource = loadResource(file)
      if (resource.isDefined) resources.put(name, resource.get)
      resource
    }
  }

  /**
   *  Returns file representing resource.
   *  Instead of loading/retrieving resource.
   *  Should be used by descendant classes to access file.
   *  dataDir + dir + name + postfix
   *  @param  name resource name
   *  @return file representing resource
   */
  def resourceFile(name: String) = {
    new File(ResourceCache.dataDir+dir+name+postfix)
  }


}
