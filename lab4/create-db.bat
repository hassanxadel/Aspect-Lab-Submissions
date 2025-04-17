@echo off
echo Creating database "service-room" if it doesn't exist...
docker exec -it room-pg psql -U postgres -c "CREATE DATABASE \"service-room\" WITH OWNER postgres ENCODING 'UTF8';"
echo Database creation attempt completed. Any errors about the database already existing can be ignored. 