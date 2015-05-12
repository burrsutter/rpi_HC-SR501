var Gpio = require('onoff').Gpio;
var pir = new Gpio(21,'in','both');

pir.watch(function(err, value) {
  if (value === 1) {
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
