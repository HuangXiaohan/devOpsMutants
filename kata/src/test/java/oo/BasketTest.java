package oo;


import org.junit.Test;
import static org.junit.Assert.* ;

import static oo.Book.*;

public class BasketTest {


    private static final Basket empty = new Basket();
    private static final Basket complete = new Basket(BOOK1, BOOK2, BOOK3, BOOK4, BOOK5);
    private static final Basket azkaban = new Basket(BOOK3, BOOK3, BOOK3);

    @Test
    public void basketContents() {
        for(Book b: Book.values())
            assertEquals(0, empty.howMany(b));

        for(Book b: Book.values())
            assertEquals(1, complete.howMany(b));

        for(Book b: Book.values())
            if(b == BOOK3) {
                assertEquals(3, azkaban.howMany(b));
            } else {
                assertEquals(0, azkaban.howMany(b));
            }

    }

    @Test
    public void differentiatingContents() {
        assertEquals(0, empty.howManyDifferent());
        assertEquals(5, complete.howManyDifferent());
        assertEquals(1, azkaban.howManyDifferent());
    }

    @Test
    public void emptiness() {
        assertTrue(empty.isEmpty());
        assertFalse(complete.isEmpty());
        assertFalse(azkaban.isEmpty());
    }

    @Test
    public void numberOfBooks() {
        assertEquals(0, empty.howManyBooks());
        assertEquals(5, complete.howManyBooks());
        assertEquals(3, azkaban.howManyBooks());
    }

    @Test
    public void removeBooks() {
        Basket basket = new Basket(BOOK1, BOOK2, BOOK2);
        assertEquals(3, basket.howManyBooks());
        basket.remove(BOOK1);
        assertEquals(2, basket.howManyBooks());
        basket.remove(BOOK2);
        assertEquals(1, basket.howManyBooks());
    }

    @Test
    public void cloneBooks() {
        Basket basket = new Basket(BOOK1, BOOK2, BOOK2);
        Basket clone = basket.duplicate();
        clone.remove(BOOK2);
        assertEquals(2, basket.howMany(BOOK2));
        assertEquals(1, clone.howMany(BOOK2));
    }

}