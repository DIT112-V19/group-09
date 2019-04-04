const int pingPin = 7; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 6; // Echo Pin of Ultrasonic Sensor

void setup() {
   Serial.begin(9600); // Starting Serial Terminal
}

void loop() {
   long sonar_ping = fireSonar();
   printSonarInCm(sonar_ping, 300);
  
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
