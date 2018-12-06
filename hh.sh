curl -X PUT "localhost:9200/radio/artists/1?pretty" -H 'Content-Type: application/json' -d '{"name": "John Doe","id":"132434","links":{"url":"www.baidu.com","picture":"dddddd"}}'

CREATE EXTERNAL TABLE artists (id BIGINT,name STRING,links STRUCT<url:STRING, picture:STRING>)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler' TBLPROPERTIES('es.resource' = 'radio/artists','es.nodes'='127.0.0.1','es.port'='9200','es.mapping.names' = 'id:id, name:name, links.url:links.url, links.picture:links.picture');



CREATE EXTERNAL TABLE artists (id BIGINT,name STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler' TBLPROPERTIES('es.resource' = 'radio/artists','es.nodes'='127.0.0.1','es.port'='9200','es.mapping.names' = 'id:id, name:name');


CREATE EXTERNAL TABLE artists (
    id      STRING,
    name    STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer/my_doc',
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id,name:name');

CREATE EXTERNAL TABLE artists1 (
    id      STRING,
    name    STRING,
	url     STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer/my_doc',
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id,name:name,url:links.url');

CREATE EXTERNAL TABLE t2 (id STRING,type STRING,index STRING,name STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer',
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id,type:_metadata._type,index:_metadata._index,name:name');

CREATE EXTERNAL TABLE artists1 (
    id      STRING,
    name    STRING,
	url     STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer/my_doc',
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id,name:name,url:links.url');

CREATE EXTERNAL TABLE artists2 (
    id      STRING,
    name    STRING,
	links   ARRAY<STRUCT<url:STRING, picture:STRING>>)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer/my_doc','es.read.field.as.array.include' ='links'
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id');

CREATE EXTERNAL TABLE artists2 (
    id      STRING,
    name    STRING,
	links   ARRAY<STRUCT<url:STRING, picture:STRING>>)
hadoop fs -ls STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
hadoop fs -ls TBLPROPERTIES('es.resource' = 'customer/my_doc','es.read.field.as.array.include' ='links'
hadoop fs -ls 'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id');
hadoop fs -ls 
hadoop fs -ls CREATE EXTERNAL TABLE a (
hadoop fs -ls     message STRING,fffff
hadoop fs -ls     name    ARRAY<STRING>)
hadoop fs -ls STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
hadoop fs -ls TBLPROPERTIES('es.resource' = 'my_index/my_doc',
hadoop fs -ls 'es.nodes'='127.0.0.1','es.port'='9200','es.read.metadata' = 'true','es.read.field.as.array.include' ='lists');




#################################
CREATE EXTERNAL TABLE arrry2 (id STRING,name STRING,url STRING)
STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'customer/my_doc','es.read.field.as.array.include' ='links'
'es.nodes'='127.0.0.1','es.port'='9200','es.index.auto.create' = 'false','es.read.metadata' = 'true','es.mapping.names' = 'id:_metadata._id,name:name,url:links[0].url');
