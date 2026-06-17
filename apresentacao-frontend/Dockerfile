FROM node:22-alpine AS build

WORKDIR /app

COPY apresentacao-frontend/src/main/angular/package*.json ./
RUN npm ci

COPY apresentacao-frontend/src/main/angular/ ./
RUN npm run build

FROM nginx:1.27-alpine

COPY apresentacao-frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/gestao-torneios-frontend/browser /usr/share/nginx/html

EXPOSE 80
