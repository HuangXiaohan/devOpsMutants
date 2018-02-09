package raw;


import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PotterDicsountCalculatorTest {

    private PotterDiscountCalculator calculator;

    @Before
    public void initContext() {
        calculator = new PotterDiscountCalculator();
    }


    @Test
    public void emptyOrders() {
        assertEquals(0.0, calculator.calculatePrice(build()), 0.01);
        assertEquals(0.0, calculator.calculatePrice(null), 0.01);
    }

    @Test
    public void basicScenarios() {
        // scalar order
        assertEquals(8.00, calculator.calculatePrice(build(1)), 0.01);
        assertEquals(8.00, calculator.calculatePrice(build(2)), 0.01);
        assertEquals(8.00, calculator.calculatePrice(build(3)), 0.01);
        assertEquals(8.00, calculator.calculatePrice(build(4)), 0.01);
        assertEquals(8.00, calculator.calculatePrice(build(5)), 0.01);

        // multiple order without discount
        assertEquals(16.00, calculator.calculatePrice(build(1, 1)),    0.01);
        assertEquals(24.00, calculator.calculatePrice(build(2, 2, 2)), 0.01);
    }


    @Test
    public void simpleDiscounts() {
        assertEquals(15.20, calculator.calculatePrice(build(1, 2)),          0.01);
        assertEquals(21.60, calculator.calculatePrice(build(1, 3, 5)),       0.01);
        assertEquals(25.60, calculator.calculatePrice(build(1, 2, 3, 5)),    0.01);
        assertEquals(30.00, calculator.calculatePrice(build(1, 2, 3, 4, 5)), 0.01);
    }

    @Test
    public void severalDiscounts() {
        assertEquals(23.20, calculator.calculatePrice(build(1, 1, 2)), 0.01);
        assertEquals(30.40, calculator.calculatePrice(build(1, 1, 2, 2)), 0.01);
        assertEquals(40.80, calculator.calculatePrice(build(1, 1, 2, 3, 3, 4)), 0.01);
        assertEquals(38.00, calculator.calculatePrice(build(1, 2, 2, 3, 4, 5)), 0.01);
    }

    @Test
    public void edgeCase() {
        assertEquals( 51.20,
                calculator.calculatePrice(build(1, 1, 2, 2, 3, 3, 4, 5)), 0.01);
        assertEquals(141.20, calculator.calculatePrice(
                build(  1, 1, 1, 1, 1,
                        2, 2, 2, 2, 2,
                        3, 3, 3, 3,
                        4, 4, 4, 4, 4,
                        5, 5, 5, 5     )), 0.01);
    }


    // Private Helper to transform easily a set of int parameters into a list of Integers
    private List<Integer> build(Integer... books) {
        return Arrays.asList(books);
    }

}