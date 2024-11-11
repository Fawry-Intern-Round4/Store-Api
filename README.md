# Store Service

The `Store Service` handles store management, product stock, and product consumption across various stores. It is part of the organization's microservices architecture and provides APIs for managing stores and their stock and consumption details.

## Overview

The `Store Service` provides functionality for:
- Creating and retrieving store information.
- Managing product stock and consumption.
- Retrieving product consumption data.
- Validating stock items.

## Endpoints

### Create Store

- **URL**: `/store`
- **Method**: `POST`
- **Description**: Creates a new store.
- **Request Body**: 
  ```json
  {
    "name": "string",
    "location": "string"
  }
  ```
- **Response**: 
  ```json
  {
    "id": "long",
    "name": "string",
    "location": "string"
  }
  ```
- **Response Code**: `201 Created`

---

### Get All Stores

- **URL**: `/store`
- **Method**: `GET`
- **Description**: Retrieves all stores.
- **Response**: 
  ```json
  [
    {
      "id": "long",
      "name": "string",
      "location": "string"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Get Store by ID

- **URL**: `/store/{id}`
- **Method**: `GET`
- **Description**: Retrieves details of a specific store by ID.
- **Path Variable**: `id` (Long)
- **Response**: 
  ```json
  {
    "id": "long",
    "name": "string",
    "location": "string"
  }
  ```
- **Response Code**: `200 OK`

---

### Get All Products by Store ID

- **URL**: `/store/{id}/product`
- **Method**: `GET`
- **Description**: Retrieves all products available at a specific store.
- **Path Variable**: `id` (Long)
- **Response**: 
  ```json
  [
    {
      "id": "long",
      "code": "string",
      "name": "string",
      "description": "string",
      "price": "double",
      "image": "string"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Add Stock

- **URL**: `/stock`
- **Method**: `POST`
- **Description**: Adds stock to the store for a specific product.
- **Request Body**:
  ```json
  {
    "productId": "long",
    "storeId": "long",
    "quantity": "int"
  }
  ```
- **Response**: 
  ```json
  {
    "id": "long",
    "productId": "long",
    "storeId": "long",
    "quantity": "int",
    "dateAdded": "string"
  }
  ```
- **Response Code**: `201 Created`

---

### Validate Stock

- **URL**: `/stock/validation`
- **Method**: `POST`
- **Description**: Validates the stock of multiple products.
- **Request Body**:
  ```json
  [
    {
      "productId": "long",
      "storeId": "long",
      "quantity": "int"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Consume Stock

- **URL**: `/stock/consumption`
- **Method**: `PUT`
- **Description**: Consumes stock for specific products in the store.
- **Request Body**:
  ```json
  [
    {
      "productId": "long",
      "storeId": "long",
      "quantity": "int"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Get All Product Consumptions

- **URL**: `/store/consumption`
- **Method**: `GET`
- **Description**: Retrieves all product consumptions across all stores.
- **Response**:
  ```json
  [
    {
      "id": "long",
      "productId": "long",
      "store": {
        "id": "long",
        "name": "string",
        "location": "string"
      },
      "quantityConsumed": "int",
      "dateConsumed": "datetime"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Get Product Consumptions by Store ID

- **URL**: `/store/consumption?storeId={storeId}`
- **Method**: `GET`
- **Description**: Retrieves product consumption details for a specific store.
- **Query Param**: `storeId` (Long)
- **Response**:
  ```json
  [
    {
      "id": "long",
      "productId": "long",
      "store": {
        "id": "long",
        "name": "string",
        "location": "string"
      },
      "quantityConsumed": "int",
      "dateConsumed": "datetime"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

## Configuration

- Configure the database connection in the `application.properties` file:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_db_username
  spring.datasource.password=your_db_password
  ```
