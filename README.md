# Glovo Android Challenge
This app solves the Glovo Android Challenge.
### Getting Started
- Setup and run the [server](https://github.com/Glovo/glovo-challengemobile) 
- Checkout this project and edit the server url address in the following line of the [app module's build.gradle file](app/build.gradle): 
 ``` buildConfigField "String", "SERVER_BASE_URL", "\"http://10.0.2.2:3000/\""```
- Run the app

### Project Structure
This project was organized using the principles of the [app modularization by features](https://proandroiddev.com/intro-to-app-modularization-42411e4c421e). Although this approach is not necessary for an app as small as this challenge, it is useful to demonstrate how to structure a project that aims to build a robust and scalable application. This is a very common and good practice to keep different teams working on isolated and decoupled features. I divide this application in two "product features": 
- **User City**: This feature is the responsible for getting the code of the city where the user is located, using two strategies: the current user device location, and the available city picker screen which is displayed to the user when the application starts and cannot be able to fetch the device location.
- **Working Area**: The main screen of the app, which loads the map of available zones and the details of the city focused on the map.

In addition to these modules, the project contains the following additional ones:
- **app**: This is the main application module which contains the application class and the navigation logic. The *NavigationActivity* is the responsible for manage the user through the flow composed by all the product features, loading the different features activities.
- **base**: This is a base module which contains architecture base classes and helpers, which are used by the different product modules. 
- **networkdatasource**: This module contains the logic to manage the network communication between the different product modules and the server, bringing a common data source class, which is instantiated by the network repositories of the different product modules.

<img src="./modules.svg">
