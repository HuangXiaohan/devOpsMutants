package devops.five.mutators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;
import devops.five.utils.ParametrizedMutatorA;

/**
 * Permutes '<' and '>'.
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public class ComparisonOperatorsMutator extends ParametrizedMutatorA<CtElement> {

  public ComparisonOperatorsMutator() {
    super("comparisonOperators");
  }
  
  @Override
  protected boolean matchTheScope(CtElement candidate) {
    if (!(candidate instanceof CtBinaryOperator)) {
      return false;
    }

    BinaryOperatorKind kind = ((CtBinaryOperator) candidate).getKind();

    return kind.equals(BinaryOperatorKind.GT) || kind.equals(BinaryOperatorKind.LT);
  }

  public void process(CtElement ctElement) {
    CtBinaryOperator cCtElement = (CtBinaryOperator) ctElement;
    BinaryOperatorKind kind = cCtElement.getKind();

    if (kind.equals(BinaryOperatorKind.GT)) {
      cCtElement.setKind(BinaryOperatorKind.LT);
    } else if (kind.equals(BinaryOperatorKind.LT)) {
      cCtElement.setKind(BinaryOperatorKind.GT);
    }
  }
}
