package org.searchengine;

import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    Person person;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        person = new Person();
        person.loadFromFile("src/test/resources/data.txt");
        person.buildIndex();
    }

    @Test
    void shouldFindUsingAllStrategy() {
        Set<String> result = person.searchResult("Erick", "ALL");
        assertEquals(2, result.size());
        assertTrue(result.contains("Erick Harrington harrington@gmail.com"));
        assertTrue(result.contains("Erick Burgess"));
    }

    @Test
    void shouldFindUsingAnyStrategy() {
        Set<String> result = person.searchResult("Katie Rene", "ANY");
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindUsingNoneStrategy() {
        Set<String> result = person.searchResult("Erick", "NONE");
        assertEquals(4, result.size());
    }

    @Test
    void shouldReturnEmptyForUnknownWord() {
        Set<String> result = person.searchResult("XYZ", "ALL");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldBeCaseInsensitive() {
        Set<String> result = person.searchResult("erick", "ALL");
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyForInvalidStrategy() {
        Set<String> result = person.searchResult("Erick", "INVALID");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleMultipleWordsAll() {
        Set<String> result = person.searchResult("Erick Harrington", "ALL");
        assertEquals(1, result.size());
        assertTrue(result.contains("Erick Harrington harrington@gmail.com"));
    }

    @Test
    void shouldReturnAllWhenNoneMatchesAnything() {
        Set<String> result = person.searchResult("XYZ", "NONE");
        assertEquals(6, result.size()); // all people in file
    }

    @Test
    void shouldExecuteSearchConsoleMethod() {
        person.search("Erick", "ALL");
        person.search("XYZ", "ALL"); // triggers "No matching people found."
    }

    @Test
    void shouldPrintAll() {
        person.printAll(); // increases coverage
    }
}
