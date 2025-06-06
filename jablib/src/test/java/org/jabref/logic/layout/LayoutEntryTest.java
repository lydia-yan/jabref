package org.jabref.logic.layout;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.model.database.BibDatabase;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.SpecialField;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.field.UnknownField;
import org.jabref.model.metadata.MetaData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * The test class LayoutEntryTest test the net.sf.jabref.export.layout.LayoutEntry. Indirectly the
 * net.sf.jabref.export.layout.Layout is tested too.
 * <p/>
 * The LayoutEntry creates a human readable String assigned with HTML formatters. To test the Highlighting Feature, an
 * instance of LayoutEntry will be instantiated via Layout and LayoutHelper. With these instance the doLayout() Method
 * is called several times for each test case. To simulate a search, a BibEntry will be created, which will be used by
 * LayoutEntry.
 * There are five test cases: - The shown result text has no words which should be highlighted. - There is one word
 * which will be highlighted ignoring case sensitivity. - There are two words which will be highlighted ignoring case
 * sensitivity. - There is one word which will be highlighted case sensitivity. - There are more words which will be
 * highlighted case sensitivity.
 */

class LayoutEntryTest {

    @BeforeEach
    void setUp() {
        BibEntry mBTE = new BibEntry();
        mBTE.setField(StandardField.ABSTRACT, "In this paper, we initiate a formal study of security on Android: Google's new open-source platform for mobile devices. Tags: Paper android google Open-Source Devices");
        //  Specifically, we present a core typed language to describe Android applications, and to reason about their data-flow security properties. Our operational semantics and type system provide some necessary foundations to help both users and developers of Android applications deal with their security concerns.
        mBTE.setField(StandardField.KEYWORDS, "android, mobile devices, security");
        mBTE.setField(new UnknownField("posted-at"), "2010-08-11 15:00:49");
        mBTE.setField(StandardField.LOCATION, "Dublin, Ireland");
        mBTE.setCitationKey("chaudhuri-plas09");
        mBTE.setField(StandardField.PAGES, "1--7");
        mBTE.setField(StandardField.BOOKTITLE, "PLAS '09: Proceedings of the ACM SIGPLAN Fourth Workshop on Programming Languages and Analysis for Security");
        mBTE.setField(new UnknownField("citeulike-article-id"), "7615801");
        mBTE.setField(new UnknownField("citeulike-linkout-1"), "http://dx.doi.org/10.1145/1554339.1554341");
        mBTE.setField(StandardField.URL, "http://dx.doi.org/10.1145/1554339.1554341");
        mBTE.setField(StandardField.PUBLISHER, "ACM");
        mBTE.setField(StandardField.TIMESTAMP, "2010.11.11");
        mBTE.setField(StandardField.AUTHOR, "Chaudhuri, Avik");
        mBTE.setField(StandardField.TITLE, "Language-based security on Android");
        mBTE.setField(StandardField.ADDRESS, "New York, NY, USA");
        mBTE.setField(SpecialField.PRIORITY, "2");
        mBTE.setField(StandardField.ISBN, "978-1-60558-645-8");
        mBTE.setField(StandardField.OWNER, "Arne");
        mBTE.setField(StandardField.YEAR, "2009");
        mBTE.setField(new UnknownField("citeulike-linkout-0"), "http://portal.acm.org/citation.cfm?id=1554339.1554341");
        mBTE.setField(StandardField.DOI, "10.1145/1554339.1554341");
    }

    public String layout(String layoutFile, BibEntry entry) throws IOException {
        Reader reader = Reader.of(layoutFile.replace("__NEWLINE__", "\n"));
        Layout layout = new LayoutHelper(reader, mock(LayoutFormatterPreferences.class), mock(JournalAbbreviationRepository.class)).getLayoutFromText();

        return layout.doLayout(entry, null);
    }

    @Test
    void parseMethodCalls() {
        assertEquals(1, LayoutEntry.parseMethodsCalls("bla").size());
        assertEquals("bla", LayoutEntry.parseMethodsCalls("bla").getFirst().getFirst());

        assertEquals(1, LayoutEntry.parseMethodsCalls("bla,").size());
        assertEquals("bla", LayoutEntry.parseMethodsCalls("bla,").getFirst().getFirst());

        assertEquals(1, LayoutEntry.parseMethodsCalls("_bla.bla.blub,").size());
        assertEquals("_bla.bla.blub", LayoutEntry.parseMethodsCalls("_bla.bla.blub,").getFirst().getFirst());

        assertEquals(2, LayoutEntry.parseMethodsCalls("bla,foo").size());
        assertEquals("bla", LayoutEntry.parseMethodsCalls("bla,foo").getFirst().getFirst());
        assertEquals("foo", LayoutEntry.parseMethodsCalls("bla,foo").get(1).getFirst());

        assertEquals(2, LayoutEntry.parseMethodsCalls("bla(\"test\"),foo(\"fark\")").size());
        assertEquals("bla", LayoutEntry.parseMethodsCalls("bla(\"test\"),foo(\"fark\")").getFirst().getFirst());
        assertEquals("foo", LayoutEntry.parseMethodsCalls("bla(\"test\"),foo(\"fark\")").get(1).getFirst());
        assertEquals("test", LayoutEntry.parseMethodsCalls("bla(\"test\"),foo(\"fark\")").getFirst().get(1));
        assertEquals("fark", LayoutEntry.parseMethodsCalls("bla(\"test\"),foo(\"fark\")").get(1).get(1));

        assertEquals(2, LayoutEntry.parseMethodsCalls("bla(test),foo(fark)").size());
        assertEquals("bla", LayoutEntry.parseMethodsCalls("bla(test),foo(fark)").getFirst().getFirst());
        assertEquals("foo", LayoutEntry.parseMethodsCalls("bla(test),foo(fark)").get(1).getFirst());
        assertEquals("test", LayoutEntry.parseMethodsCalls("bla(test),foo(fark)").getFirst().get(1));
        assertEquals("fark", LayoutEntry.parseMethodsCalls("bla(test),foo(fark)").get(1).get(1));
    }

    @ParameterizedTest
    @CsvSource({
            "2",
            "3",
            "4",
            "6",
            "7"
    })
    void unsupportedOperationTypes(int type) {
        List<StringInt> parsedEntries = List.of(new StringInt("place_holder", 0),
                new StringInt("testString", 0));
        BibDatabaseContext bibDatabaseContext = new BibDatabaseContext(new BibDatabase(), new MetaData(), null);
        LayoutEntry layoutEntry = new LayoutEntry(parsedEntries, type, null, null, null);
        assertThrows(UnsupportedOperationException.class, () -> layoutEntry.doLayout(bibDatabaseContext, StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @CsvSource({
            "1, testString",
            "5, testString",
            "8, UTF-8",
            "9, ''",
            "10, ''"
    })
    void layoutResult(int type, String expectedValue) {
        List<StringInt> parsedEntries = List.of(new StringInt("place_holder", 0),
                new StringInt("testString", 0));
        BibDatabaseContext bibDatabaseContext = new BibDatabaseContext(new BibDatabase(), new MetaData(), null);
        LayoutEntry layoutEntry = new LayoutEntry(parsedEntries, type, null, null, null);
        assertEquals(expectedValue, layoutEntry.doLayout(bibDatabaseContext, StandardCharsets.UTF_8));
    }
}
