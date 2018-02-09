package devops.five.mutators;

import devops.five.utils.ParametrizedMutatorA;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

/**
 * Created by huang on 01/03/17.
 */
public class NotEqualOperatorMutator extends ParametrizedMutatorA<CtElement> {
    public NotEqualOperatorMutator() {
        super("notEqualOperator");
    }

    @Override
    protected boolean matchTheScope(CtElement candidate) {
        if(!(candidate instanceof CtBinaryOperator)) {
            return false;
        }
        BinaryOperatorKind kind = ((CtBinaryOperator) candidate).getKind();

        return kind.equals(BinaryOperatorKind.EQ) || kind.equals(BinaryOperatorKind.NE);
    }

    public void process(CtElement ctElement) {
        if (((CtBinaryOperator) ctElement).getKind() == BinaryOperatorKind.EQ) {
            ((CtBinaryOperator) ctElement).setKind(BinaryOperatorKind.NE);
        }
        else if (((CtBinaryOperator) ctElement).getKind() == BinaryOperatorKind.NE) {
            ((CtBinaryOperator) ctElement).setKind(BinaryOperatorKind.EQ);
        }
    }
}
