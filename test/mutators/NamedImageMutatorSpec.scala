package test.mutators

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import vog.substance.Substance
import vog.cache.Image
import vog.substance.mutators.NamedImageMutator

/**
 * @author Ivyl
 */
class NamedImageMutatorSpec extends Spec with ShouldMatchers {

  describe("NamedImageMutator") {
    //minimal test substance
    class TestSubstance extends Substance with NamedImageMutator {
      var image: Option[Image] = None
      protected def behavior = {}
    }

    val substance = new TestSubstance

    it("should provide image name") {
      substance.imageName.getClass should equal (classOf[String])
    }

    it("should be assignable") {
      substance.imageName = "test"
    }
  }
}
