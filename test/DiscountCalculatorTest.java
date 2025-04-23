package test;

import JFree.DiscountCalculator;
import org.jfree.data.time.Week;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import java.lang.Exception;

import static org.junit.Assert.*;

public class DiscountCalculatorTest {

    @Test
    public void testIsTheSpecialWeekWhenFalse() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 22);  // March 22, 2025
        Date date = calendar.getTime();
        Week week = new Week(date);
        DiscountCalculator calculator = new DiscountCalculator(week);

        // Act
        boolean result = calculator.isTheSpecialWeek();

        // Assert
        assertFalse("This week is not specialweek(not week 26)", result);
    }

    // Test missing cases ( JUNE, 23 is a date in week 26 )
    @Test
    public void testIsTheSpecialWeekWhenTrue() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 23);  // June 23, 2025 - expected to be week 26
        Date date = calendar.getTime();
        Week week = new Week(date);
        DiscountCalculator calculator = new DiscountCalculator(week);

        // Act
        boolean result = calculator.isTheSpecialWeek();

        // Assert
        assertTrue("It is a special week (week number 26)", result);
    }
    @Test
    public void testGetDiscountPercentageEvenWeek() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 23);  // Week 26 is even
        Date date = calendar.getTime();
        Week week = new Week(date);
        DiscountCalculator calculator = new DiscountCalculator(week);

        // Act
        int discount = calculator.getDiscountPercentage();

        // Assert
        assertEquals("Discount should be 7% for even week", 7, discount);
    }



    @Test
    public void testGetDiscountPercentageOddWeek() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 30);  // Week 27 is odd
        Date date = calendar.getTime();
        Week week = new Week(date);
        DiscountCalculator calculator = new DiscountCalculator(week);

        // Act
        int discount = calculator.getDiscountPercentage();

        // Assert
        assertEquals("Discount should be 5% for odd week", 5, discount);
    }

    @Test
    public void testDiscountCalculatorObjectIsNotNull() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 23);  // This is in week 26
        Date date = calendar.getTime();
        Week week = new Week(date);

        // Act
        DiscountCalculator calculator = new DiscountCalculator(week);

        // Assert
        assertNotNull("DiscountCalculator object should not be null", calculator);
    }


    @Test
    public void testBoundaryDateWeek1January1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 1);
        Week week = new Week(calendar.getTime());

        DiscountCalculator calculator = new DiscountCalculator(week);
        assertFalse("January 1 should not be the special week", calculator.isTheSpecialWeek());
        assertEquals("Expected 5% discount for odd week 1", 5, calculator.getDiscountPercentage());
    }

    @Test
    public void testEndOfYearEdgeCase() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.DECEMBER, 31);
        Week week = new Week(calendar.getTime());

        DiscountCalculator calculator = new DiscountCalculator(week);
        assertNotNull("Week object should not be null", week);

        int weekNumber = week.getWeek();
        assertFalse("Week " + weekNumber + " should not be the special week", calculator.isTheSpecialWeek());

        int expected = (weekNumber % 2 == 0) ? 7 : 5;
        assertEquals("Expected discount based on week " + weekNumber, expected, calculator.getDiscountPercentage());
    }
    @Test
    public void testIsTheSpecialWeekIncorrectExpectation() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 24);  // Still part of week 26
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);

        // This is expected to fail
        assertFalse("This should fail: June 24 is actually in the special week", calculator.isTheSpecialWeek());
    }

    @Test
    public void testIncorrectDiscountForEvenWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JULY, 7);  // Likely week 28 (even)
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);

        // This will fail because the expected value is incorrect
        assertEquals("This should fail: expected wrong discount", 5, calculator.getDiscountPercentage());
    }


}
