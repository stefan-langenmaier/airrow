#!/bin/bash

# data structure

# airrow - uuid
## refCode
## location
## status
## relation entity
## updatedAt
#-
## refCode
## location
## status
## relation entity
## updatedAt
## creator
## permanent
## mimeType
## fileHash
#-
## relation rating
## updatedAt
## creator
## selfReference
## rating

# airrow-trajectories - internal
## creator
## location
## status
## updatedAt

# could be split into file and point
# many points can share the same file
# airrow-points - uuid
## refCode
## creator
## location
## status
## updatedAt
## permanent
## mimeType
## fileHash
## fileName

# airrow-ratings - internal
## creator
## entity
## status
## rating

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
          "max_gram": 2
        }
      }
    },
    "index" : {
      "number_of_shards" : 1,
      "number_of_replicas" : 0
    }
  },
  "mappings": {
    "_doc": {  
      "properties": {
        "location": {
          "type": "geo_point"
        },
        "status": {
          "type": "text",
          "analyzer": "status_analyzer"
        },
        "relation": {
          "type": "join",
          "relations": {
            "entity": "rating"
          }
        },
        "updatedAt": {
            "type":   "date",
            "format": "basic_date_time"
        },
        "creator": {
          "type": "keyword"
        },
        "refCode": {
          "type": "keyword"
        },
        "selfReference": {
          "type": "boolean"
        },
        "permanent": {
          "type": "boolean"
        },
        "mimeType": {
          "type": "keyword"
        },
        "path": {
          "type": "keyword"
        },
        "rating": {
          "type": "keyword"
        }
      }
    }
  }
}
'

curl -X PUT "localhost:9200/airrow-trajectories?pretty" -H 'Content-Type: application/json' -d'
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
          "max_gram": 2
        }
      }
    },
    "index" : {
      "number_of_shards" : 2,
      "number_of_replicas" : 0
    }
  },
  "mappings": {
    "_doc": {  
      "properties": {
        "uuid": {
          "type": "keyword"
        },
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

curl -X PUT "localhost:9200/airrow-points?pretty" -H 'Content-Type: application/json' -d'
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
      "number_of_shards" : 2,
      "number_of_replicas" : 0
    }
  },
  "mappings": {
    "_doc": {  
      "properties": {
        "creator": {
          "type": "keyword"
        },
        "refCode": {
          "type": "keyword"
        },
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
        },
        "permanent": {
          "type": "boolean"
        },
        "mimeType": {
          "type": "keyword"
        },
        "fileHash": {
          "type": "keyword"
        },
        "fileName": {
          "type": "keyword"
        }
      }
    }
  }
}
'

curl -X PUT "localhost:9200/airrow-ratings?pretty" -H 'Content-Type: application/json' -d'
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
          "max_gram": 2
        }
      }
    },
    "index" : {
      "number_of_shards" : 2,
      "number_of_replicas" : 0
    }
  },
  "mappings": {
    "_doc": {  
      "properties": {
        "creator": {
          "type": "keyword"
        },
        "entity": {
          "type": "keyword"
        },
        "rating": {
          "type": "keyword"
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
                                },
                                {
                                    "has_child" : {
                                        "type" : "rating",
                                        "query" : {
                                            "term": {
                                                "creator": "{{self}}"
                                            }
                                        }
                                    }
                                },
                                {
                                    "term": {
                                        "creator": "{{self}}"
                                    }
                                }
                            ]
                        }
                    },
                    "functions": [
                        {
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
                        },
                        {
                            "linear": {
                                "location": {
                                    "origin": {
                                        "lat": "{{location.latitude}}",
                                        "lon": "{{location.longitude}}"
                                    },
                                    "scale": "{{walkDistance}}",
                                    "offset": "0m",
                                    "decay": "{{scale}}"
                                }
                            }
                        }
                    ],
                    "score_mode": "sum"
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

curl -X PUT "localhost:9200/airrow/_doc/77c03aa8-f71b-4cfa-af73-6b1f1519041b" -H 'Content-Type: application/json' -d'
{
  "permanent": true,
  "location": {
    "lat": 48,
    "lon": 10
  },
  "status": "🥰",
  "updatedAt": "20210131T230315.059+0000"
}
'

curl -X POST "localhost:9200/airrow/_doc?routing=1" -H 'Content-Type: application/json' -d'
{
  "creator": "77c03aa8-f71b-4cfa-af73-6b1f1519041b",
  "relation": {
    "name": "rating",
    "parent": "_TaCjXcBgebpGectFU9Z"
  },
  "rating": "3",
  "updatedAt": "20210131T230315.059+0000"
}
'

curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21701' -H 'Content-Type: application/json' -d'{"refCode": "r01", "relation": "entity","permanent": true,"location": {"lat": 48.331950,"lon": 10.866627},"status": "🍺🌽","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21702' -H 'Content-Type: application/json' -d'{"refCode": "r02", "relation": "entity","permanent": true,"location": {"lat": 48.341910,"lon": 10.869792},"status": "🎭🎵","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21703' -H 'Content-Type: application/json' -d'{"refCode": "r03", "relation": "entity","permanent": true,"location": {"lat": 48.342486,"lon": 10.868475},"status": "📚🕮","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21704' -H 'Content-Type: application/json' -d'{"refCode": "r04", "relation": "entity","permanent": true,"location": {"lat": 48.340299,"lon": 10.865330},"status": "⚕️🚑","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21705' -H 'Content-Type: application/json' -d'{"refCode": "r05", "relation": "entity","permanent": true,"location": {"lat": 48.339696,"lon": 10.864436},"status": "⛲🏰","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21706' -H 'Content-Type: application/json' -d'{"refCode": "r06", "relation": "entity","permanent": true,"location": {"lat": 48.339583,"lon": 10.857775},"status": "🛷🌄","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21707' -H 'Content-Type: application/json' -d'{"refCode": "r07", "relation": "entity","permanent": true,"location": {"lat": 48.341600,"lon": 10.859079},"status": "🏊⛱️","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21708' -H 'Content-Type: application/json' -d'{"refCode": "r08", "relation": "entity","permanent": true,"location": {"lat": 48.338283,"lon": 10.855744},"status": "🐟💦","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21709' -H 'Content-Type: application/json' -d'{"refCode": "r09", "relation": "entity","permanent": true,"location": {"lat": 48.331711,"lon": 10.853194},"status": "🏝️⏸️","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21710' -H 'Content-Type: application/json' -d'{"refCode": "r10", "relation": "entity","permanent": true,"location": {"lat": 48.328292,"lon": 10.850056},"status": "🏞️🎄","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21711' -H 'Content-Type: application/json' -d'{"refCode": "r11", "relation": "entity","permanent": true,"location": {"lat": 48.322270,"lon": 10.850421},"status": "🪑 💺","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21712' -H 'Content-Type: application/json' -d'{"refCode": "r12", "relation": "entity","permanent": true,"location": {"lat": 48.323308,"lon": 10.851853},"status": "🌳🌲","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21713' -H 'Content-Type: application/json' -d'{"refCode": "r13", "relation": "entity","permanent": true,"location": {"lat": 48.326383,"lon": 10.857974},"status": "⛳🎪","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21714' -H 'Content-Type: application/json' -d'{"refCode": "r15", "relation": "entity","permanent": true,"location": {"lat": 48.330788,"lon": 10.856520},"status": "🐴🐎","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21715' -H 'Content-Type: application/json' -d'{"refCode": "r15", "relation": "entity","permanent": true,"location": {"lat": 48.331219,"lon": 10.859835},"status": "🌉🌊","updatedAt": "20210131T230315.059+0000"}'
curl -X PUT 'localhost:9200/airrow/_doc/41e183c6-57fd-423f-acba-5cd365a21716' -H 'Content-Type: application/json' -d'{"refCode": "r16", "relation": "entity","permanent": true,"location": {"lat": 48.333066,"lon": 10.864942},"status": "🌿🏘️","updatedAt": "20210131T230315.059+0000"}'
