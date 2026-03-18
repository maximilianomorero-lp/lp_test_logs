package main

import (
	"encoding/json"
	"fmt"
	"math/rand"
	"net/http"
	"os"
	"time"

	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

type contextKey string

const (
	traceIDKey    contextKey = "trace_id"
	internalIDKey contextKey = "internal_id"
)

var containerName = getEnv("CONTAINER_NAME", "default")

func getEnv(key, def string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return def
}

type logEntry struct {
	ContainerName string `json:"container_name"`
	TraceID       string `json:"trace_id"`
	InternalID    string `json:"internal_id"`
	Level         string `json:"level"`
	Message       string `json:"message"`
}

func logJSON(level, traceID, internalID, message string) {
	entry := logEntry{
		ContainerName: containerName,
		TraceID:       traceID,
		InternalID:    internalID,
		Level:         level,
		Message:       message,
	}
	b, _ := json.Marshal(entry)
	fmt.Println(string(b))
}

func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		traceID := r.Header.Get("x-trace-id")
		internalID := r.Header.Get("x-internal-id")

		r.Header.Set("X-Trace-ID-Ctx", traceID)
		r.Header.Set("X-Internal-ID-Ctx", internalID)

		next.ServeHTTP(w, r)
	})
}

func getHeaders(r *http.Request) (string, string) {
	return r.Header.Get("X-Trace-ID-Ctx"), r.Header.Get("X-Internal-ID-Ctx")
}

func pingHandler(w http.ResponseWriter, r *http.Request) {
	traceID, internalID := getHeaders(r)
	logJSON("INFO", traceID, internalID, "GET /ping")

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"message": "pong"})
}

func exceptionHandler(w http.ResponseWriter, r *http.Request) {
	traceID, internalID := getHeaders(r)
	logJSON("ERROR", traceID, internalID, "[log_name: exception] test exception triggered")

	w.WriteHeader(http.StatusInternalServerError)
}

func healthHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"status": "UP"})
}

func startCron() {
	ticker := time.NewTicker(5 * time.Second)
	go func() {
		for range ticker.C {
			traceID := fmt.Sprintf("%d", rand.Intn(100000))
			internalID := uuid.New().String()
			logJSON("INFO", traceID, internalID, "[name_log: test_log] esto es un log de test")
		}
	}()
}

func main() {
	startCron()

	mux := http.NewServeMux()
	mux.HandleFunc("/ping", pingHandler)
	mux.HandleFunc("/exception", exceptionHandler)
	mux.HandleFunc("/health", healthHandler)
	mux.Handle("/metrics", promhttp.Handler())

	logJSON("INFO", "", "", "Starting server on :8080")
	if err := http.ListenAndServe(":8080", loggingMiddleware(mux)); err != nil {
		logJSON("ERROR", "", "", fmt.Sprintf("Server failed: %s", err))
		os.Exit(1)
	}
}
