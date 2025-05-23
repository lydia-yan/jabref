package org.jabref.logic.bibtex.comparator;

import java.util.stream.Stream;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.InternalField;
import org.jabref.model.entry.field.OrFields;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldComparatorTest {
    @Test
    void compareMonthFieldIdentity() {
        FieldComparator comparator = new FieldComparator(StandardField.MONTH);
        BibEntry equal = new BibEntry()
                .withField(StandardField.MONTH, "1");

        assertEquals(0, comparator.compare(equal, equal));
    }

    @Test
    void compareMonthFieldEquality() {
        FieldComparator comparator = new FieldComparator(StandardField.MONTH);
        BibEntry equal = new BibEntry()
                .withField(StandardField.MONTH, "1");
        BibEntry equal2 = new BibEntry()
                .withField(StandardField.MONTH, "1");

        assertEquals(0, comparator.compare(equal, equal2));
    }

    @Test
    void compareMonthFieldBiggerAscending() {
        FieldComparator comparator = new FieldComparator(StandardField.MONTH);
        BibEntry smaller = new BibEntry()
                .withField(StandardField.MONTH, "jan");
        BibEntry bigger = new BibEntry()
                .withField(StandardField.MONTH, "feb");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareMonthFieldBiggerDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(StandardField.MONTH), true);
        BibEntry smaller = new BibEntry()
                .withField(StandardField.MONTH, "feb");
        BibEntry bigger = new BibEntry()
                .withField(StandardField.MONTH, "jan");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareYearFieldIdentity() {
        FieldComparator comparator = new FieldComparator(StandardField.YEAR);
        BibEntry equal = new BibEntry()
                .withField(StandardField.YEAR, "2016");

        assertEquals(0, comparator.compare(equal, equal));
    }

    @Test
    void compareYearFieldEquality() {
        FieldComparator comparator = new FieldComparator(StandardField.YEAR);
        BibEntry equal = new BibEntry()
                .withField(StandardField.YEAR, "2016");
        BibEntry equal2 = new BibEntry()
                .withField(StandardField.YEAR, "2016");

        assertEquals(0, comparator.compare(equal, equal2));
    }

    @Test
    void compareYearFieldBiggerAscending() {
        FieldComparator comparator = new FieldComparator(StandardField.YEAR);
        BibEntry smaller = new BibEntry()
                .withField(StandardField.YEAR, "2000");
        BibEntry bigger = new BibEntry()
                .withField(StandardField.YEAR, "2016");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareYearFieldBiggerDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(StandardField.YEAR), true);
        BibEntry smaller = new BibEntry()
                .withField(StandardField.YEAR, "2016");
        BibEntry bigger = new BibEntry()
                .withField(StandardField.YEAR, "2000");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareTypeFieldIdentity() {
        FieldComparator comparator = new FieldComparator(InternalField.TYPE_HEADER);
        BibEntry equal = new BibEntry(StandardEntryType.Article);

        assertEquals(0, comparator.compare(equal, equal));
    }

    @Test
    void compareTypeFieldEquality() {
        FieldComparator comparator = new FieldComparator(InternalField.TYPE_HEADER);
        BibEntry equal = new BibEntry(StandardEntryType.Article);
        equal.setId("1");
        BibEntry equal2 = new BibEntry(StandardEntryType.Article);
        equal2.setId("1");

        assertEquals(0, comparator.compare(equal, equal2));
    }

    @Test
    void compareTypeFieldBiggerAscending() {
        FieldComparator comparator = new FieldComparator(InternalField.TYPE_HEADER);
        BibEntry smaller = new BibEntry(StandardEntryType.Article);
        BibEntry bigger = new BibEntry(StandardEntryType.Book);

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareTypeFieldBiggerDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(InternalField.TYPE_HEADER), true);
        BibEntry bigger = new BibEntry(StandardEntryType.Article);
        BibEntry smaller = new BibEntry(StandardEntryType.Book);

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareStringFieldsIdentity() {
        FieldComparator comparator = new FieldComparator(StandardField.TITLE);
        BibEntry equal = new BibEntry()
                .withField(StandardField.TITLE, "title");

        assertEquals(0, comparator.compare(equal, equal));
    }

    @Test
    void compareStringFieldsEquality() {
        FieldComparator comparator = new FieldComparator(StandardField.TITLE);
        BibEntry equal = new BibEntry()
                .withField(StandardField.TITLE, "title");
        BibEntry equal2 = new BibEntry()
                .withField(StandardField.TITLE, "title");

        assertEquals(0, comparator.compare(equal, equal2));
    }

    @Test
    void compareStringFieldsBiggerAscending() {
        FieldComparator comparator = new FieldComparator(StandardField.TITLE);
        BibEntry bigger = new BibEntry()
                .withField(StandardField.TITLE, "b");
        BibEntry smaller = new BibEntry()
                .withField(StandardField.TITLE, "a");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareStringFieldsBiggerDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(StandardField.TITLE), true);
        BibEntry bigger = new BibEntry()
                .withField(StandardField.TITLE, "a");
        BibEntry smaller = new BibEntry()
                .withField(StandardField.TITLE, "b");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareNumericFieldsBiggerDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(StandardField.PMID), true);
        BibEntry smaller = new BibEntry()
                .withField(StandardField.PMID, "234567");
        BibEntry bigger = new BibEntry()
                .withField(StandardField.PMID, "123456");

        assertEquals(1, comparator.compare(bigger, smaller));
    }

    @Test
    void compareParsableWithNonParsableNumericFieldDescending() {
        FieldComparator comparator = new FieldComparator(new OrFields(StandardField.PMID), true);
        BibEntry parsable = new BibEntry()
                .withField(StandardField.PMID, "123456");
        BibEntry unparsable = new BibEntry()
                .withField(StandardField.PMID, "abc##z");

        assertEquals(1, comparator.compare(parsable, unparsable));
    }

    @ParameterizedTest
    @MethodSource
    void compareNumericalValues(int comparisonResult, String id1, String id2, String message) {
        FieldComparator comparator = new FieldComparator(StandardField.PMID);
        BibEntry entry1 = new BibEntry()
                .withField(StandardField.PMID, id1);
        BibEntry entry2 = new BibEntry()
                .withField(StandardField.PMID, id2);

        assertEquals(comparisonResult, comparator.compare(entry1, entry2), message);
    }

    private static Stream<Arguments> compareNumericalValues() {
        return Stream.of(
                Arguments.of(0, "123456", "123456", "IDs should be lexicographically equal"),
                Arguments.of(1, "234567", "123456", "234567 should be lexicographically greater than 123456"),
                Arguments.of(1, "abc##z", "123456", "abc##z should be lexicographically greater than 123456 "),
                Arguments.of(0, "", "", "Empty IDs should be lexicographically equal"),
                Arguments.of(-1, "", "123456", "No ID should be lexicographically smaller than 123456"),
                Arguments.of(1, "123456", "", "123456 should be lexicographically greater than no ID")
        );
    }

    @ParameterizedTest
    @MethodSource
    void nullTests(int comparisonResult, String firstValue, String secondValue) {
        FieldComparator comparator = new FieldComparator(StandardField.TITLE);

        BibEntry entry1 = new BibEntry();
        if (firstValue != null) {
            entry1.setField(StandardField.TITLE, firstValue);
        }

        BibEntry entry2 = new BibEntry();
        if (secondValue != null) {
            entry2.setField(StandardField.TITLE, secondValue);
        }

        assertEquals(comparisonResult, comparator.compare(entry1, entry2));
    }

    private static Stream<Arguments> nullTests() {
        return Stream.of(
                Arguments.of(0, null, null),
                Arguments.of(0, "value", "value"),
                Arguments.of(-1, null, "value"),
                Arguments.of(1, "value", null)
        );
    }

    @Test
    void compareAuthorField() {
        FieldComparator comparator = new FieldComparator(StandardField.AUTHOR);
        BibEntry bigger = new BibEntry()
                .withField(StandardField.AUTHOR, "Freund, Lucas");
        BibEntry smaller = new BibEntry()
                .withField(StandardField.AUTHOR, "Mustermann, Max");

        assertEquals(1, comparator.compare(smaller, bigger));
    }
}
