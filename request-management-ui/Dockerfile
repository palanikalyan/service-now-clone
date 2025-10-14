# Use official Node.js image for build stage
FROM node:20 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build -- --output-path=dist/request-management-ui/browser

# Use official Nginx image for serving static files
FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

# Copy built Angular app to Nginx static directory
COPY --from=build /app/dist/request-management-ui/browser/* /usr/share/nginx/html/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
