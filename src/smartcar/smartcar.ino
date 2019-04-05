#include <Smartcar.h>

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

const int leftOdometer = 2;
const int rightOdometer = 3;

const int trigPin = 6; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 7; // Echo Pin of Ultrasonic Sensor

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPin, echoPin, MAX_DISTANCE);

int SPEED = 20;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
}

void loop()
{
  //printSonarInCm(sonar_ping, 300);
  int sonar_ping = front.getDistance();
  if(sonar_ping <= 20 && sonar_ping != 0)
  {
    car.setSpeed(0);
  }
  else
  {
    car.setSpeed(SPEED);
  }
  
}
