# Group 09 (Team 🐿)
Showcase video found at https://youtu.be/Bo9d2Y1cg9M


## What?   
A room mapping device that measures the dimensions of a room or a space within a room using an intuitive mobile application as the interface. Real time measurements are shown to the user with the car mapping. The measuring process is automated, while if the user wants to measure a distance that is not a full room (for example, one wall or part of a wall), they have the possibility of controlling the car manually for a specific distance.


## Why?   
Makes it easier to visualise the plan of a room for renovating purposes or floor plans, for users such as real estate agents or home owners. The process is more convenient than manually measuring and it is also quick to create the room scheme, as opposed to sketching it by hand.


## How?   
Arduino IDE/Visual Studio Code was used to implement the features of the car, while Android Studio was used to develop the application which serves as an interface for the car. The car uses two ultrasonic sensors and the two car odometers (left and right) to measure the length of a traversed wall, as well as keep within a fixed distance from the wall while covering it in length. Using a mobile application, the traversal of one or multiple walls can be visualized as a mapping on the phone screen (this was implemented using the gyroscope and trigonometric functions to process the trajectory, including the angles). It is also possible to control the car manually via holding and swiping for movements.


## Components used:  
### Hardware
* Android Phone (used to run mobile application which serves as interface for the car)
* Ultra Sonic Sensor (x2) (reads distances from said wall)  
* Odometers (x2) (measures travelled distance)
* Bluetooth Module (provides connectivity with mobile interface)  
* Arduino Mega board   
* Gyroscope Module (used in the mapping of the car trajectory)  
### Software   
* Android Studio (used to program a Kotlin mobile application to serve as an interface between the car and the user - to use manually or start the automated sequence)
* Arduino IDE/Visual Studio Code (IDEs in which Dimitris Platis' SmartCar library was used to implement the car's driving logic)    


## Setup and Getting Started
In order to start using the car, the user needs to have the mobile application DORA on their Android device. The user has to pair their device with the car (the Bluetooth they have to look for is called DIT112) in the phone settings. 
Once the two are paired, the user can run the application. In the application, a button called 'CONNECT VIA BLUETOOTH' should be clicked. Once connected, the app can fully be used by the user - the initial screen is that of the automated sequence to measure and map the room (with buttons START and STOP). The manual control can be accessed by switching to the tab on the bottom (Manual Controlling).
