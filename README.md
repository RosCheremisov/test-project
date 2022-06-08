##Run POSTGRES server

docker run --name test-project-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=test_project_db -e POSTGRES_USER=postgres -d -p5432:5432 postgres

##Run Redis server

docker-compose up