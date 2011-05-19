package test.containters

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import vog.substance.Substance
import vog.substance.containers.{NoLayerException, BaseContainer}
import java.awt.image.ImageObserver
import swing.Graphics2D

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
      container.onEachOrdered(_.behave)
      verifyZeroInteractions(substance)
    }

    it("should be used when in layer is included in order") {
      container.order = container.order ::: List(layerName)
      container.onEachOrdered(_.behave)
      verify(substance).behave
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

      container.onEachOrdered(_.behave)

      subs.foreach(verify(_).behave)
    }

    it("should draw all") {
      val graphics = mock[Graphics2D]
      val observer = mock[ImageObserver]
      container.drawAll(graphics, observer)

      subs.foreach(verify(_).draw(graphics, observer))
    }

    it("should behave all") {
      container.behaveAll()

      subs.foreach(verify(_, times(2)).behave) //from previous
    }


    it("should thorw exception when non-existing layer is ordered") {
      container.order = "foo" :: container.order

      evaluating { container.onEachOrdered(_.behave) } should produce [NoLayerException]
    }
  }

}