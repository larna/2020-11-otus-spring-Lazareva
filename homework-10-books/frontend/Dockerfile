FROM nginx:1.11-alpine
COPY /dist/. /usr/share/nginx/html/.
COPY nginx.conf /etc/nginx/conf.d/nginx.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
