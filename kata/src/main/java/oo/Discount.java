package oo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class  Discount {

    private int nbBooks;
    private double percentage;

    public Discount(int nbBooks, double percentage) {
        this.nbBooks = nbBooks;
        this.percentage = percentage;
    }

    public boolean canBeApplied(Basket b) {
        return b.howManyDifferent() >= nbBooks;
    }

    public double apply(Basket b) {
        return Cashier.PRICE * nbBooks * percentage;
    }

    public Basket removePayedBooks(Basket b) {
        Map<Book, Integer> data = contents(b);
        Book[] consumed =
                data.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList()).subList(0, nbBooks).toArray(new Book[]{});
        Basket result = b.duplicate();
        result.remove(consumed);
        return result;
    }

    private Map<Book, Integer> contents(Basket b) {
        Map<Book, Integer> data = new HashMap<>();
        for(Book book: Book.values())
            data.put(book, b.howMany(book));
        return data;
    }
}



