MyFlights
=========

Android Application using FlightXML 2.0 API to manage personal flights

INSTALLATION: Current Version Still in Development - NO APK available to download straight to Android Device Yet

1. Pull files to a local directory and point your IDE with Android SDK installed to build project.
2. Run the Android application in debug mode.

Application Flow:
=================

Application starts an activity that displays your current upcoming flights by default. Adding a new flight opens up a
new activity to imput flight information. Once submitted, the API is called to find the flight and display info on main 
activity.

There is preferences fragment to choose to show flights in the past.

A service runs every 15 minutes via a alarmManager to check to see if flights have happened which are marked as deleted. 
This will be changed to a regular service that runs every 5 minutes as there is no need to run radio for this 
service as more services are added that will make other calls to API.

Database Schema:
================

All data is currently stored internally within application in a SQLite DB.

TABLE: flights
COLUMNS: _id (Primary Key int), origin (Foreign Key (airports) int), destination (Foreign Key (airports) int), 
depart (datetime), arrive (datetime), airline (Foreign Key (airlines) int), flight (int), flightxml_enabled (int),
meterex (text), status (int), is_deleted (int)
NOTE: This data comes from user input and 

TABLE: airports
COLUMNS: _id (Primary Key int), airport(text), airport_name(text)
NOTE: This data is stored in assets and added to DB on first install or DB upgrade

TABLE: airlines
COLUMNS: _id (Primary Key int), airline(text), name(text)
NOTE: This data is stored in assets and added to DB on first install or DB upgrade






