package sound

import actors.Actor

/**
 *  Plays music concurrently.
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

  private def play(sample: SoundSample) {
    var chunk: Array[Byte] = new Array[Byte](0)
    val iterator = sample.chunks.iterator

    SoundPlayer.play(sample.format) { (dataLine, format) =>

      while (iterator.hasNext) {
        chunk = iterator.next
        if (iterator.hasNext) {
         dataLine.write(chunk, 0, chunk.length)
       } else {
         dataLine.write(chunk, 0, sample.lastChunkSize)
       }
      }
    }

  }

}
