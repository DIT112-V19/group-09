#include <Smartcar.h>

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

const int LEFT_ODOMETER_PIN = 2;
const int RIGHT_ODOMETER_PIN = 3;
const int TRIGGER_PIN = 7; // Trigger Pin of Ultrasonic Sensor
const int ECHO_PIN = 6; // Echo Pin of Ultrasonic Sensor
const unsigned int MAX_DISTANCE = 20;
SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

int SPEED = 20;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
}

void loop() {
   //printSonarInCm(sonar_ping, 300);
  
  int obstacle = front.getDistance();
  if(obstacle < MAX_DISTANCE){
    car.setSpeed(0);
  }else{
      car.setSpeed(SPEED);
  }
  
}
