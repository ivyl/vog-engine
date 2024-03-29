package vog.sound

import actors.Actor
import collection.mutable.Queue
import net.lag.logging.Logger
import vog.config.Configuration
import annotation.target.getter
import java.io.File

/**
 *  Simple low-latency vog.sound player management class.
 *  Needs to be started (apply does this).
 *
 *  Playing by sending SoundSample instance, no response.
 *
 *  Should be started only once, otherwise actors accumulate.
 *  @author Ivyl
 */
object SoundManager extends Actor {

  /**
   *  Number of workers created when staring new manager.
   *  audio.poolSize in configuration file
   */
  @getter
  var poolSize = Configuration.config.getInt("audio.poolSize", 8)
  private val actorsQueue = new Queue[Actor]()

  /**
   *  Starting player with given number of playing actors.
   *  No need to execute start with this.
   *  @param poolSize number of playing actors to initialize with.
   */
  def apply(poolSize: Int) {
    this.poolSize = poolSize
    start()
  }

  /** Start actor with default pool size. */
  def apply() {
    start()
  }

  /**
   *  Play in background from file. Starts playback.
   *  See FileAudioActor documentation.
   *  @return FileAudioActor
   */
  def playFile(file: File) = (new FileAudioActor(file)).start()

  protected def initialize() {
    for (i <- 0 until poolSize) {
      actorsQueue enqueue (new AudioActor).start
    }
  }

  def act() {
    initialize()
    while(true){
      receive {
        case sample: SoundSample  => playOnFirstAvailable(sample)
        case actor:  AudioActor   => actorsQueue enqueue actor
      }
    }
  }

  /** Dequeuing first free AudioActor and ordering him to play. */
  private def playOnFirstAvailable(sample: SoundSample) {
    if (!actorsQueue.isEmpty) {
      actorsQueue.dequeue ! sample
    }
  }

}
