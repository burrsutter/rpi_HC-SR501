# Passive Infrared Motion Sensor, HC-SR501
The HC-SR501 is a passive infrared motion sensor. The PIR sensors we’ll be using in this lab have three pins: ground, digital out and 3-5VDC in. At idle, when no motion has been detected, the digital out will remain low. When motion is detected, the digital out will pulse high (3.3V) and we’ll the Raspberry Pi GPIO trigger feature to sense this. The HC-SR501 sensor has a range of approximately 7 meters, and a 110° x 70° detection range.

# Wiring the Sensor
To wire the PIR sensor to the RaspberryPi2, connect the pins as shown in the following diagram:

![](./images/PIR_wiring.jpg =350x600)

This has the connections:

* Sensor VCC pin connected to the Pi 5V Pin2
* Sensor OUT pin connected to the Pi GPIO21 Pin40
* Sensor GND pin connected to the Pi Ground Pin6

The following pin header diagram shows the header and Broadcom naming convention:
![](./images/Pi2Header.png =700x)

# Python usage
The first version (poll_motion.py) sits in a loop waiting for the GPIO pin to go to a high state:

	root@raspberrypi:/media/github/rpi_HC-SR501# python poll_motion.py 
	PIR Module Test (CTRL+C to exit)
	Ready
	Motion Detected: 1
	Motion Detected: 2
	Motion Detected: 3
	Motion Detected: 4
	Motion Detected: 5
	Motion Detected: 6
	^CTraceback (most recent call last):
	  File "poll_motion.py", line 20, in <module>
		time.sleep(1)
	KeyboardInterrupt

The second version () uses the GPIO trigger callback to be told when the GPIO pin state goes to a high state:

	root@raspberrypi:/media/github/rpi_HC-SR501# python sense_motion.py 
	PIR Module Test (CTRL+C to exit)
	Ready
	Motion Detected
	Motion Detected
	^CQuit
	root@raspberrypi:/media/github/rpi_HC-SR501# 

# node usage
To run the node client, use:

	root@raspberrypi:/media/github/rpi_HC-SR501# node sense_motion.js 
	Monitoring...
	Motion Detected: 1
	Motion Detected: 1
	Motion Detected: 1
	^CExiting

# Java usage
To run the Java client, use the Gradle script and run:

	root@raspberrypi:/media/github/rpi_HC-SR501# gradle motionLoop
	Starting a new Gradle Daemon for this build (subsequent builds will be faster).
	:compileJava
	:processResources UP-TO-DATE
	:classes
	:motionLoop
	PIR Module Test (CTRL+C to exit)
	Ready
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	 --> GPIO TRIGGER CALLBACK RECEIVED 
	^CInterrupted, stopping...
	root@raspberrypi:/media/github/rpi_HC-SR501#
