package test;
import org.jfree.data.time.TimePeriodFormatException;
import org.jfree.data.time.Year;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class Year2Test {
    Year year;
    Date time;

    private void arrange() {
        year = new Year();
    }

    @Test
    public void testDefaultConstructor() {
        Year year = new Year();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals("Default constructor use the current year",currentYear, year.getYear());
    }
    @Test
    public void testYearZeroPassesIncorrectly() {
        // BUG: This should throw IllegalArgumentException but passes
        new Year(0); // Year 0 is invalid but test passes
    }


    @Test
    public void testYearIntConstructor() {
        arrange();
        assertEquals(2025, year.getYear());
    }
    //failure doesnt throw an exception gets year outside the min range 1900
    @Test
    public void testConstructorBehavior() {
        try {
            Year y = new Year(1899);
            System.out.println("Year created: " + y);
            fail("Expected IllegalArgumentException for year < 1900");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }



    //when the input year is greater than the valid maximum (9999)throw exception . That’s exactly what the test is expecting to happen.
    @Test(expected = IllegalArgumentException.class)
    public void testIntConstructorAboveMaximum() {
        new Year(20000); //throw exception
    }
    // Buggy test that passes but shouldn't
    @Test
    public void testIntConstructorZeroPassesIncorrectly() {
        new Year(0); // Should throw but passes
    }

//passes but source code sets year to -9999 min
    @Test
    public void testYearIntConstructorEdgeLow() {
        Year year = new Year(Year.MINIMUM_YEAR);
        assertEquals("Year should be set to MINIMUM_YEAR (1900)", Year.MINIMUM_YEAR, year.getYear());
    }

    @Test
    public void testYearIntConstructorEdgeHigh() {
        Year year = new Year(Year.MAXIMUM_YEAR);
        assertEquals("Year should be set to MAXIMUM_YEAR (9999)", Year.MAXIMUM_YEAR, year.getYear());
    }

//Date constructor Date time
    @Test
    public void testYearDateConstructor() {
        Date date = new Date(); // Current date
        Year year = new Year(date);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals("Year should match the current year", currentYear, year.getYear());
    }
//with data passes within range
    @Test
    public void testDateConstructor() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JUNE, 15);
        Year year = new Year(cal.getTime());
        assertEquals(2023, year.getYear());
        assertFalse("Expected year to be 2023", year.getYear() != 2023);
    }


    @Test
    public void testNullDateExceptionDetails() {
        try {
            new Year((Date)null);
            fail("Null date is not accepted");
        } catch ( Exception e) {

        }
    }

    // Buggy test that passes but shouldn't
    //Bug Test Actual Behavior of date construcor (No Exception) which is wrong as 1 is not in range (1900-9999)
    @Test
    public void testDateConstructorAcceptsAnyYear() {
        // Should pass - shows the actual behavior
        Calendar cal = Calendar.getInstance();
        cal.set(1, Calendar.JANUARY, 1); // Year 1 AD
        Year year = new Year(cal.getTime());
        assertEquals(1, year.getYear()); // Confirms it accepts year 1
    }
    @Test
    public void testDateConstructorYearBoundary() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        Year year1 = new Year(cal.getTime());

        cal.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        Year year2 = new Year(cal.getTime());

        assertEquals(2023, year1.getYear());
        assertEquals(2024, year2.getYear());
    }
//date zone local
@Test
public void testYearDateTimeZoneLocaleConstructor() {
    Date date = new Date(); // Current date
    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    Locale locale = Locale.US;
    Year year = new Year(date, timeZone, locale);
    Calendar calendar = Calendar.getInstance(timeZone, locale);
    calendar.setTime(date);
    assertEquals("Year should match the calendar's year", calendar.get(Calendar.YEAR), year.getYear());
}
//data passed
    @Test
    public void testDateConstructorWithTimeZoneAndLocale() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JUNE, 15);
        Year year = new Year(cal.getTime(), TimeZone.getTimeZone("GMT"), Locale.US);
        assertEquals(2023, year.getYear());
    }

    @Test(expected = Exception.class)
    public void testDateConstructorWithTimeZoneAndLocaleNullDate() {
        new Year(null, TimeZone.getTimeZone("GMT"), Locale.US);
    }
    @Test
    public void testUTCBoundaryStartOfYear() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        // Act
        Year year = new Year(date, TimeZone.getTimeZone("UTC"), Locale.US);

        // Assert
        assertEquals(2024, year.getYear());
    }
    @Test
    public void testUTCBoundaryEndOfYear() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        // Act
        Year year = new Year(date, TimeZone.getTimeZone("UTC"), Locale.US);

        // Assert
        assertEquals(2024, year.getYear());
    }
    @Test
    public void testLeapYearHandling() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2016, Calendar.FEBRUARY, 29);
        Date date = cal.getTime();

        // Act
        Year year = new Year(date, TimeZone.getTimeZone("UTC"), Locale.US);

        // Assert
        assertEquals(2016, year.getYear());
    }

    @Test(expected = Exception.class)
    public void testDateConstructorWithNullTimeZone() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JUNE, 15);
        new Year(cal.getTime(), null, Locale.US);
    }

    @Test
    public void testGetYearForCurrentYear() {
        arrange(); // uses current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(currentYear, year.getYear());
    }
    @Test
    public void testGetYearForExplicitYear() {
        Year year = new Year(2029);
        assertEquals(2029, year.getYear());
    }
//SHOULD THROW EXCEPTION as year 0 is invalid per documentation
    @Test(expected = Exception.class)
    public void testGetYearForYearZero() {
        // This should fail as year 0 is invalid per documentation
        Year year = new Year(0);
        assertEquals(0, year.getYear());
    }

    //Bug;// This should fail as negative years are invalid
    @Test
    public void testGetYearForNegativeYear() {
        // This should fail as negative years are invalid
        Year year = new Year(-2023);
        assertEquals(-2023, year.getYear()); // Will pass but shouldn't
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetYearForYear10000() {
        // This should fail as 10000 is above maximum and through exception
        Year year = new Year(10000);
        assertEquals(10000, year.getYear()); //
    }
    @Test
    public void testGetYearMinimumBoundary() {
        Year year = new Year(1900); // Minimum valid year
        assertEquals(1900, year.getYear());
    }

    @Test
    public void testGetYearMaximumBoundary() {
        Year year = new Year(9999); // Maximum valid year
        assertEquals(9999, year.getYear());
    }

    //next
    @Test
    public void testNext() {
        Year year = new Year(2025);
        Year nextYear = (Year) year.next();
        assertNotNull("Next year should not be null", nextYear);
        assertEquals("Next year should be 2026", 2026, nextYear.getYear());
    }
    @Test
    public void testNextAtMaximumBoundary() {
        Year year = new Year(Year.MAXIMUM_YEAR);
        assertNull("Next year at boundary should be null", year.next());
    }
    //bug :This should throw expection as negative years are invalid in doc but src is valid(-9999 till9999)
    @Test(expected = Exception.class)
    public void testNextForNegativeYear() {
        Year negativeYear = new Year(-2023);
        Year next = (Year) negativeYear.next();
        assertEquals(-2022, next.getYear()); // Will pass but shouldn't
    }
    //Bug;// This should throgh exception as year 0 is invalid per documentation
    @Test(expected = Exception.class)
    public void testNextForYearZero() {
        // This should fail as year 0 is invalid per documentation
        Year year0 = new Year(0);
        Year next = (Year) year0.next();
        assertEquals(1, next.getYear()); // Will pass but shouldn't
    }
    //prevois Returns the year preceding this one.
    @Test
    public void testPrevious() {
        Year year = new Year(2025);
        Year previousYear = (Year) year.previous();
        assertNotNull("Previous year should not be null", previousYear);
        assertEquals("Previous year should be 2024", 2024, previousYear.getYear());
    }
    @Test
    public void testPreviousAtMinimumBoundary() {
        Year year = new Year(Year.MINIMUM_YEAR);
        assertNull("Previous year at boundary should be null", year.previous());
    }

    //Bug:it should return null as 1899 is out of range and throw exception but didint as src is -9999 to 9999
    @Test(expected = IllegalArgumentException.class)
    public void testPreviousAfterInvalidConstruction() {
        // First create invalid year if possible
        try {
            Year invalidYear = new Year(1899);
            invalidYear.previous(); // Should already fail at construction
        } catch (IllegalArgumentException e) {
            throw e; // Expected
        }
    }
    //bug this shoulld throw exception but didnt
    @Test(expected = IllegalArgumentException.class)
    public void testPreviousForNegativeYear() {
        // This should fail as negative years are invalid
        Year negativeYear = new Year(-2023);
        Year prev = (Year) negativeYear.previous();
        assertEquals(-2024, prev.getYear()); // Will pass but shouldn't
    }
    @Test
    public void testPreviousFromYear10000IfCreated() {
        try {
            // Try to create invalid year
            Year year10000 = new Year(10000);

            // If construction succeeds (implementation bug), test previous()
            Year previousYear = (Year) year10000.previous();

            // Verify behavior
            if (previousYear == null) {
                fail("Previous of 10000 returned null unexpectedly");
            } else {
                assertEquals(9999, previousYear.getYear());
            }
        } catch (IllegalArgumentException e) {
            // Expected behavior - test passes
            assertTrue("Properly rejected year 10000", true);
            assertEquals("Year constructor: year (10000) outside valid range.", e.getMessage());
        }
    }

    //compareTo(Object o1) negative == before, zero == same, positive == after.
    @Test
    public void testCompareToEquals() {
        Year y1 = new Year(2025);
        Year y2 = new Year(2025);
        assertEquals(0, y1.compareTo(y2));
    }

    @Test
    public void testCompareToLessThan() {   //year 1 is less than year 2
        Year y1 = new Year(2025);
        Year y2 = new Year(2026);
        assertTrue(y1.compareTo(y2) < 0);
    }

    @Test
    public void testCompareToMoreThan() {   //year 1 is more than year 2
        Year y1 = new Year(2025);
        Year y2 = new Year(2024);
        assertTrue(y1.compareTo(y2) > 0);
    }
    //it has to throw pointer  to not compare null with year
    @Test(expected = NullPointerException.class)
    public void testCompareTo_NullInput() {
        Year year2023 = new Year(2023);
        year2023.compareTo(null);
    }
    //it has to throw pointer  to not compare null with year
    @Test(expected = Exception.class)
    public void testCompareTo_NonYearObject() {
        Year year2023 = new Year(2023);
        year2023.compareTo("2023"); // String instead of Year
    }

    //toString() Returns a string representing the year..
    @Test
    public void testToString() {
        Year year = new Year(2025);
        assertEquals("toString should return the year as a string", "2025", year.toString());
    }
    @Test
    public void testToStringAtMaximumBoundary() {
        Year year = new Year(Year.MAXIMUM_YEAR);
        assertEquals("toString should handle the maximum valid year", "9999", year.toString());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testToStringNegativeYear() {
        try {
            Year year = new Year(-2023);
            assertEquals("-2023", year.toString()); // Will pass but shouldn't
        } catch (IllegalArgumentException e) {
            // Expected if constructor validates range
        }
    }
    //it has to throw exception as 1800 is below min 1900 but it fails to throw
    @Test
    public void testToStringForInvalidYearBelowMinimum() {
        Year year = null;
        try {
            year = new Year(1800);
            fail("Expected IllegalArgumentException for year below minimum");
        } catch (IllegalArgumentException e) {
            // Expected, do nothing
        }
        assertNull(year);
    }

    @Test
    public void testToStringForInvalidYearAboveMaximum() {
        Year year = null;
        try {
            year = new Year(10000);
            fail("Expected IllegalArgumentException for year above maximum");
        } catch (IllegalArgumentException e) {
            // Expected, do nothing
        }
        assertNull(year);
    }
    @Test
    public void testGetFirstMillisecond() {
        Year year = new Year(2024);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        expected.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        expected.set(Calendar.MILLISECOND, 0);

        assertEquals(expected.getTimeInMillis(), year.getFirstMillisecond(cal));
    }

    @Test    //bug as it shouldn't accept negative date.
    public void testGetFirstMillisecondNegative() {
        Year year = new Year(-2024);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        expected.set(-2024, Calendar.JANUARY, 1, 0, 0, 0);
        expected.set(Calendar.MILLISECOND, 0);

        assertEquals(expected.getTimeInMillis(), year.getFirstMillisecond(cal));
    }


    @Test(expected = NullPointerException.class)
    public void testGetFirstMillisecond_NullCalendar() {
        Year year = new Year(2025);
        year.getFirstMillisecond(null);
    }



    @Test
    public void testGetLastMillisecond() {
        Year year = new Year(2024);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        expected.set(2024, Calendar.DECEMBER, 31, 23, 59, 59);
        expected.set(Calendar.MILLISECOND, 999);

        assertEquals(expected.getTimeInMillis(), year.getLastMillisecond(cal));
    }

    @Test
    public void testGetLastMillisecondNegative() {
        Year year = new Year(-2024);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        expected.set(-2024, Calendar.DECEMBER, 31, 23, 59, 59);
        expected.set(Calendar.MILLISECOND, 999);

        assertEquals(expected.getTimeInMillis(), year.getLastMillisecond(cal));
    }

    @Test(expected = NullPointerException.class)
    public void testGetLastMillisecondNullCalendar() {
        Year year = new Year(2025);
        year.getLastMillisecond(null);
    }

    @Test
    public void testGetLastMillisecond_LeapYear() {
        Year year = new Year(2020);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        cal.set(2020, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long expected = cal.getTimeInMillis();

        assertEquals(expected, year.getLastMillisecond(cal));
    }

    @Test
    public void testPeg() {
        Year year = new Year(2025);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);

        year.peg(cal);
    }

    @Test(expected = NullPointerException.class)
    public void testPegNullCalendar() {
        Year year = new Year(2025);
        year.peg(null);
    }

    @Test
    public void testEqualsSameYear() {
        Year y1 = new Year(2024);
        Year y2 = new Year(2024);
        assertTrue(y1.equals(y2));
    }

    @Test
    public void testEqualsDifferentYear() {
        Year y1 = new Year(2024);
        Year y2 = new Year(2023);
        assertFalse(y1.equals(y2));
    }

    @Test
    public void testEquals_SameObject() {
        Year y1 = new Year(2024);
        assertTrue(y1.equals(y1));
    }

    @Test
    public void testEqualsExchange() {
        Year y1 = new Year(2022);
        Year y2 = new Year(2022);
        assertEquals(y1.equals(y2), y2.equals(y1));
    }

    @Test
    public void testEqualsNull() {
        Year y1 = new Year(2024);
        assertFalse(y1.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Year y1 = new Year(2024);
        assertFalse(y1.equals(2024));
    }

    @Test //Bug, should not accept negative year. if it could accept negative, it works just fine
    public void testEqualsNegativeYear() {
        Year y1 = new Year(-100);
        Year y2 = new Year(-100);
        assertTrue(y1.equals(y2));
    }

    @Test      //Bug it takes min as -9999 in sourcode  which is wrong correct range (1900-9999)
    public void testEqualsMinValue() {
        Year minYear1 = new Year(Year.MINIMUM_YEAR);
        Year minYear2 = new Year(Year.MINIMUM_YEAR);
        assertTrue(minYear1.equals(minYear2));
    }

    @Test
    public void testEqualsMaxValue() {
        Year maxYear1 = new Year(Year.MAXIMUM_YEAR);
        Year maxYear2 = new Year(Year.MAXIMUM_YEAR);
        assertTrue(maxYear1.equals(maxYear2));
    }

    @Test
    public void testEqualsMaxMinValue() {
        Year maxYear = new Year(Year.MAXIMUM_YEAR);
        Year MinYear = new Year(Year.MINIMUM_YEAR);
        assertFalse(maxYear.equals(MinYear));
    }

    //Hash code Returns a hash code for this object instance.
    @Test
    public void testHashCodeConsistencyForEqualObjects() {
        Year year1 = new Year(2025);
        Year year2 = new Year(2025);
        assertTrue("Equal objects should have the same hash code", year1.hashCode() == year2.hashCode());
    }

    @Test
    public void testHashCodeDifferenceForUnequalObjects() {
        Year year1 = new Year(2025);
        Year year2 = new Year(2020);
        assertFalse("Unequal objects should have different hash codes", year1.hashCode() == year2.hashCode());
    }
    @Test
    public void testHashCodeStability() {
        Year year = new Year(2025);
        int initialHashCode = year.hashCode();
        // Ensure the hash code remains the same after multiple calls
        assertEquals("Hash code should remain stable", initialHashCode, year.hashCode());
        assertEquals(" again Hash code should remain stable", initialHashCode, year.hashCode());
    }
    @Test
    public void testHashCodeForNegativeYear() {
        Year year = new Year(-2025);
        int hashCode = year.hashCode();
        assertTrue("Hash code for negative year should be consistent", hashCode == year.hashCode());
    }

    @Test
    public void testHashCodeForPositiveYear() {
        Year year = new Year(2000);
        int hashCode = year.hashCode();
        assertEquals( hashCode, year.hashCode());
    }

    @Test
    public void testGetSerialIndexPositiveYear() {
        Year year = new Year(2024);
        assertEquals(2024, year.getSerialIndex());
    }

    @Test
    public void testGetSerialIndexZeroYear() {
        Year year = new Year(0);
        assertEquals(0, year.getSerialIndex());
    }

    @Test
    public void testGetSerialIndexNegativeYear() {
        Year year = new Year(-100);
        assertEquals(-100, year.getSerialIndex());
    }

    @Test
    public void testGetSerialIndexMaximumYear() {
        Year year = new Year(Year.MAXIMUM_YEAR);
        assertEquals(9999, year.getSerialIndex());
    }

    @Test
    public void testGetSerialIndex_MinimumYear() {
        Year year = new Year(Year.MINIMUM_YEAR);
        assertEquals(-9999, year.getSerialIndex());
    }

    @Test
    public void testGetSerialIndex_DifferentInstancesSameYear() {
        Year y1 = new Year(2000);
        Year y2 = new Year(2000);
        assertEquals(y1.getSerialIndex(), y2.getSerialIndex());
    }

    @Test
    public void testGetSerialIndex_DifferentYears() {
        Year y1 = new Year(1990);
        Year y2 = new Year(2000);
        assertNotEquals(y1.getSerialIndex(), y2.getSerialIndex());
    }






    @Test
    public void testParseValidYear() {
        Year year = Year.parseYear("2023");
        assertNotNull(year);
        assertEquals(2023, year.getYear());
    }
    @Test(expected = NullPointerException.class)
    public void testParseNullString() {
        Year year = Year.parseYear(null);
        assertNull(year);
    }
    //BUG;test does not through exception
    @Test(expected = IllegalArgumentException.class)
    public void testParseOutOfRangeYear() {
        assertNull(Year.parseYear("1899"));  // Below minimum
        assertNull(Year.parseYear("10000")); // Above maximum
    }


    @Test
    public void testParseYearInvalidFormat() {
        try {
            Year.parseYear("twenty twenty-five");
            fail("Expected TimePeriodFormatException for invalid year format");
        } catch (TimePeriodFormatException e) {
            assertEquals("Cannot parse string.", e.getMessage());
        }
    }
    //Bug it should not parse negative outt of range SHOULD throw excetiob but in source code range is (-9999 to 9999)
    @Test(expected = Exception.class)
    public void testParseYearValidNegative() {
        Year year = Year.parseYear("-2021");

        assertEquals("Parsed year should match the input", -2021, year.getYear());
    }

}