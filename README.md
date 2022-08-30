# jh123
Local dev setup:
- mysql and elasticsearch in docker-compose containers
- from project root

docker-compose -f src/main/docker/mysql.yml -f src/main/docker/elasticsearch.yml up --remove-orphans

and then start application 

./mvnw -Dspring.profiles.active=dev

The API documentation is accessible via 

http://localhost:8080/v3/api-docs/

or 

http://localhost:7742/ (first must start swagger.yml docker profile)

The recipe query endpoint supports elasticsearch compatible query syntax, for example

localhost:8080/api/_search/recipes?query=(ingredients:(shrimps AND NOT chicken)) AND (instructions:saute) AND (category:noodles) AND (servings:2) 

Also, application endpoints support recipe CRUD operations in MYSQL database, shadowed by Elasticsearch index
