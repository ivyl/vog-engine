package vog.config

import java.awt.image.BufferedImage
import vog.sound.SoundSample
import vog.cache.{SoundCache, Cache, RotationCache, ImageCache}


/**
 * @author Ivyl
 */
object CacheManager {
  var image: RotationCache[BufferedImage] = new ImageCache
  var sound: Cache[SoundSample] = new SoundCache
}