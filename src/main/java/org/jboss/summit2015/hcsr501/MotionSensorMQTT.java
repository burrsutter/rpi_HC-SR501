package org.jboss.summit2015.hcsr501;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.util.concurrent.Callable;

/**
 * Use the pi4j classes to watch a gpio trigger, and publishes the event to MQTT
 * This uses the pin number scheme as outlined in: http://pi4j.com/pins/model-2b-rev1.html
 */
public class MotionSensorMQTT {
    public static void main(String[] args) throws Exception {

        System.out.printf("PIR Module Test (CTRL+C to exit)\n");

        // Connect to MQTT broker, clientID uses last octet of IP address to be unique across all clients
        InetAddress localhost = InetAddress.getLocalHost();
        byte[] addr = localhost.getAddress();
        String id = String.format("%d", addr[3]);
        String topic = "RHSummit2015_temp_rpi_hcsr501/"+id;
        String clientID = "SendMotionMQTT-" + id;
        MqttClient client = new MqttClient("tcp://iot.eclipse.org:1883", clientID, new MemoryPersistence());
        client.connect();
        System.out.printf("Connected to: tcp://iot.eclipse.org:1883\n");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // provision gpio pin #29, (header pin 40) as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);
        System.out.printf("Ready\n");

        // create a gpio callback trigger on the gpio pin
        Callable<Void> callback = () -> {
            // Send a json message indicating the time of the motion
            String json = String.format("{'event':'motion-detection', 'time': %d}", System.currentTimeMillis());
            client.publish(topic, json.getBytes(), 1, true);
            System.out.println(json);
            return null;
        };
        // create a gpio callback trigger on the PIR device pin for when it's state goes high
        pir.addTrigger(new GpioCallbackTrigger(PinState.HIGH, callback));

        // stop all GPIO activity/threads by shutting down the GPIO controller
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Interrupted, stopping...\n");
                gpio.shutdown();
            }
        });

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(100);
        }

    }
}
