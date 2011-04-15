package cache

import sound.SoundSample
import java.io._
import javax.sound.sampled.{AudioSystem, AudioFormat}
import net.lag.logging.Logger
import config.Configuration

object SoundCache {
  /** Sample file in stored PCM
   *  audio.sampleSize in configuration file
   *  @default 16
   */
  val sampleSize = Configuration.config.getInt("audio.sampleSize", 16)

  /** Converts given audio format to PCM, for storing purposes.
   *  @param sourceFormat source format, some properties will be held
   *  @return             resulting PCM format
   */
  def convertFormat(sourceFormat : AudioFormat)  = {
    new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        sourceFormat.getSampleRate(),
        sampleSize,
        sourceFormat.getChannels(),
        sourceFormat.getChannels() * (sampleSize / 8),
        sourceFormat.getSampleRate(),
        false)
  }
}

/** Sound caching class.
 *  Loads files under audio/ *.ogg (default).
 *  *.ogg may contain subdirectories.
 *  Caches everything you have SPI libraries for.
 *  @author Ivyl
 */
class SoundCache extends ResourceCache[SoundSample] {

  /** Directory used to construct path for audio file.
   *  Formula = dir + name + postfix
   *  audio.dir in configuration file
   *  @default audio/
   */
  val dir = Configuration.config.getString("audio.dir", "audio/")

  /** Postfix used to construct path for audio file.
   *  Formula = dir + name + postfix
   *  audio.postfix in configuration file
   * @default .ogg
   */
  val postfix = Configuration.config.getString("audio.postfix", ".ogg")

  /** File is stored in chunks, this represent single chunk size in bytes.
   *  audio.chunkSize in configuration file
   *  @default 3000
   */
  val chunkSize = Configuration.config.getInt("audio.chunkSize", 3000)

  protected def loadResource(file: File) = {
    var soundSample = new SoundSample(null, null, 0, 0)
    try {
      var inputStream = AudioSystem.getAudioInputStream(file)
      val oldFormat = AudioSystem.getAudioFileFormat(inputStream)
      val format = SoundCache.convertFormat(oldFormat.getFormat)
      inputStream = AudioSystem.getAudioInputStream(format, inputStream)

      var read = 0
      var lastChunkSize = 0

      var singleChunk = new Array[Byte](chunkSize)
      var chunks = List[Array[Byte]]()


      read = inputStream.read(singleChunk)

      while (read != -1) {
        chunks = chunks ::: List(singleChunk)
        singleChunk = new Array[Byte](chunkSize)
        lastChunkSize = read
        read = inputStream.read(singleChunk)
      }

      inputStream.close

      soundSample = new SoundSample(chunks, format, chunkSize, lastChunkSize)

    } catch {
      case e : IOException => Logger.get.warning(e, "Sound cache couldn't find/load " + file.getAbsolutePath)
    }

    soundSample
  }

}
