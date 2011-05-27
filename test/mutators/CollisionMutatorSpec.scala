package test.mutators

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import vog.substance.Substance
import vog.substance.mutators.CollisionMutator
import vog.cache.Image

/**
 * @author Ivyl
 */
class CollisionMutatorSpec extends Spec with ShouldMatchers with MockitoSugar {
  describe("Collision Mutator") {
    class MutatedSubstance extends Substance with CollisionMutator {
      protected def behavior() {}
      var image: Option[Image] = None
      var tested = false
      def test() {tested = true}
    }

    val mutated = new Substance with CollisionMutator {
      var image: Option[Image] = None
      protected def behavior() {}

    }
      describe("handlers") {
        it("should be addable") {
          mutated.addCollisionHandler{substance => "" }
        }

        it("should be handled") {
          mutated.addCollisionHandler { case x: MutatedSubstance => x.test() }
          val substance = new MutatedSubstance
          mutated.collided(substance)
          substance.tested should be (true)
        }

        class OtherSubstance extends Substance with CollisionMutator {
          protected def behavior() { }
          var image: Option[Image] = None
        }

        it("should not thorw exception when match not found") {
          mutated.collided(new OtherSubstance)
        }

        it("should pass other exceptions") {
          mutated.addCollisionHandler { case x: OtherSubstance => throw new Exception }
          evaluating(mutated.collided(new OtherSubstance)) should produce [Exception]
        }
    }

    describe("collision") {
      it("should be detectend when collides") {
        pending
      }

      it("should not be detected when doesn't colide") {
        pending
      }
    }
  }
}