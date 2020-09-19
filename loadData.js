const data = require("./dataSep-18-2020.json");
const fetch = require('node-fetch');
const Bluebird = require('bluebird');
fetch.Promise = Bluebird;

function load() {
  data.map((d,i) => {
    fetch(`http://localhost:9200/employee/_doc/${i+1}`, { method: 'PUT', body: JSON.stringify(d), headers:{'content-type':'application/json'} })
    .then(res => res.json()) // expecting a json response
    .then(json => console.log(json));
    return d;
  });
}

load();
