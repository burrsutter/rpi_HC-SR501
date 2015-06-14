import time
import paho.mqtt.client as mqtt

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))


client = mqtt.Client()
client.on_connect = on_connect
client.connect("iot.eclipse.org", 1883, 60)

# Come up with a unique id to use, e.g., the last portion of the IP address
id = ...
topic = "RHSummit2015_temp_rpi_HC-SR501/" + id

# Motion callback now sends a message to the MQTT broker, customize if you wish
def MOTION(PIR_PIN):
    now = time.time()
    json = "{{'event':'motion-detection', 'time': {:f}}}".format(now)
    print json
    client.publish(topic, json)

# Copy the sense_motion.py code for the motion detection to here...
