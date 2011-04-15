package vog

import cache.sound.SoundPlayer


/**
 * @author Ivyl
 */
object Main {

  def main(argv: Array[String]) {
    val soundCache = new cache.SoundCache
    SoundPlayer(8)
    val soundSample = soundCache.retrieve("bla")
    println(soundSample.chunks.size)
    SoundPlayer ! soundSample
    Thread.sleep(500)
    SoundPlayer ! soundSample
  }

}