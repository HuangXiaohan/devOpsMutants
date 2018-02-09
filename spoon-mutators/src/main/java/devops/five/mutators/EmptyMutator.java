package devops.five.mutators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import devops.five.utils.ParametrizedMutatorA;

/**
 * This Mutator does absolutly nothing, but it is required because otherwise when we compare the source folder (the one
 * we apply our mutant on) with the mutant project created, we will have a lot of difference that aren't related to the
 * mutant itself. This is because spoon rename all class name to fully classified one. For example : List becomes
 * java.lang.List. So by applying an empty mutator, we have a reference project with fully classified name that we can
 * use to compare our mutant with
 * @author Maxime
 */
public class EmptyMutator extends ParametrizedMutatorA<CtElement> {
  @Override
  protected boolean matchTheScope(CtElement candidate) {
    return true;
  }

  public void process(CtElement ctElement) {
    // DO nothing
  }
}
