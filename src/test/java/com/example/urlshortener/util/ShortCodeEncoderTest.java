package com.example.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShortCodeEncoderTest {
    @Test
    void encode_shouldEncodeSmallPositiveNumber() {
        String result = ShortCodeEncoder.encodeId(1L);
        assertEquals("1", result);
    }

    @Test
    void encode_shouldHandleBase62Boundary() {
        String result = ShortCodeEncoder.encodeId(62L);
        assertEquals("10", result);
    }

    @Test
    void encode_shouldEncodeLargerNumber() {
        String result = ShortCodeEncoder.encodeId(125L);
        assertEquals("21", result);
    }

    @Test
    void encode_zeroShouldReturnZero() {
        String result = ShortCodeEncoder.encodeId(0L);
        assertEquals("0", result);
    }

    @Test
    void encode_negativeNumberShouldThrowException() {
        try {
            ShortCodeEncoder.encodeId(-1L);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected exception, test passes
        }
    }
}
