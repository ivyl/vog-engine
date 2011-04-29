package test.cache

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import vog.cache.ResourceCache


/**
 * @author Ivyl
 */
class ResourceCacheSpec extends Spec with ShouldMatchers {

  describe("ResourceCache") {

    describe("when gets resource") {
      val obj = 10

      val cache = new ResourceCache[Int] {
        val dir = ""
        val postfix = ""
        override protected def loadResource(file: File) = Some(obj)
      }

      it("should return it") {
        cache.retrieve("") should equal (Some(obj))
      }
    }


    describe("when resource is None") {
      val cache = new ResourceCache[Int] {
        val dir = ""
        val postfix = ""
        override protected def loadResource(file: File) = None
      }

      it("should get None exception when file not avaible") {
        cache.retrieve("aaa") should equal (None)
      }
    }

    it("should build proper path") {
      val directory = "dir/"
      val post = ".post"
      val name = "name"

      val cache = new ResourceCache[Int] {
        val dir = directory
        val postfix = post
        override protected def loadResource(file: File) = None
      }

      val path = cache.resourceFile(name).getPath

      path should include (name)
      path should include (post)
      path should include (directory.stripSuffix("/"))
      path should include (ResourceCache.dataDir.stripSuffix("/"))
    }

  }
}