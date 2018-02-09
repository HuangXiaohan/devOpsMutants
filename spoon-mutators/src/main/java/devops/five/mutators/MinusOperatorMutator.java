package devops.five.mutators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.declaration.CtElement;
import devops.five.utils.ParametrizedMutatorA;

/**
 * Replaces '-' with '+'.
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public class MinusOperatorMutator extends ParametrizedMutatorA<CtElement> {

  public MinusOperatorMutator() {
    super("minusOperator");
  }

  @Override
  protected boolean matchTheScope(CtElement candidate) {
    if(candidate instanceof CtBinaryOperator) {
      return ((CtBinaryOperator) candidate).getKind().equals(BinaryOperatorKind.MINUS);
    }

    if (candidate instanceof CtUnaryOperator) {
      return ((CtUnaryOperator) candidate).getKind().equals(UnaryOperatorKind.POSTDEC)
          || ((CtUnaryOperator) candidate).getKind().equals(UnaryOperatorKind.PREDEC);
    }

    return false;
  }

  public void process(CtElement ctElement) {
    if (ctElement instanceof CtBinaryOperator) {
      ((CtBinaryOperator) ctElement).setKind(BinaryOperatorKind.PLUS);
    } else {
      CtUnaryOperator op = (CtUnaryOperator) ctElement;
      if (op.getKind().equals(UnaryOperatorKind.POSTDEC)) {
        op.setKind(UnaryOperatorKind.POSTINC);
      } else {
        op.setKind(UnaryOperatorKind.PREINC);
      }
    }

  }
}
