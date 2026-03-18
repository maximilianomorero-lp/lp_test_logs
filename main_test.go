package main

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

func TestPingHandler(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/ping", nil)
	w := httptest.NewRecorder()

	pingHandler(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("expected status 200, got %d", w.Code)
	}
	if !strings.Contains(w.Body.String(), "pong") {
		t.Errorf("expected body to contain 'pong', got %s", w.Body.String())
	}
}

func TestExceptionHandler(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/exception", nil)
	w := httptest.NewRecorder()

	exceptionHandler(w, req)

	if w.Code != http.StatusInternalServerError {
		t.Errorf("expected status 500, got %d", w.Code)
	}
}

func TestHealthHandler(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/health", nil)
	w := httptest.NewRecorder()

	healthHandler(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("expected status 200, got %d", w.Code)
	}
	if !strings.Contains(w.Body.String(), "UP") {
		t.Errorf("expected body to contain 'UP', got %s", w.Body.String())
	}
}

func TestLoggingMiddleware(t *testing.T) {
	called := false
	next := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		called = true
		traceID, internalID := getHeaders(r)
		if traceID != "test-trace" {
			t.Errorf("expected trace_id 'test-trace', got '%s'", traceID)
		}
		if internalID != "test-internal" {
			t.Errorf("expected internal_id 'test-internal', got '%s'", internalID)
		}
	})

	req := httptest.NewRequest(http.MethodGet, "/ping", nil)
	req.Header.Set("x-trace-id", "test-trace")
	req.Header.Set("x-internal-id", "test-internal")
	w := httptest.NewRecorder()

	loggingMiddleware(next).ServeHTTP(w, req)

	if !called {
		t.Error("next handler was not called")
	}
}
