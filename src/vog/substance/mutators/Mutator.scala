package vog.substance.mutators

import vog.substance.BaseSubstance


/**
 * Substance decoration traits should be descendants of this.
 * For cleaner code.
 *
 * ==Overview==
 * To implement some behaviour do {{{protected abstract override}}} of behaviour or paint
 * (depending on what you want to change)
 * Overriding function you should call {{{super.function(params..)}}}
 * at the end (if mutator action happens before) of beginning (mutator after actual method)
 *
 * {{{
 * class MySubstance extends Substance with FirstMutator, SecondMutator, LastMutator {
 *   ...
 * }
 * }}}
 *
 * Mutators are called from last to first, and then our constructor. For details check scala documentation.
 *
 * @author Ivyl
 */
trait Mutator extends BaseSubstance