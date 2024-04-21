docker build -t shutovna/topfive .
docker network create topfive-network
docker run -dp 5432:5432 --rm --net topfive-network --name topfive-db -e POSTGRES_PASSWORD=password postgres
docker run -dp 8080:8080 --rm --net topfive-network --name topfive-web shutovna/topfive
docker exec -i -t topfive-web bash