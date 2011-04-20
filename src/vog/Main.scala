package vog

import sound.SoundManager
import substance.Substance
import substance.mutators.CacheMutator


/**
 * @author Ivyl
 */
object Main {

  def main(argv: Array[String]) {
    val soundCache = new cache.SoundCache
    SoundManager(8)
    SoundManager.playFile(soundCache.resourceFile("bla"))

    (new Substance with CacheMutator {
      def behavior = {}

    }).draw(null, null)

  }

}