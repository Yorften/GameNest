# --- Stage 1: Build React Frontend ---
FROM node:current-alpine AS frontend-builder

ARG API_URL
ARG GITHUB_APP_URL
ARG GAME_BUILDS_URL

ENV VITE_API_URL=$API_URL
ENV VITE_GITHUB_APP_URL=$GITHUB_APP_URL
ENV VITE_GAME_BUILDS_URL=$GAME_BUILDS_URL


WORKDIR /app

COPY frontend/package.json frontend/package-lock.json ./

RUN npm install

COPY frontend/ ./

RUN npm run build

# --- Stage 2: Setup Nginx ---
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=frontend-builder /app/dist /usr/share/nginx/html/frontend

COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# We need to make sure not to run the container as a non root user
# for better security
WORKDIR /app
RUN chown -R nginx:nginx /app && chmod -R 755 /app && \
        chown -R nginx:nginx /var/cache/nginx && \
        chown -R nginx:nginx /var/log/nginx && \
        chown -R nginx:nginx /etc/nginx/conf.d
RUN touch /var/run/nginx.pid && \
        chown -R nginx:nginx /var/run/nginx.pid

USER nginx

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

