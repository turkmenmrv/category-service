CONTAINER_NAME="postgres-docker"

docker rm -f ${CONTAINER_NAME} || true
docker run --name ${CONTAINER_NAME} -p 5432:5432 -d postgres

sleep 3
docker cp ./create-db.sql postgres-docker:/docker-entrypoint-initdb.d/create-db.sql

docker exec -u postgres postgres-docker psql postgres postgres -f docker-entrypoint-initdb.d/create-db.sql

echo 'Bootstrapping complete';

docker ps
