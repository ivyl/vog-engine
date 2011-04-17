package sound

import actors.Actor
import javax.sound.sampled._
import net.lag.logging.Logger
import config.Configuration
import java.io.{InputStream, IOException}

object FileAudioActor {
  /**
   *  Represents buffers size.
   *  audio.bufferSize in configuration file
   *  @default 3200
   */
  val bufferSize = Configuration.config.getInt("audio.bufferSize", 3200)
}

/**
 *  Plays music concurrently from files.
 *  Basing on actors.
 *  Stops playing when it receives 'exit .
 *  @author Ivyl
 */
class FileAudioActor(val inputStream: InputStream) extends Actor {

  def act() = {
    //assuming that AudioActors are quite busy
    var audioInputStream = AudioSystem.getAudioInputStream(inputStream)

    try {
      val oldFormat = AudioSystem.getAudioFileFormat(audioInputStream)
      val format = FormatConverter.convertFormat(oldFormat.getFormat)

      val dataLine = AudioSystem.getSourceDataLine(format)
      val buffer = new Array[Byte](3000)

      var continue = true
      var read = 0
      audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)

      dataLine.open(format, FileAudioActor.bufferSize)
      dataLine.start

      read = audioInputStream.read(buffer)
      while (read != -1 && continue) {
        dataLine.write(buffer, 0, read)
        read = audioInputStream.read(buffer)

        /* TODO: is there better way to do this? */
        receiveWithin(0) {
          case 'exit => {
            continue = false
            dataLine.flush
          }
          case _ => {}
        }
      }

      dataLine.drain
      dataLine.close

    } catch {
      case e: IOException => Logger.get.warning(e, "Sound operate on igven stream" )
      case e: LineUnavailableException => Logger.get.warning(e, "Couldn't play some sound")
      case e: SecurityException => Logger.get.warning(e, "Couldn't close dataLine")
    } finally {
      audioInputStream.close
    }
  }
}
