package vog.sound

import actors.Actor
import javax.sound.sampled._
import net.lag.logging.Logger
import java.io.{File, IOException}

/**
 *  Plays music concurrently from files.
 *  Actor.
 *  Stops playing when it receives 'exit .
 *  @author Ivyl
 */
class FileAudioActor(val file: File) extends Actor {

  def act() {
    //assuming that AudioActors are quite busy
    var audioInputStream = AudioSystem.getAudioInputStream(file)

    try {
      val format = AudioSystem.getAudioFileFormat(audioInputStream).getFormat
      val buffer = new Array[Byte](3000)

      var continue = true
      var read = 0


      SoundPlayer.play(format) { (dataLine, format) =>
        audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)

        read = audioInputStream.read(buffer)
        while (read != -1 && continue) {
          dataLine.write(buffer, 0, read)
          read = audioInputStream.read(buffer)

          /* TODO: is there better way to do this? */
          receiveWithin(0) {
            case 'exit => {
              continue = false
              dataLine.flush()
            }
            case _ => {}
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
