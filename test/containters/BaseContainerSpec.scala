package test.containters

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import vog.substance.Substance
import vog.substance.containers.{NoLayerException, BaseContainer}
import java.awt.image.ImageObserver
import swing.Graphics2D
import java.util.Random
import vog.cache.Image

/**
 * @author Ivyl
 */
class BaseContainerSpec extends Spec with ShouldMatchers with MockitoSugar {
  describe("Base Container when adding substance") {
    val container = new BaseContainer
    val substance = mock[Substance]
    val layerName = "first";

    it("should go right") {
      container.addSubstance(layerName, substance)
    }

    it("should be not be invoked because it isn't in order") {
      container.foreachOrdered(_.behave())
      verifyZeroInteractions(substance)
    }

    it("should be used when in layer is included in order") {
      container.order = container.order ::: List(layerName)
      container.foreachOrdered(_.behave())
      verify(substance).behave()
    }

    var subs = List[Substance]()

    it("should do this for all elements on all layers") {

      for(i <- 1 to 10) {
        val layerName = "layer" + i

        container.order = layerName :: container.order

        for(j <- 1 to 10) {
          val sub = mock[Substance]
          subs = sub :: subs
          container.addSubstance(layerName, sub)
        }
      }

      container.foreachOrdered(_.behave())

      subs.foreach(verify(_).behave())
    }

    it("should draw all") {
      val graphics = mock[Graphics2D]
      val observer = mock[ImageObserver]
      container.drawAll(graphics, observer)

      subs.foreach(verify(_).draw(graphics, observer))
    }

    it("should behave all") {
      container.behaveAll()

      subs.foreach(verify(_, times(2)).behave()) //from previous
    }

      it("should remove all marked as dead") {
      class TestSubstance extends Substance {
        protected def behavior() {}
        var image: Option[Image] = None
      }

      val rand = new Random

      for (i <- 1 to 100) {
        val layerName = "testu" + i

        container.order = layerName :: container.order

        for (j <- 1 to 100) {
          val substance = new TestSubstance

          if (rand.nextBoolean) { substance.die() }

          container.addSubstance(layerName, substance)
        }
      }

      container.removeDead()

      container.foreachOrdered(_.isDead should be (false))

    }

    it("should thorw exception when non-existing layer is ordered") {
      container.order = "foo" :: container.order

      evaluating { container.foreachOrdered(_.behave()) } should produce [NoLayerException]
    }
  }

}
