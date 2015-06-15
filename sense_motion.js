var Gpio = require('onoff').Gpio;
// Configure GPIO pin 21 for input and rising edge detection
var pir = new Gpio(21,'in','rising');

// Add the edge detection callback to catch the motion detection events
pir.watch(function(err, value) {
  if (value === 1) {
    // The pin went high
    console.log("Motion Detected: %d", value);
  }
});


function exit() {
  console.log("Exiting");
  pir.unexport();
  process.exit();
}

process.on('SIGINT', exit);

console.log("Monitoring...");
