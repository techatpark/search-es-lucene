#!/bin/bash
# Tested using bash version 4.1.5
read number
for i in `seq 1 $number`  
do 
curl --request PUT \
  --url http://localhost:8080/employee/_doc/$i \
  --header 'content-type: application/json' \
  --data '{
	"name": "thirumurugn",
	"age": '$i'
}'
   # your-unix-command-here
   
done