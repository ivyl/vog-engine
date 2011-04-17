package vog

import cache.sound.SoundPlayer


/**
 * @author Ivyl
 */
object Main {

  def main(argv: Array[String]) {
    val soundCache = new cache.SoundCache
    SoundPlayer(8)
    val soundSample = soundCache.resourceFile("bla")
    SoundPlayer.playFile(soundSample)
  }

}