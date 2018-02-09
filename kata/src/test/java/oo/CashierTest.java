package oo;


import org.junit.*;
import static org.junit.Assert.*;

import static oo.Book.*;
import static oo.Cashier.PRICE;

public class CashierTest {

    private Cashier cashier;

    @Before
    public void initContext() {
        cashier = new Cashier();
    }


    @Test
    public void emptyOrders() {
        assertEquals(0.0, cashier.price(null), 0.01);
        Basket empty = new Basket();
        assertEquals(0.0, cashier.price(empty), 0.01);
    }

    @Test
    public void basicScenarios() {
        // scalar order
        assertEquals(PRICE, cashier.price(new Basket(BOOK1)), 0.01);
        assertEquals(PRICE, cashier.price(new Basket(BOOK2)), 0.01);
        assertEquals(PRICE, cashier.price(new Basket(BOOK3)), 0.01);
        assertEquals(PRICE, cashier.price(new Basket(BOOK4)), 0.01);
        assertEquals(PRICE, cashier.price(new Basket(BOOK5)), 0.01);

        // multiple order without discount
        assertEquals(2 * PRICE, cashier.price(new Basket(BOOK1, BOOK1)),        0.01);
        assertEquals(3 * PRICE, cashier.price(new Basket(BOOK2, BOOK2, BOOK2)), 0.01);
    }

    @Test
    public void simpleDiscounts() {
        assertEquals(5 * PRICE * 0.75, cashier.price(new Basket(BOOK1, BOOK2, BOOK3, BOOK4, BOOK5)), 0.01);
        assertEquals(4 * PRICE * 0.80, cashier.price(new Basket(BOOK1, BOOK2, BOOK3, BOOK5)),        0.01);
        assertEquals(2 * PRICE * 0.95, cashier.price(new Basket(BOOK1, BOOK2)),                      0.01);
        assertEquals(3 * PRICE * 0.90, cashier.price(new Basket(BOOK1, BOOK3, BOOK5)),               0.01);


    }

    @Test
    public void severalDiscounts() {
        assertEquals(PRICE + (2 * PRICE * 0.95), cashier.price(new Basket(BOOK1, BOOK1, BOOK2)), 0.01);
        assertEquals(2 * (2 * PRICE * 0.95), cashier.price(new Basket(BOOK1, BOOK1, BOOK2, BOOK2)), 0.01);
        assertEquals(   (4 * PRICE * 0.8) + (2 * PRICE * 0.95),
                        cashier.price(new Basket(BOOK1, BOOK1, BOOK2, BOOK3, BOOK3, BOOK4)), 0.01);
        assertEquals(   PRICE + (5 * PRICE * 0.75),
                        cashier.price(new Basket(BOOK1, BOOK2, BOOK2, BOOK3, BOOK4, BOOK5)), 0.01);
    }

    @Test
    public void edgeCase() {
        Basket b = new Basket(BOOK1, BOOK1, BOOK2, BOOK2, BOOK3, BOOK3, BOOK4, BOOK5);
        assertEquals(2 * (4 * PRICE * 0.8),
                cashier.price(b), 0.01);

        Basket allin = new Basket(
                BOOK1, BOOK1, BOOK1, BOOK1, BOOK1,
                BOOK2, BOOK2, BOOK2, BOOK2, BOOK2,
                BOOK3, BOOK3, BOOK3, BOOK3,
                BOOK4, BOOK4, BOOK4, BOOK4, BOOK4,
                BOOK5, BOOK5, BOOK5, BOOK5 );
        assertEquals(3 * (5 * PRICE * 0.75) + 2 * (4 * PRICE * 0.8), cashier.price(allin), 0.01);
    }
}