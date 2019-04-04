#include <Smartcar.h>

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

const int leftOdometer = 2;
const int rightOdometer = 3;

const int pingPin = 7; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 6; // Echo Pin of Ultrasonic Sensor

int STOP_DISTANCE = 10;
int SPEED = 50;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
}

void loop() {
   //printSonarInCm(sonar_ping, 300);
  
  
  long sonar_ping = fireSonar();
  if(microsecondsToCentimeters(sonar_ping) < STOP_DISTANCE){
    car.setSpeed(0);
  }else{
      car.setSpeed(SPEED);
  }
  
}

void printSonarInCm(long sonar_ping, long pause_duration){
  long cm = microsecondsToCentimeters(sonar_ping);
  Serial.print(cm);
  Serial.print(" cm");
  Serial.println();
  delay(pause_duration);
}

long fireSonar(){
   pinMode(pingPin, OUTPUT);
   digitalWrite(pingPin, LOW);
   delayMicroseconds(2);
   digitalWrite(pingPin, HIGH);
   delayMicroseconds(10);
   digitalWrite(pingPin, LOW);
   pinMode(echoPin, INPUT);
   return pulseIn(echoPin, HIGH);
}

long microsecondsToCentimeters(long microseconds) {
   return microseconds / 29 / 2;
}
