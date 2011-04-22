package substance.mutators

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
 * @author Ivyl
 */
trait AnimationMutator extends NamedMutator {

  protected var animationRunning = false

  /** constructed using AnimationMutator, should not be changed. */
  var imageName = name + "_" + currentFrameNumber

  /** number of frames we cycle through. Counting from 0. */
  var maxFrames = 0

  /** specifies how many times behave is used before invoked before we change frame. */
  var frameDelay = 0

  protected var currentFrameNumber = 0
  /** Used internally to count behave invokes. */
  protected var animationCounter = 0

  //analogical to above
  protected var event: Option[Symbol] = None
  protected var eventTime = 0
  protected var eventDelay = 0
  protected var eventCurrentFrame = 0
  protected var eventCounter = 0

  protected abstract override def behavior {
    if (event.isDefined) {
      handleEvent
    } else if (animationRunning) {
      animate
    }

    super.behavior
  }

  protected def handleEvent {
    eventCounter = eventCounter + 1

    if (eventCurrentFrame >= eventTime) {
      eventCurrentFrame = 0
      eventCounter = 0
      eventTime = 1 //to stop this if
      event = None
    } else if (eventCounter >= eventDelay)  {
      eventCurrentFrame += 1
      imageName = name + "_" + event.get + "_" + eventCurrentFrame
      eventCounter = 0
    }
  }

  protected def animate {
    animationCounter = animationCounter + 1

    if (animationCounter >= frameDelay) {
      currentFrameNumber = (currentFrameNumber + 1) % maxFrames
      animationCounter = 0
      imageName = name + "_" + currentFrameNumber
    }
  }

  def stopAnimation {
    animationRunning = false
  }

  def startAnimation {
    animationRunning = true
  }
  /**
   * Starts new event.
   * name of resource displayed is name_event_frame
   * Should be used only for events that alter substance look.
   * Use something else for simple overlays.
   *
   * @param event name
   * @param time  number of frames, counting starts form 0
   * @param delay number of {{{behave}}} between subsequent frames.
   */
  def eventAnimation(event: Symbol, time: Int, delay: Int) {
    this.event = Some(event)
    this.eventTime = time
    this.eventDelay = delay
  }


}