package vog.config

import vog.sound.SoundSample
import vog.cache._

/**
 * Provides image and sound cache instances.
 * Initialized using default implementations.
 * @author Ivyl
 */
object CacheManager {
  var image: RotationCache[Image] = new ImageCache
  var sound: Cache[SoundSample] = new SoundCache
}
