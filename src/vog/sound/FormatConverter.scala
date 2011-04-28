package sound

import config.Configuration
import javax.sound.sampled.AudioFormat


/**
 *  Converter for storing and playing purposes.
 *  @author Ivyl
 */
object FormatConverter {

  /**
   *  Sample file in stored PCM
   *  audio.sampleSize in configuration file
   *  @default 16
   */
  val sampleSize = Configuration.config.getInt("audio.sampleSize", 16)

  /**
   *  Converts given audio format to PCM, for storing purposes.
   *  @param sourceFormat source format, some properties will be held
   *  @return             resulting PCM format
   */
  def convertFormat(sourceFormat: AudioFormat) = {
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