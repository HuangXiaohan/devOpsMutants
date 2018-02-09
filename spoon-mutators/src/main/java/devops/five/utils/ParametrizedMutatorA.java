package devops.five.utils;

import java.util.Random;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.declaration.CtElement;

/**
 * Pattern for parametrized mutators
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public abstract class ParametrizedMutatorA<T extends CtElement> extends AbstractProcessor<T> {
  private double percentage;
  private static final double DEFAULT_PERCENTAGE = 1.0;
  private static final String FILE_NAME = "percentages.properties";

  protected abstract boolean matchTheScope(T candidate);

  public ParametrizedMutatorA() {
    percentage = DEFAULT_PERCENTAGE;
  }

  public ParametrizedMutatorA(String field) {
    InputStream inputStream;
    Properties properties = new Properties();

    try {
      inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
      properties.load(inputStream);

      String property = properties.getProperty(field);

      if (property == null) {
        percentage = DEFAULT_PERCENTAGE;
      } else {
        percentage = Double.parseDouble(property);
      }

    } catch (IOException e) {
      percentage = DEFAULT_PERCENTAGE;
    }
  }

  @Override
  public boolean isToBeProcessed(T candidate) {
    if (!matchTheScope(candidate)) {
      return false;
    }

    return Math.random() <= percentage;
  }
}
