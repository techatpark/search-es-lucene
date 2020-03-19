#!/bin/bash

[[ `diff nginx_blue.conf nginx.conf` ]] &&  
   (cp nginx_blue.conf nginx.conf && echo "Blue") ||
   (cp nginx_green.conf nginx.conf && echo "Green")
   
docker restart production_nginx