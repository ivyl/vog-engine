package vog.substance.mutators

/**
 * Makes substance animated.
 * Uses resources named name_frame.
 * You can start and stop animation. When stopped using simple name.
 * Animation is cyclic. Configure speed using {{{frameDelay}}.
 * Should be used to animation such as walking.
 * Handling animation that does not alter substance basic look (overlays) done by creating substance with it.
 *
 * Count frames from 0.
 *
 * By default animation is stopped.
 *
 * @author Ivyl
 */
trait AnimationMutator extends NamedImageMutator {

  protected var animationRunning = false

  /** number of frames we cycle through. Counting from 0. */
  var maxFrames = 0

  /** specifies how many times behave is used before invoked before we change frame. */
  var frameDelay = 0

  protected var currentFrameNumber = 0
  /** Used internally to count behave invokes. */
  protected var animationCounter = 0

  imageName = name

  //analogical to above
  protected var event: Option[String] = None
  protected var eventTime = 0
  protected var eventDelay = 0
  protected var eventCurrentFrame = 0
  protected var eventCounter = 0

  protected abstract override def internalBehavior {
    if (animationRunning) {
      if (event.isDefined) {
        handleEvent
      } else {
        animate
      }
    }
    super.internalBehavior
  }

  protected def handleEvent {
    eventCounter = eventCounter + 1

    if (eventCounter >= eventDelay)  {
      eventCurrentFrame += 1
      eventCounter = 0
      imageName = buildImageName
    }

    if (eventCurrentFrame >= eventTime) {
      eventCurrentFrame = 0
      eventCounter = 0
      //eventTime = 1 //to stop this if
      event = None
      imageName = buildImageName
    }
  }

  protected def animate {
    animationCounter = animationCounter + 1

    if (animationCounter >= frameDelay) {
      currentFrameNumber = (currentFrameNumber + 1) % maxFrames
      animationCounter = 0
      imageName = buildImageName
    }
  }

  /** from next behave animation will be stopped */
  def stopAnimation = synchronized {
    imageName = name
    animationRunning = false
  }

  /** from next behave animation will be running */
  def startAnimation = synchronized {
    imageName = buildImageName
    animationRunning = true
  }

  /**
   * Starts new event.
   * name of resource displayed is name_event_frame
   * Should be used only for events that alter substance look.
   * Use something else for simple overlays.
   * Event would not run if animation is stopped.
   *
   * @param event name
   * @param time  number of frames, counting starts form 0
   * @param delay number of {{{behave}}} between subsequent frames.
   */
  def eventAnimation(event: String, time: Int, delay: Int) = synchronized {
    this.event = Some(event)
    this.eventTime = time
    this.eventDelay = delay
    this.eventCounter = 0
    this.eventCurrentFrame = 0
    if (animationRunning) imageName = buildImageName
  }

  def isEventRunning = event.isDefined

  protected def buildImageName = {
    if (event.isDefined) {
      name + "_" + event.get + "_" + eventCurrentFrame
    } else {
      name + "_" + currentFrameNumber
    }
  }

}