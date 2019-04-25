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

int stuckCounter = 0;

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
  Serial.println("Hello");
  //gyro.update();
  frontDistance = front.getDistance();

  digitalWrite(trigPinSide, LOW);
  delayMicroseconds(2); //delays are required for a successful sensor operation
  digitalWrite(trigPinSide, HIGH);

  delayMicroseconds(10); //this delay is required as well!
  digitalWrite(trigPinSide, LOW);
  duration = pulseIn(echoPinSide, HIGH);
  sideDistance = (duration / 2) / 29.1; //convert the distance to centimeters
  
  if (frontDistance < 20 && frontDistance != 0) {

    turnLeft();

  } else {
    if (sideDistance > 24 && sideDistance != 0) {

      turnRight();
    } 
  }
  
  drive();
  handleStuck();
  
}

void handleStuck(){
  if(car.getSpeed() <= 10){
    stuckCounter++;
  }else{
    stuckCounter = 0;
  }

  if(stuckCounter >= 5000){
    stuckCounter = 0;
    wallReverse();
  }
}

void wallReverse() {
  car.overrideMotorSpeed(-SPEED, 0);
  delay(450);
}

void drive() {
    int power = (((SPEED / wallDistance) * sideDistance) - wallDistance) * 2;
    if(constrain(power, -30, 30) < -26){
      wallReverse();
    }else{
      car.overrideMotorSpeed(SPEED + constrain(power, -30, 30), SPEED + -constrain(power, -30, 30));
    }
}

void handleBluetooth()
{
  BTserial.print("HI MOM");
  delay(50);
}

void driveB()
{
  car.setSpeed(-SPEED);
}

void turnLeft() {
  car.overrideMotorSpeed(-50, 50);
  delay(200);
}

void turnRight() {
  car.overrideMotorSpeed(40,-20);
}
