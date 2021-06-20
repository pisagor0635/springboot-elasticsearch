# springboot-elasticsearch

PostgreSQL on Docker 

docker run --name postgres -e POSTGRES_PASSWORD=1234 -d -p 5432:5432 postgres

docker exec -it containerID bash

psql -U postgres

create database test;

\l -> list the database
\q -> quit

Elastic Search on Docker

docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2

localhost:9200
