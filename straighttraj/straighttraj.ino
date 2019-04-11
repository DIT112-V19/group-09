#include <Smartcar.h>

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);
DirectionlessOdometer leftOdometer(110);
DirectionlessOdometer rightOdometer(120);

DistanceCar car(control, leftOdometer, rightOdometer);

//GY50 gyro(10);
const int leftOdometerPin = 2;
const int rightOdometerPin = 3;
const int trigPinFront = 6; // Front Trigger Pin of Ultrasonic Sensor
const int echoPinFront = 7; // Front Echo Pin of Ultrasonic Sensor
const int trigPinSide = 10; // Side Trigger Pin of Ultrasonic Sensor
const int echoPimSide = 9; // Side Echo Pin of Ultrasonic Sensor

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPinFront, echoPinFront, MAX_DISTANCE);
SR04 side(trigPinSide, echoPinSide, MAX_DISTANCE);

int SPEED = 30;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
  car.enableCruiseControl();
}

void loop()
{
handleObstacle();
handleSideMovements()

}

void handleObstacle()
{
  int distance = front.getDistance();
  if(distance <= 40 && distance != 0)
  {
    car.setSpeed(30);
    //car.setAngle(180);
    
  }
  else
  {
    car.setSpeed(SPEED);
  }
  
}

void handleSideMovements()
{
  int wallDistance = 30;
  int distance = side.getDistance();
  if(distance <= wallDistance && distance != 0)
  {
    ///TODO: turn left
  }
  else if(distance > wallDistance)
  {
    ///TODO: turn right
  }else{
    //MALFUNCTION !?
  }
  
}
