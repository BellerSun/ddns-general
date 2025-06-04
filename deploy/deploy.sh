#!/bin/bash

# Stop and remove existing containers
docker-compose -f docker-compose.yml down

# Remove old Docker images
docker pull apache.plus:1612/ddns-general:latest

# Start the services
docker-compose -f docker-compose.yml up -d

# Clean up unused Docker images
docker system prune -af
