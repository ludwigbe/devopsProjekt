FROM nginx:alpine


# Optional: eigene Konfiguration hier
COPY api_nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
