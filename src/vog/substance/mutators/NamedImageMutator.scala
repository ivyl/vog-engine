package vog.substance.mutators

/**
 * Adds name and image name.
 * imageName should be used for retrieval
 * @author Ivyl
 */
trait NamedImageMutator extends Mutator {
  /**
   * Name of image to retrieve.
   * Method of retrieval should be implemented in other mutator.
   * This should be created somehow using {{{name}}.
   */
  var imageName: String = "default"

}
