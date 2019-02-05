CONTAINER_NAME="postgres-docker"

docker rm -f ${CONTAINER_NAME} || true
docker run --name ${CONTAINER_NAME} -p 5432:5432 -d postgres

sleep 2
psql -h localhost -p 5432 -U postgres -f ./create-db.sql

echo 'Bootstrapping complete';

docker ps
