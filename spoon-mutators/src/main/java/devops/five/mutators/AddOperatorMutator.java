package devops.five.mutators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

/**
 * @author Maxime
 */
public class AddOperatorMutator extends AbstractProcessor<CtElement> {

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        if(candidate instanceof CtBinaryOperator)
            if(((CtBinaryOperator) candidate).getKind().equals(BinaryOperatorKind.PLUS)) {
                return true;
            }
        return false;
    }

    public void process(CtElement ctElement) {
        CtBinaryOperator ctBinaryOperator = (CtBinaryOperator) ctElement;
        ctBinaryOperator.setKind(BinaryOperatorKind.MUL);
    }
}
