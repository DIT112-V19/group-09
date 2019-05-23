#include <Smartcar.h>
#include <SoftwareSerial.h>

SoftwareSerial BTserial(15, 14); // RX | TX
const int GYROSCOPE_OFFSET = 18;

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);
DirectionlessOdometer leftOdometer(110);
DirectionlessOdometer rightOdometer(120);

DirectionlessOdometer mleftOdometer(110);
DirectionlessOdometer mrightOdometer(120);

GY50 gyro(GYROSCOPE_OFFSET);

SmartCar car(control, gyro, leftOdometer, rightOdometer);

// PINS
const int leftOdometerPin = 2;
const int rightOdometerPin = 3;

const int trigPinFront = 4; // Front Trigger Pin of Ultrasonic Sensor
const int echoPinFront = 5; // Front Echo Pin of Ultrasonic Sensor

const int trigPinSide = 6; // Side Trigger Pin of Ultrasonic Sensor
const int echoPinSide = 7; // Side Echo Pin of Ultrasonic Sensor

// VARIABLES

int TARGET_SPEED = 40; //Default car speed

long duration;
int gyroHeading;
int lastGyroHeading;

int targetSideDistance = 20; //Distance from side wall in CM

int targetFrontDistance = 35; //Distnace from front wall in CM

int acceptableFalloff = 5;

long gyroDirection = 0;

bool automaticDriving = false;

const unsigned int MAX_DISTANCE = 100;
SR04 front(trigPinFront, echoPinFront, MAX_DISTANCE);
SR04 side(trigPinSide, echoPinSide, MAX_DISTANCE);


void setup()
{
  // SERIAL
  Serial.begin(9600); // Starting Serial Terminal

  // SENSOR
  pinMode(trigPinSide, OUTPUT);
  pinMode(echoPinSide, INPUT);

  pinMode(trigPinFront, OUTPUT);
  pinMode(echoPinFront, INPUT);

  //OTHER
  leftOdometer.attach(leftOdometerPin, [](){
    leftOdometer.update();
  });
  rightOdometer.attach(rightOdometerPin, [](){
    rightOdometer.update();
  });

  mleftOdometer.attach(leftOdometerPin, [](){
    mleftOdometer.update();
  });
  mrightOdometer.attach(rightOdometerPin, [](){
    mrightOdometer.update();
  });

}
  
void loop()
{
 
    gyro.update();

    readBluetooth();
   
    
    if(automaticDriving){
      long sideDistance = getSideDistance();
      long frontDistance = getFrontDistance();
      
  
      if(frontDistance > targetFrontDistance){
        followSideWall(sideDistance);
      }else{
        frontWallReached();
      }
    }

    handleCoordinates();
    handleDistance();
}

void handleDistance(){
  if(modometerAverageDistance() != 0){
    Serial.print("#");
    Serial.println(modometerAverageDistance());
  }
}

void handleCoordinates(){
  float dir = gyro.getHeading();
 
  if(dir != gyroDirection){

     float distance = (rightOdometer.getDistance() + leftOdometer.getDistance())/2;
     
      float x = distance * sin(radians(gyroDirection));
      float y = distance * cos(radians(gyroDirection));
      Serial.print(-x);
      Serial.print(",");
      Serial.println(-y);
     
     resetOdometer();
     gyroDirection = dir;
  }
}

void frontWallReached(){
    do {
      car.overrideMotorSpeed(-TARGET_SPEED, TARGET_SPEED);
      delay(5);
    }while(getFrontDistance() < targetFrontDistance + acceptableFalloff  /*(getFrontDistance() < targetFrontDistance && getSideDistance() < targetSideDistance)*/);
}

void followSideWall(long sideDistance) {
    if(sideDistance > targetSideDistance * 2){
        car.overrideMotorSpeed(TARGET_SPEED, TARGET_SPEED/4);
        delay(10);
    }else if(sideDistance < 5){
      car.overrideMotorSpeed(-TARGET_SPEED, -TARGET_SPEED);
      delay(200);
    }else{
        sideDistance = constrain(sideDistance, 0, targetSideDistance * 2);
        float offset = sideDistance / float(targetSideDistance);
        car.overrideMotorSpeed(TARGET_SPEED * offset, TARGET_SPEED / offset);
    }
}

int strap(int val, int min, int max)
{
    int p = max-min+1;
    int mod = (val-min)%p;
    if(mod<0)
        mod += p;
    return min+mod;
}

long getSideDistance(){
    digitalWrite(trigPinSide, LOW);
    delayMicroseconds(2);
    // Sets the trigPin on HIGH state for 10 micro seconds
    digitalWrite(trigPinSide, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPinSide, LOW);
    // Reads the echoPin, returns the sound wave travel time in microseconds to CM
    float dist = distanceToCM(pulseIn(echoPinSide, HIGH));
    if(dist != 0){
      return dist;
    }else{
      return getSideDistance(); 
    }
}

long getFrontDistance(){
    digitalWrite(trigPinFront, LOW);
    delayMicroseconds(2);
    // Sets the trigPin on HIGH state for 10 micro seconds
    digitalWrite(trigPinFront, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPinFront, LOW);
    // Reads the echoPin, returns the sound wave travel time in microseconds to CM
    float dist = distanceToCM(pulseIn(echoPinFront, HIGH));
    if(dist != 0){
      return dist;
    }else{
      return getFrontDistance(); 
    }
}

void readBluetooth(){
  while(Serial.available()){
        char msg = Serial.read();
        handleAutomaticControl(msg);
        handleManualControl(msg);
    }
}

void handleAutomaticControl(char character){
  switch(character){
    case 'G':
      automaticDriving = true;
    break;
    case 'S':
      automaticDriving = false;
      car.setSpeed(0);
    break;
    default:
      automaticDriving = false;
      break;
  }
}

void handleManualControl(char character){
  switch(character){
    case '2': //FORWARD
    car.overrideMotorSpeed(TARGET_SPEED,TARGET_SPEED);
    break;
    case '3': //FORWARD-RIGHT
    car.overrideMotorSpeed(TARGET_SPEED, TARGET_SPEED);
    break;
    case '1': //FORWARD-LEFT
    car.overrideMotorSpeed(TARGET_SPEED, TARGET_SPEED);
    break;
    case '8': //REAR
    car.overrideMotorSpeed(-TARGET_SPEED, -TARGET_SPEED);
    break;
    case '9': //REAR-RIGHT
    car.overrideMotorSpeed(-TARGET_SPEED, -TARGET_SPEED);
    break;
    case '7': //READ-LEFT
    car.overrideMotorSpeed(-TARGET_SPEED, -TARGET_SPEED);
    break;
    case '6': //RIGHT
    car.overrideMotorSpeed(TARGET_SPEED, -TARGET_SPEED);
    mresetOdometer();
    break;
    case '4': //LEFT
    car.overrideMotorSpeed(-TARGET_SPEED, TARGET_SPEED);
    mresetOdometer();
    break;
    case '5': //STOP
    car.setSpeed(0);
    break;
  }
}

float odometerAverageDistance(){
  float leftO = leftOdometer.getDistance();
  float rightO = rightOdometer.getDistance();
  return ((leftO + rightO)/2);
}

float modometerAverageDistance(){
  float leftO = mleftOdometer.getDistance();
  float rightO = mrightOdometer.getDistance();
  return ((leftO + rightO)/2);
}

void resetOdometer(){
  leftOdometer.reset();
  rightOdometer.reset();
}

void mresetOdometer(){
  mleftOdometer.reset();
  mrightOdometer.reset();
}

long distanceToCM(long distance){
  return distance * 0.034/2;
}
