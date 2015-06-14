var Gpio = require('onoff').Gpio;
// Configure GPIO pin 21 for input and rising edge detection
var pir = new Gpio(21,'in','rising');

// Setup the mqtt client
var mqtt = require('mqtt');
var client = mqtt.connect('mqtt://iot.eclipse.org:1883")
// Come up with a unique id to use, e.g., the last portion of the IP address
var id = ...
var topic = "RHSummit2015_temp_rpi_HC-SR501/" + id

client.on('connect', function () {
    console.log("Connected");
});

// Add the edge detection callback to catch the motion detection events
pir.watch(function(err, value) {
  if (value === 1) {
    // The pin went high
    console.log("Motion Detected: %d", value);
    // Publish the motion-detected event to the broker
    var date = new Date();
    client.publish(topic, "{'event':'motion-detected', 'time': "+date.getTime()+"}", 1);
  }
});

function exit() {
  console.log("Exiting");
  pir.unexport();
  process.exit();
}

process.on('SIGINT', exit);

console.log("Monitoring...");
