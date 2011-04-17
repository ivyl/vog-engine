package cache

import scala.collection.mutable._
import config.Configuration
import java.io.{FileInputStream, InputStream, File}

object ResourceCache {
  var dataDir = Configuration.dataDirectory
}

/**
 *  General resource cache, providing partial implementation.
 *  Caching in [[scala.collection.mutable.HashMap].
 *  @tparm T      Stored element type.
 *  @author Ivyl
 */
trait ResourceCache[T] extends Cache[T] {
  protected var resources: Map[String, T] = new HashMap;

  protected val postfix: String
  protected val dir: String

  /**
   * Frees resources by removing reference to [[scala.collection.mutable.HashMap].
   * New HashMap is created.
   */
  def free {
    resources = new HashMap;
  }

  /**
   *  Handles loading resources.
   *  To implement.
   *  Invoked when not found in cache.
   *  @param file file to be loaded into cache.
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
  def retrieve(name: String): T = {
    if (resources contains name) {
      resources.get(name).get
    } else {
      val file = resourceFile(name)
      val resource = loadResource(file).get

      resources.put(name, resource)
      resource
    }
  }

  /**
   *  Returns InputStream to non-cached file in file system.
   *  FileInputStream in this case.
   *  @param name name of resource to be loaded
   *  @return input stream representing resource
   */
  def resourceInputStream(name: String): InputStream = {
    new FileInputStream(resourceFile(name))
  }

  /**
   *  Returns file representing resource.
   *  Instead of loading/retrieving resource.
   *  @param  name resource name
   *  @return file representing resource
   */
  protected def resourceFile(name: String) = {
    new File(ResourceCache.dataDir+dir+name+postfix)
  }


}