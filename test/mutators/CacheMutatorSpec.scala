package test.mutators

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import vog.substance.Substance
import vog.substance.mutators.CacheMutator
import vog.config.CacheManager
import vog.cache.{ImageCache, Image}
import swing.Graphics2D
import java.awt.image.{ImageObserver, BufferedImage}

/**
 * @author Ivyl
 */
class CacheMutatorSpec extends Spec with ShouldMatchers with MockitoSugar {

  describe("CacheImageMutator") {
    //minimal test substance
    class TestSubstance extends Substance with CacheMutator {
      protected def behavior = {}
    }

    val firstName = "default"
    val firstImage = new Image(new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR))

    val secondName = "bdsf3324esads"
    val secondImage = new Image(new BufferedImage(2,2,BufferedImage.TYPE_4BYTE_ABGR))

    val cache = mock[ImageCache]

    when(cache.retrieveRotated(firstName, 0)).thenReturn(Some(firstImage))
    when(cache.retrieveRotated(secondName, 0)).thenReturn(Some(secondImage))

    CacheManager.image = cache;

    val substance = new TestSubstance

    it("should load default image") {
      substance.image.get should equal (firstImage)
    }

    it("should load changed image when drawign") {
      substance.imageName = secondName
      substance.image.get should equal (firstImage)
      substance.draw(mock[Graphics2D], mock[ImageObserver])
      substance.image.get should equal (secondImage)
    }
  }
}
