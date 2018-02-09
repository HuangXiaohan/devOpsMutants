package oo;

import static oo.Book.*;

import org.junit.*;
import static org.junit.Assert.*;

public class DiscountTest {

    private Basket empty  = new Basket();
    private Basket school = new Basket(BOOK1, BOOK1, BOOK1, BOOK1, BOOK1);
    private Basket series = new Basket(BOOK1, BOOK2, BOOK3, BOOK4, BOOK5);
    private Basket allin  = new Basket(BOOK1, BOOK1, BOOK2, BOOK2, BOOK3, BOOK3, BOOK4, BOOK5);

    @Test
    public void testDiscountMatch() {
        Discount d = new Discount(5, 0.75);
        assertFalse(d.canBeApplied(empty));
        assertFalse(d.canBeApplied(school));
        assertTrue(d.canBeApplied(series));
        assertTrue(d.canBeApplied(allin));
    }

    @Test
    public void testDiscountApplication() {
        Discount d = new Discount(5, 0.75);
        assertEquals(5 * Cashier.PRICE * 0.75, d.apply(series), 0.01);
        assertTrue(d.removePayedBooks(series).isEmpty());
        assertFalse(series.isEmpty());
    }

    @Test
    public void testEdgeCase() {
        Discount d = new Discount(4, 0.80);
        Basket remaining = d.removePayedBooks(allin);
        assertEquals(8, allin.howManyBooks());
        assertEquals(4, remaining.howManyBooks());
        assertEquals(4, remaining.howManyDifferent());
        assertEquals(1, remaining.howMany(BOOK1));
        assertEquals(1, remaining.howMany(BOOK2));
        assertEquals(1, remaining.howMany(BOOK3));
        assertTrue(remaining.howMany(BOOK4) == 1 || remaining.howMany(BOOK5) == 1);
    }

}
