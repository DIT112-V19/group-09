# Group 09 (Team 🐿)

## What?   
A room mapping device that measures the dimensions of a room or a space within a room using an intuitive mobile application as the interface. Real time measurements are shown to the user with the car mapping. The measuring process is automated, while if the user wants to measure a distance that is not a full room (for example, one wall or part of a wall), they have the possibility of controlling the car manually for a specific distance.


## Why?   
Makes it easier to visualise the plan of a room for renovating purposes or floor plans, for users such as real estate agents or home owners. The process is more convenient than manually measuring and it is also quick to create the room scheme.


## How?   
Arduino IDE/Visual Studio Code was used to implement the features of the car, while Android Studio was used to develop the application which serves as an interface for the car. The car uses two ultrasonic sensors and the two car odometers (left and right) to measure the length of a traversed wall, as well as keep within a fixed distance from the wall while covering it in length. Using a mobile application, the traversal of one or multiple walls can be visualized as a mapping on the phone screen (this was implemented using the gyroscope and trigonometric functions to process the trajectory, including the angles). It is also possible to control the car manually via holding and swiping for movements.


### Components used:  
#### Hardware
* Android Phone
* Ultra Sonic Sensor (x2)  
* Odometers (x2) 
* Bluetooth Module   
* Arduino Mega board   
* Gyroscope Module   
#### Software   
* Android Studio
* Arduino   
