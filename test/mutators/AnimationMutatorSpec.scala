package test.mutators

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import vog.substance.Substance
import vog.substance.mutators.AnimationMutator
import vog.cache.Image

/**
 * @author Ivyl
 */
class AnimationMutatorSpec extends Spec with ShouldMatchers {

  describe("AnimationMutator") {
    //minimal test substance
    class TestSubstance extends Substance with AnimationMutator {
      var image: Option[Image] = None
      protected def behavior { }
    }

    val substance = new TestSubstance

    val frameDelay = 5
    val maxFrames = 7

    describe("when messing with animation") {
      substance.frameDelay = frameDelay
      substance.maxFrames = maxFrames

      it("should have woking animation") {
        substance.startAnimation

        for (i <- 0 until 2*maxFrames) {
          substance.imageName should endWith ((i%maxFrames).toString)
          substance.imageName should startWith (substance.name)
          for(j <- 1 to frameDelay) {
            substance.behave
          }
        }
      }

      it("should stop animation") {
        substance.stopAnimation
        for (i <- 0 until 2*maxFrames*frameDelay) {
          substance.behave
          substance.imageName should equal (substance.name)
        }

      }
    }

    describe("when messing with events") {
      val eventTime = 5
      val eventDelay = 2
      it("should handle events") {
        substance.startAnimation
        substance.eventAnimation("foobar", eventTime, eventDelay)

        for (i <- 0 until (eventTime)*eventDelay) {
          substance.imageName should endWith ((i/eventDelay).toString)
          substance.imageName should include ("foobar")
          substance.behave
        }
      }

      it("should stop processing event") {
        substance.imageName should not include ("foobar")
        substance.behave
        substance.imageName should not include ("foobar")
      }

      it("should not process event when stopped") {
        substance.stopAnimation
        substance.eventAnimation("foobar", eventTime, eventDelay)
        substance.behave
        substance.behave
        substance.imageName should equal (substance.name)
      }
    }
  }
}