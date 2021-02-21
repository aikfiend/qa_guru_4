import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTests {
    @Test
    @Tag("Positive")
    void positiveTest() {
        assertEquals(true, true);
    }

    @Test
    @Tag("Negative")
    void negativeTest() {
        assertEquals(true, false);
    }

    @Test
    @Tag("Positive")
    void positiveHamcrestTest() {
        assertThat(true, is(true));
    }

    @Test
    @Tag("Negative")
    void negativeHamcrestTest() {
        assertThat(true, is(false));
    }

    @Test
    @Tag("Positive")
    @DisplayName("Positive Hamcrest Test with Steps")
    void successHamcrestStepsTest() {
        step("Assert that true is true", () ->
                assertThat(true, is(true)));
    }

    @Test
    @Tag("Negative")
    @DisplayName("Negative Hamcrest Test with Steps")
    void negativeHamcrestStepsTest() {
        step("Assert that true is false", () ->
                assertThat(true, is(false)));
    }
}