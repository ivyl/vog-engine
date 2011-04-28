package cache

import java.io._
import net.lag.logging.Logger
import config.Configuration
import javax.sound.sampled.AudioSystem
import sound.{FormatConverter, SoundSample}

/**
 *  Sound caching class.
 *  Loads files under audio/ *.ogg (default).
 *  *.ogg may contain subdirectories.
 *  Caches everything you have SPI libraries for.
 *  @author Ivyl
 */
class SoundCache extends ResourceCache[SoundSample] {

  /**
   *  Directory used to construct path for audio file.
   *  Formula = dir + name + postfix
   *  audio.dir in configuration file
   *  @default audio/
   */
  val dir = Configuration.config.getString("audio.dir", "audio/")

  /**
   *  Postfix used to construct path for audio file.
   *  Formula = dir + name + postfix
   *  audio.postfix in configuration file
   * @default .ogg
   */
  val postfix = Configuration.config.getString("audio.postfix", ".ogg")

  /**
   *  Data is stored in chunks, this represent single chunk size in bytes.
   *  audio.chunkSize in configuration file
   *  @default 3000
   */
  val chunkSize = Configuration.config.getInt("audio.chunkSize", 3000)

  protected def loadResource(file: File) = {
    var soundSample: Option[SoundSample] = None
    var inputStream = AudioSystem.getAudioInputStream(file)

    try {
      val oldFormat = AudioSystem.getAudioFileFormat(inputStream)
      val format = FormatConverter.convertFormat(oldFormat.getFormat)
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

      soundSample = Some(new SoundSample(chunks, format, chunkSize, lastChunkSize))

    } catch {
      case e: IOException => Logger.get.warning(e, "Sound cache couldn't find/load " + file.getAbsolutePath)
    } finally {
      inputStream.close
    }

    soundSample
  }

}
