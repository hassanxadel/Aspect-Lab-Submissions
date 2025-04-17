#!/bin/bash

# Connect to PostgreSQL and create the database if it doesn't exist
docker exec -it room-pg psql -U postgres -c "CREATE DATABASE \"service-room\" WITH OWNER postgres ENCODING 'UTF8' LC_COLLATE 'en_US.utf8' LC_CTYPE 'en_US.utf8';"

echo "Database creation attempt completed. Any errors about the database already existing can be ignored." 