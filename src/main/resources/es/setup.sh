#!/bin/bash

curl -X PUT "localhost:9200/airrow?pretty" -H 'Content-Type: application/json' -d'
{
  "settings": {
    "analysis": {
      "analyzer": {
        "status_analyzer": {
          "type": "custom", 
          "tokenizer": "status_tokenizer",
          "char_filter": [],
          "filter": []
        }
      },
      "tokenizer": {
        "status_tokenizer": {
          "type": "ngram",
          "min_gram": 1,
          "max_gram": 4
        }
      }
    },
    "index" : {
      "number_of_replicas" : 0
    }
  },
  "mappings": {
    "airrow": {  
      "properties": {
        "location": {
          "type": "geo_point"
        },
        "status": {
          "type": "text",
          "analyzer": "status_analyzer"
        },
        "updatedAt": {
            "type":   "date",
            "format": "basic_date_time"
        }
      }
    }
  }
}
'