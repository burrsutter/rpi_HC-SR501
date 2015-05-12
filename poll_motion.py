import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

PIR_PIN = 21
CNT = 0;

GPIO.setup(PIR_PIN, GPIO.IN)

print "PIR Module Test (CTRL+C to exit)"
time.sleep(2)
print "Ready"


while True:
	if GPIO.input(PIR_PIN):
		CNT = CNT + 1
		print "Motion Detected: " + str(CNT)
	time.sleep(1)
