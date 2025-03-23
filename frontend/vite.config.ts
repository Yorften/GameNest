import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    {
      name: "set-cors-headers",
      configureServer(server) {
        server.middlewares.use((req, res, next) => {
          if (req.originalUrl && (req.originalUrl.startsWith("/games") || req.originalUrl.startsWith("/builds"))) {       
            res.setHeader("Cross-Origin-Opener-Policy", "same-origin");
            res.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
          }
          next();
        });
      },
    },
  ],
  server: {
    open: true,
  },
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: "src/setupTests",
    mockReset: true,
  },
});
