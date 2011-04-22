package substance.mutators

/**
 * Adds name and image name.
 * imageName should be used for retrieval
 * @author Ivyl
 */
trait NamedMutator extends Mutator {
  /**
   * Name of Substance.
   */
  var name: String

  /**
   * Name of image to retrieve.
   * Method of retreival should be implemented in other mutator.
   * This should be created somehow using {{{name}}.
   */
  var imageName: String

}