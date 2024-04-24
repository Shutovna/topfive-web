docker run -dp 5432:5432 --name topfive-db -e POSTGRES_USER=topfive -e POSTGRES_PASSWORD=password -e POSTGRES_DB=topfive postgres
docker start topfive-db