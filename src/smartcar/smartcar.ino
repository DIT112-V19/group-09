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

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPin, echoPin, MAX_DISTANCE);
SR04 side(trigPinSide, echoPinSide, MAX_DISTANCE);

enum CARSTATE
{
  STOPPED,
  DRIVING,
  TURN_RIGHT,
  TURN_LEFT
};

CARSTATE state = DRIVING;

int SPEED = 30;
int frontDistance;
int sideDistance;
int wallDistance = 10;

void setup() {
  Serial.begin(9600); // Starting Serial Terminal
  BTserial.begin(9600);
}

void loop()
{
  frontDistance = front.getDistance();
  sideDistance = side.getDistance();
  
  switch(state)
  {
    case STOPPED: 
      Serial.println("Stopped");
      // TODO Fix bad code
      if (frontDistance > 5)
        state = DRIVING;
      car.setSpeed(0);
      break;
    case DRIVING:
      Serial.println("Driving");
      movement();
      break;
    case TURN_LEFT:
      Serial.println("Left");
      turn(-90);
      break;
    case TURN_RIGHT:
      Serial.println("Right");
      turn(90);
      break;
  }

  //Serial.print(frontDistance);
  //Serial.print(" ");
  //Serial.println(sideDistance);
  //handleBluetooth();
  //movement();
}

void movement()
{  
  driveF();
  if(frontDistance <= 5 && frontDistance != 0)
    state = STOPPED;
  else if(frontDistance < 40 && frontDistance != 0)
  {
    if(sideDistance > 50)
    {
      state = TURN_RIGHT;
    }
    else
    {
      state = TURN_LEFT;
    }
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

void turn(int angle)
{
  car.setSpeed(30);
  car.setAngle(angle);
  for(int i = 0; i < 1000; i++)
  {
    if (frontDistance <= 5 && frontDistance != 0)
      state = STOPPED;
    delay(1);
  }
  state = DRIVING;
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
