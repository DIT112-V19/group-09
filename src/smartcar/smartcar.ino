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

int SPEED = 40;
int frontDistance;
long duration, sideDistance;
int wallDistance = 18;

void setup()
{
  Serial.begin(9600); // Starting Serial Terminal
  BTserial.begin(9600);

  pinMode(trigPinSide, OUTPUT);
  pinMode(echoPinSide, INPUT);

  car.setSpeed(SPEED);
}

void loop()
{
  //gyro.update();
  frontDistance = front.getDistance();

  digitalWrite(trigPinSide, LOW);
  delayMicroseconds(2); //delays are required for a successful sensor operation
  digitalWrite(trigPinSide, HIGH);

  delayMicroseconds(10); //this delay is required as well!
  digitalWrite(trigPinSide, LOW);
  duration = pulseIn(echoPinSide, HIGH);
  sideDistance = (duration / 2) / 29.1; //convert the distance to centimeters

  /*  switch(state)
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

    if(frontDistance <= 5 && frontDistance != 0)
      state = STOPPED;
    else*/

  if (frontDistance < 20 && frontDistance != 0) {

    turnLeft();

  } else {
    if (sideDistance > 24 && sideDistance != 0) {

      turnRight();

    } else if (sideDistance <= (wallDistance + 3) && sideDistance >= wallDistance) {

      driveF();
      //Serial.println("front");
      
    } else if (sideDistance < wallDistance && sideDistance != 0) {
      
      //Serial.println("left");
      leftTrajectoryCorrection();

    } else if (sideDistance > (wallDistance + 3) && sideDistance != 0) {
      
      //Serial.println("right");
      rightTrajectoryCorrection();

    }
  }
}


void handleBluetooth()
{
  BTserial.print("HI MOM");
  delay(50);
}

void driveF()
{
  if (car.getSpeed() != SPEED)
    car.setSpeed(SPEED);
}

void driveB()
{
  car.setSpeed(-SPEED);
}

void turnLeft() {
  car.overrideMotorSpeed(-50, 50);
  delay(150);
}

void turnRight() {
  car.overrideMotorSpeed(40,-20);
}

void leftTrajectoryCorrection() {
  car.overrideMotorSpeed(20, SPEED);
}

void rightTrajectoryCorrection() {
  car.overrideMotorSpeed(SPEED, 20);
}
