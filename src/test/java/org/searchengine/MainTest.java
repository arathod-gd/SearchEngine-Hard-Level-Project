package org.searchengine;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void shouldRunMainAndExit() throws Exception {
        String input = """
                2
                0
                """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{"--data", "src/test/resources/data.txt"});
    }

    @Test
    void shouldRunSearchFlow() throws Exception {
        String input = """
                1
                ALL
                Erick
                0
                """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{"--data", "src/test/resources/data.txt"});
    }

    @Test
    void shouldHandleWrongOption() throws Exception {
        String input = """
                9
                0
                """;

        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{"--data", "src/test/resources/data.txt"});
    }
}
