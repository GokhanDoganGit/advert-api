# Advert API Project

This project is designed to allow users to submit, manage, and modify the status of their adverts.
Users can enter the advert title, description, and category, and manage the status of these
adverts (Aktif, Deaktif, etc.).
This project is a POC for managing adverts.

## Project Purpose

- Users can submit adverts to the system, manage their statuses, and modify active or deactivated
  adverts.
- Adverts will transition between "Onay Bekliyor" and "Aktif" based on certain rules and timeframes.
- The API allows for statistical information about adverts and status changes to be retrieved.
- The project includes unit tests, containerization, and logging for performance monitoring.

## Technologies Used

- **Java 11**
- **Spring Boot 2.7.18**
- **Maven 3.6+**
- **H2 In-memory Database**
- **Swagger/Postman Documentation**

## API Endpoints

### 1. Create Advert

#### POST `/api/advert`

Used to create a new advert.

- **Request Body**:
    ```json
    {
      "title": "Vehicle for Sale",
      "description": "Vehicle with 4 wheels and 4 doors is open for sale",
      "category": "VASITA"
    }
    ```

### 2. Change Advert Status

#### PATCH `/api/advert/update-advert-status`

Used to change an advert’s status to "Active" or "Deactivated".

- **Request Body**:
    ```json
    {
      "advertId": 1,
      "newStatus": "AKTIF"
    }
    ```

### 3. Advert Statistics

#### GET `/api/advert/statistics`

Retrieves statistical information about the status of all adverts.

- **Response**:
    ```json
    {
      "statusCounts": {
          "AKTIF": 1,
          "MUKERRER": 1,
          "DEAKTIF": 1
      }
    }
    ```

### 4. List Advert Status Changes

#### GET `/api/advert/{advertId}/status-changes`

Lists all status changes for a specific advert.

- **Response**:
    ```json
    {
      "advertId": 1,
      "statusChanges": [
        {
          "oldStatus": null,
          "newStatus": "ONAY_BEKLIYOR",
          "changeDateTime": "25-12-2024T11:51:21"
        },
        {
          "oldStatus": "ONAY_BEKLIYOR",
          "newStatus": "AKTIF",
          "changeDateTime": "25-12-2024T11:51:30"
        },
        {
          "oldStatus": "AKTIF",
          "newStatus": "DEAKTIF",
          "changeDateTime": "25-12-2024T11:51:46"
        }
      ]
    }
    ```

## User Flows

### Advert Title:

- The title must start with a letter (including Turkish characters) or a number.
- The title must be between 10 and 50 characters.
- If any words from the `badwords.txt` file are present, the advert entry is blocked.

### Advert Description:

- The description must be at least 20 characters and no more than 200 characters.
- Special characters can be used in the description.

### Advert Categories:

- **EMLAK**
- **VASITA**
- **ALISVERIS**
- **DIGER**

### Advert Lifecycle:

- **"EMLAK", "VASITA", "DIGER"**: Initially set to "Pending Approval".
- **ALISVERIS**: Set directly to "AKTIF".
- **Expiration Date**: "EMLAK" adverts expire in 4 weeks, "VASITA" adverts in 3 weeks, and
  "ALISVERIS" and "DIGER" adverts in 8 weeks.

### Status Changes:

- Adverts in "ONAY_BEKLIYOR" will become "AKTIF" once approved.
- Users can change the status of their adverts to "DEAKTIF".

### Code Coverage

- Jacoco report is initialized with every time mvn clean build and it can be found /target/site/jacoco/index.html
- Also, we can control the percentage with jacoco plugin in the pom.xml. 

### Running the Application with Docker

- First need to build docker image with "docker build -t advert-api ." in the root folder. 
- Then we need to run docker with "docker run -p 8080:8080 advert-api"
