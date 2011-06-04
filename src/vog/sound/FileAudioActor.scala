package vog.sound

import actors.Actor
import javax.sound.sampled._
import net.lag.logging.Logger
import java.io.{File, IOException}

/**
 *  Plays music concurrently from files.
 *  Actor.
 *  Starts playing when started.
 *  @author Ivyl
 */
class FileAudioActor(private val file: File) extends Actor {

  private var end = false

  /** Stops playback */
  def stop() { end = true }

  def act() {
    //assuming that AudioActors are quite busy
    var audioInputStream = AudioSystem.getAudioInputStream(file)

    try {
      val format = AudioSystem.getAudioFileFormat(audioInputStream).getFormat
      val buffer = new Array[Byte](SoundPlayer.bufferSize)

      var continue = true
      var read = 0


      SoundPlayer.play(format) { (dataLine, format) =>
        audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)

        read = audioInputStream.read(buffer)
        while (read != -1 && continue) {
          dataLine.write(buffer, 0, read)
          read = audioInputStream.read(buffer)

          if (end) {
            continue = false
            dataLine.flush()
          }
        }
      }

    } catch {
      case e: IOException => Logger.get.warning(e, "Sound operate on given stream" )
    } finally {
      audioInputStream.close()
    }
  }
}
