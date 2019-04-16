#include <Smartcar.h>
#include <SoftwareSerial.h>

SoftwareSerial BTserial(15, 14); // RX | TX
const int GYROSCOPE_OFFSET = 18;

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);
DirectionlessOdometer leftOdometer(110);
DirectionlessOdometer rightOdometer(120);
GY50 gyro(GYROSCOPE_OFFSET);

SmartCar car(control, gyro, leftOdometer, rightOdometer);

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

void setup()
{
  Serial.begin(9600); // Starting Serial Terminal
  BTserial.begin(9600);
}

void loop()
{
  gyro.update();
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
  car.setSpeed(0);
  rotateOnSpot(angle, 30);
  state = DRIVING;
} 

void handleBluetooth()
{
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

void rotateOnSpot(int targetDegrees, int speed) {
  speed = smartcarlib::utils::getAbsolute(speed);
  targetDegrees %= 360; //put it on a (-360,360) scale
  if (!targetDegrees) return; //if the target degrees is 0, don't bother doing anything
  /* Let's set opposite speed on each side of the car, so it rotates on spot */
  if (targetDegrees > 0) { //positive value means we should rotate clockwise
    car.overrideMotorSpeed(speed, -speed); // left motors spin forward, right motors spin backward
  } else { //rotate counter clockwise
    car.overrideMotorSpeed(-speed, speed); // left motors spin backward, right motors spin forward
  }
  unsigned int initialHeading = car.getHeading(); //the initial heading we'll use as offset to calculate the absolute displacement
  int degreesTurnedSoFar = 0; //this variable will hold the absolute displacement from the beginning of the rotation
  while (abs(degreesTurnedSoFar) < abs(targetDegrees)) { //while absolute displacement hasn't reached the (absolute) target, keep turning
    car.update(); //update to integrate the latest heading sensor readings
    int currentHeading = car.getHeading(); //in the scale of 0 to 360
    if ((targetDegrees < 0) && (currentHeading > initialHeading)) { //if we are turning left and the current heading is larger than the
      //initial one (e.g. started at 10 degrees and now we are at 350), we need to substract 360, so to eventually get a signed
      currentHeading -= 360; //displacement from the initial heading (-20)
    } else if ((targetDegrees > 0) && (currentHeading < initialHeading)) { //if we are turning right and the heading is smaller than the
      //initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed displacement (+30)
      currentHeading += 360;
    }
    degreesTurnedSoFar = initialHeading - currentHeading; //degrees turned so far is initial heading minus current (initial heading
    //is at least 0 and at most 360. To handle the "edge" cases we substracted or added 360 to currentHeading)
  }
  car.setSpeed(0); //we have reached the target, so stop the car
}

