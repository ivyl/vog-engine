package vog

import sound.SoundManager


/**
 * @author Ivyl
 */
object Main {

  def main(argv: Array[String]) {
    val soundCache = new cache.SoundCache
    SoundManager(8)
    SoundManager.playFile(soundCache.resourceFile("bla"))
  }

}