# Project Title
Feefo Coding Software Engineering Technical Assessment

## Overview
This project provides and endpoint for job title normalisation

## Features
- Automatically imports datasets from resources folder on startup. (configurable in application.yaml)
- Provides an endpoint for normalising job titles using Elasticsearch fuzzy matching
- Extendable and reusable interfaces for data sets and matching types

## Endpoints
POST /normalise
Request:
{
  "text": "C# engineer",
  "type": "JOB_TITLE"
}
Response:
{
    "text": "Software engineer"
}

### Prerequisites
- Java 21
- Apache Maven 3.9+
- Docker (for Elasticsearch)

### Installation
# Clone
git clone https://github.com/sam0verboard/Feefo.git
cd repo

# Start Elasticsearch
docker compose up -d

# Run the application
mvn spring-boot:run

# Test
Application must have been started to import data before running tests
mvn test

## Adding more datasets
1. Add dataset config to application.yaml
        futureDataset:
          importOnStartup: true
          filename: "filename"
          jsonRoot: "root"
          indexName: "future_index"
2. Implement Normaliser class for dataset with desired matcher. Ensure INDEX and FIELD match config
3. Add type to NormalisationInputType enum
4. Add case for type in switch statement in Normalisation service
5. Add dataset to resources folder, restart to import