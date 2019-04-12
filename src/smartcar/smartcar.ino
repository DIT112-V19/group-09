#include <Smartcar.h>
#include <SoftwareSerial.h>

SoftwareSerial BTserial(15, 14); // RX | TX

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);
DirectionlessOdometer leftOdometer(110);
DirectionlessOdometer rightOdometer(120);

DistanceCar car(control, leftOdometer, rightOdometer);

//const int leftOdometer = 2;
//const int rightOdometer = 3;

const int trigPin = 4; // Front Trigger Pin of Ultrasonic Sensor
const int echoPin = 5; // Front Echo Pin of Ultrasonic Sensor

const int trigPinSide = 6; // Side Trigger Pin of Ultrasonic Sensor
const int echoPinSide = 7; // Side Echo Pin of Ultrasonic Sensor

int state = 0;

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPin, echoPin, MAX_DISTANCE);
SR04 side(trigPinSide, echoPinSide, MAX_DISTANCE);

int SPEED = 30;
int frontDistance;
int sideDistance;
int wallDistance =10;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
  BTserial.begin(9600);
}

void loop()
{
  frontDistance = front.getDistance();
  sideDistance = side.getDistance();
  //Serial.print(frontDistance);
  //Serial.print(" ");
  //Serial.println(sideDistance);
  //handleBluetooth();
  movement();
}

void movement()
{  
  if(frontDistance < 20 && frontDistance != 0) {
    //Serial.println("stopped");
    car.setSpeed(0);
  }
  else if(sideDistance <= (wallDistance + 3) && sideDistance >= wallDistance) 
  {
    driveF();
    //Serial.println("front");
  }
  else if(sideDistance < wallDistance && sideDistance != 0)
  {
    //Serial.println("left");
    driveL();
  }
  else if(sideDistance > (wallDistance + 3) && sideDistance != 0)
  {
    //Serial.println("right");
    driveR();
  }
}

void handleBluetooth(){

  BTserial.print("HI MOM");
  delay(50);

}

void driveF()
{
  if(car.getSpeed() != SPEED)
  car.setSpeed(SPEED);
}

void driveB()
{
  car.setSpeed(-SPEED);
}

void driveR()
{
  car.setAngle(20);
  driveF();
}

void driveL()
{
  car.setAngle(-20);
  driveF();
}
