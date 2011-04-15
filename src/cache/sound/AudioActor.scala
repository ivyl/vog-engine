package cache.sound

import actors.Actor
import javax.sound.sampled._
import net.lag.logging.Logger
import config.Configuration

object AudioActor {
  /** represents buffers size
   *  audio.bufferSize in configuration file
   */
  val bufferSize = Configuration.config.getInt("audio.bufferSize", 3200)
}

/** Class for playing music concurrently.
 *  Basing on actors.
 *  Expects to receive SoundSample.
 *  @author Ivyl
 */
class AudioActor extends Actor {

  def act() = {
    //assuming that AudioActors are quite busy
    loop {
      receive {
        case sample : SoundSample =>  {
          play(sample)
          reply(this) //resend to requeue
        }
      }
    }
  }

  private def play(sample : SoundSample) {
    try {
      val dataLine = AudioSystem.getSourceDataLine(sample.format)
      var chunk : Array[Byte] = new Array[Byte](0)
      dataLine.open(sample.format, AudioActor.bufferSize)
      dataLine.start

      val iterator = sample.chunks.iterator

      while (iterator.hasNext) {
        chunk = iterator.next
        if (iterator.hasNext) {
          dataLine.write(chunk, 0, chunk.length)
        } else {
          dataLine.write(chunk, 0, sample.lastChunkSize)
        }
      }

      dataLine.drain
      dataLine.close
    } catch {
      case e : LineUnavailableException => Logger.get.warning(e, "Couldn't play some sound")
      case e : SecurityException        => Logger.get.warning(e, "Couldn't close dataLine")
    }
  }

}
