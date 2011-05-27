package test.substance

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import vog.substance.Substance
import swing.{Graphics2D}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import vog.cache.Image
import java.awt.image.{BufferedImage, ImageObserver}

/**
 * @author Ivyl
 */
class SubstanceSpec extends Spec with ShouldMatchers with MockitoSugar {

  describe("Substance") {

    describe("working methods") {
      class TestSubstance extends Substance {
        var image: Option[Image] = None
        var run = false
        var painted = false

        protected def behavior { run = true }

        override protected def paint(g: Graphics2D, observer: ImageObserver) {
          super.paint(g, observer)
          painted = true
        }
      }

      val substance = new TestSubstance

      it("should invoke behavior on every behave") {
        substance.behave
        substance.run should be (true)
      }

      it("should invoke paint when drawing") {
        val graphics = mock[Graphics2D]
        val observer = mock[ImageObserver]

        substance.draw(graphics, observer)

        substance.painted should be (true)
      }

      it("should not paint on graphics when drawing and image is not set") {
        val graphics = mock[Graphics2D]
        val observer = mock[ImageObserver]

        substance.draw(graphics, observer)

        verifyZeroInteractions(graphics)
      }

      it("should paint on graphics when drawing and image is set") {
        val graphics = mock[Graphics2D]
        val observer = mock[ImageObserver]
        val image = new Image(mock[BufferedImage])

        substance.image = Some(image)
        substance.x = 343
        substance.y = 21

        substance.draw(graphics, observer)

        verify(graphics).drawImage(image.image, substance.x.toInt, substance.y.toInt, observer)
      }

      describe("thread safety") {
        pending
      }

    }
  }
}
