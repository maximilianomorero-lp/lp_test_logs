package com.localpayment.lp_test_logs.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionHandlerTest {

    private final CustomExceptionHandler handler = new CustomExceptionHandler();

    @Test
    void testHandleException() {
        Exception ex = new Exception("test error");
        ResponseEntity<Void> response = handler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, ex.getStackTrace().length);
    }

    @Test
    void testGetStackTraceAsStringWithNull() {
        String result = handler.getStackTraceAsString(null);
        assertEquals("", result);
    }

    @Test
    void testGetStackTraceAsStringWithEmptyTrace() {
        Exception ex = new Exception("test");
        ex.setStackTrace(new StackTraceElement[0]);
        String result = handler.getStackTraceAsString(ex);
        assertEquals("", result);
    }

    @Test
    void testGetStackTraceAsStringWithException() {
        Exception ex = new Exception("test error");
        String result = handler.getStackTraceAsString(ex);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("test error"));
    }
}
