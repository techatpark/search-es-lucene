#!/bin/bash

[[ `diff nginx_blue.conf nginx.conf` ]] &&  
   (cp nginx_blue.conf nginx.conf && echo "Blue") ||
   (cp nginx_green.conf nginx.conf && echo "Green")

docker exec production_nginx nginx -s reload

curl http://localhost:8080/