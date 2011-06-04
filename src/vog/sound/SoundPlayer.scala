package vog.sound

import vog.config.Configuration
import net.lag.logging.Logger
import javax.sound.sampled._

/**
 * Should not be used directly. See [[vog.sound.SoundManager]]
 * @author Ivyl
 */
object SoundPlayer {
  /**
   *  Represents buffers size.
   *  audio.bufferSize in configuration file
   *  @default 3200
   */
  val bufferSize = Configuration.config.getInt("audio.bufferSize", 320000)

  /**
   * Initializes [[vog.sound]] output, and after playing handles closing.
   * It does format conversion.
   * @param format input audio format
   * @param player function that gets data line and audio format it expects
   */
  def play(format: AudioFormat)(player: (SourceDataLine, AudioFormat) => Unit) {
    val newFormat = FormatConverter.convertFormat(format)
    val dataLine = AudioSystem.getSourceDataLine(newFormat)

    try {
      dataLine.open(newFormat, SoundPlayer.bufferSize)
      dataLine.start()

      player(dataLine, newFormat)

      dataLine.drain()
    } catch {
      case e: LineUnavailableException => Logger.get.warning(e, "Couldn't play some sound")
      case e: SecurityException => Logger.get.warning(e, "Couldn't close dataLine")
    } finally {
      dataLine.close()
    }
  }

}
