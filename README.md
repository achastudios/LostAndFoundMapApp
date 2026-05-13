# Lost And Found Map App

## Overview

Lost And Found Map App is an Android mobile application developed for SIT305 Task 7.1P and Task 9.1P.

The application allows users to:
- Create lost or found item adverts
- Save adverts locally using a Room Database
- Store item details including:
  - item name
  - description
  - phone number
  - date
  - location
  - category
  - image
- Display all adverts in a list
- View detailed advert information
- Remove adverts
- Display saved items on Google Maps
- Show nearby items using radius-based filtering

The project demonstrates:
- Android Activity navigation
- RecyclerView implementation
- Room Database integration
- Google Maps integration
- Location services
- Runtime permissions
- Radius-based geographic searching

---

# Features

## Task 7.1P Features
- Create new lost/found adverts
- Store adverts using Room Database
- View all adverts
- View advert details
- Delete adverts
- Upload item images from gallery
- Filter by Lost or Found items

## Task 9.1P Features
- Google Maps integration
- Current user location detection
- Display item markers on map
- Radius-based item searching
- Nearby item filtering
- Dynamic map marker generation

---

# Technologies Used

- Java
- Android Studio
- Room Database
- RecyclerView
- Google Maps SDK
- Fused Location Provider API
- Material Design Components

---

# How The App Works

## Creating an Advert
Users can:
1. Select Lost or Found
2. Enter item details
3. Select an image
4. Get current location
5. Save the advert into the Room database

## Viewing Items
Users can:
- View all saved adverts
- Open detailed item pages
- Remove adverts from the database

## Map Functionality
Users can:
- View nearby lost/found items on Google Maps
- Enter a search radius in kilometres
- Filter markers based on proximity to their current location

---

# Google Maps Setup

This project uses Google Maps SDK for Android.

Required:
- Google Maps API Key
- Maps SDK for Android enabled in Google Cloud Console

---

# Testing

The application was tested using:
- Android Emulator
- Android API 36
- Google Maps functionality
- Room database persistence
- RecyclerView list rendering
- Location-based filtering

---

# Author
Patrick Acha
