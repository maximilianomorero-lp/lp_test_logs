package main

import (
	"crypto/rand"
	"encoding/json"
	"fmt"
	"math/big"
	"net/http"
	"os"
	"time"
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

func newUUID() string {
	b := make([]byte, 16)
	rand.Read(b)
	return fmt.Sprintf("%08x-%04x-%04x-%04x-%012x", b[0:4], b[4:6], b[6:8], b[8:10], b[10:])
}

func randInt(max int) int {
	n, _ := rand.Int(rand.Reader, big.NewInt(int64(max)))
	return int(n.Int64())
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
			traceID := fmt.Sprintf("%d", randInt(100000))
			internalID := newUUID()
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

	logJSON("INFO", "", "", "Starting server on :8080")
	if err := http.ListenAndServe(":8080", loggingMiddleware(mux)); err != nil {
		logJSON("ERROR", "", "", fmt.Sprintf("Server failed: %s", err))
		os.Exit(1)
	}
}
