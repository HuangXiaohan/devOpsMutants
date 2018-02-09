package raw;

import java.util.List;

/**
 * Calculator for the KataPotter exercise
 * (source: http://bfindeiss.blogspot.fr/2013/07/the-katapotter-in-java-solved-in-31.html)
 */
public class PotterDiscountCalculator {

    private double[] discountRates;
    private int[] discounts;

    private void init() {
        discountRates = new double[]{ 1, 0.95, 0.90, 0.80, 0.75 };
        discounts = new int[5];
    }

    public double calculatePrice(List<Integer> order) {
        init(); // resetting the calculator before computing a price
        if (order == null || order.isEmpty())
            return 0.0;
        int[] booksInOrder = calculateBooksInOrder(order);
        calculateDiscounts(booksInOrder);
        optimizeDiscounts();
        double price = 0.0;
        for (int i = 0; i < discounts.length; i++)
            price += (8 * (i + 1) * discounts[i]) * discountRates[i];
        return price;
    }

    protected void optimizeDiscounts() {
        while (discounts[2] > 0 && discounts[4] > 0) {
            discounts[2]--;
            discounts[4]--;
            discounts[3] += 2;
        }
    }

    protected void calculateDiscounts(int[] booksInOrder) {
        if (booksInOrder == null)
            return;
        int differentFromZero = 0;
        for (int i = 0; i < booksInOrder.length; i++) {
            if (booksInOrder[i] > 0) {
                differentFromZero++;
                booksInOrder[i]--;
            }
        }
        if (differentFromZero > 0) {
            discounts[differentFromZero - 1] += 1;
            calculateDiscounts(booksInOrder);
        }
    }

    private int[] calculateBooksInOrder(List<Integer> order) {
        int[] result = new int[5];
        for (int book : order)
            result[book - 1]++;
        return result;
    }
}
