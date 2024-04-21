docker run -dp 5432:5432 --name topfive-db -e POSTGRES_PASSWORD=password postgres
docker start topfive-db