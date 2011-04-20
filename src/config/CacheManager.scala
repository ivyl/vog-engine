package config

import java.awt.image.BufferedImage
import sound.SoundSample
import cache.{SoundCache, Cache, RotationCache, ImageCache}


/**
 * @author Ivyl
 */
object CacheManager {
  var image: RotationCache[BufferedImage] = new ImageCache
  var sound: Cache[SoundSample] = new SoundCache
}