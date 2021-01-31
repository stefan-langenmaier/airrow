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
        "permanent": {
          "type": "boolean"
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

curl -X POST "localhost:9200/_scripts/airrow-default-search?pretty" -H 'Content-Type: application/json' -d'
{
    "script": {
        "lang": "mustache",
        "source": {
          "query": {
            "function_score": {
              "query": {
                "bool": {
                  "must": {
                    "match_all": {}
                  },
                  "should": {
                    "match": {
                      "status": {
                        "query": "{{status}}"
                      }
                    }
                  },
                  "filter": [
                    {
                      "geo_distance": {
                        "distance": "300km",
                        "location": {
                          "lat": "{{location.latitude}}",
                          "lon": "{{location.longitude}}"
                        }
                      }
                    },
                    {
                      "bool": {
                        "should": [
                          {
                            "exists": {
                              "field": "permanent"
                            }
                          },
                          {
                            "range": {
                              "updatedAt": {
                                "gt": "now-{{ttl}}"
                              }
                            }
                          }
                        ]
                      }
                    }
                  ],
                  "must_not": [
                    {
                      "term": {
                        "_id": "{{self}}"
                      }
                    }
                  ]
                }
              },
              "gauss": {
                "location": {
                  "origin": {
                    "lat": "{{location.latitude}}",
                    "lon": "{{location.longitude}}"
                  },
                  "scale": "1000m",
                  "offset": "{{walkDistance}}",
                  "decay": "{{scale}}"
                }
              }
            }
          }

        }
    }
}
'

curl -X POST "localhost:9200/airrow/_search/template?pretty" -H 'Content-Type: application/json' -d'
{
  "id": "airrow-default-search",
  "params": {
    "status": "x",
    "self": "c340394b-fca9-4c41-b773-e3c68f23fed5",
    "location": {
      "latitude": 48.1442,
      "longitude": 11.5328
    },
    "walkDistance": "1000m",
    "scale": 0.5,
    "ttl": "30d"
  }
}
'