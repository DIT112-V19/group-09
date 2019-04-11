#include <Smartcar.h>
#include <SoftwareSerial.h>

SoftwareSerial BTserial(15, 14); // RX | TX

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);
DirectionlessOdometer leftOdometer(110);
DirectionlessOdometer rightOdometer(120);

DistanceCar car(control, leftOdometer, rightOdometer);

const int leftOdometer = 2;
const int rightOdometer = 3;

const int trigPin = 6; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 7; // Echo Pin of Ultrasonic Sensor

int state = 0;

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPin, echoPin, MAX_DISTANCE);

int SPEED = 20;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
  BTserial.begin(9600);
  car.enableCruiseControl();
}

void loop()
{
  
  int distance = front.getDistance();
  if(distance <= 20 && distance != 0)
  {
    car.setSpeed(0);
  }
  else
  {
    car.setSpeed(SPEED);
  }

  handleBluetooth();
  
}

void handleBluetooth(){

  BTserial.print("HI MOM");
  delay(50);

}
