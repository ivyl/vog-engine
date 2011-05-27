package test.cache

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import vog.cache.{ResourceCache, SoundCache}
import vog.sound.SoundSample

/**
 * @author Ivyl
 */
class SoundCacheSpec extends Spec with ShouldMatchers {

  describe("Sound cache") {

    //I believe that functionality is untestable without example files, so...

    val soundCache = new SoundCache
    it("should be resource cache handling sound samples") {
      soundCache.getClass.isAssignableFrom(classOf[ResourceCache[SoundSample]])
    }
  }
}
