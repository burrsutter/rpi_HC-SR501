package org.jboss.summit2015.hcsr501;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;

import java.util.concurrent.Callable;

/**
 * Use the pi4j classes to watch a gpio trigger. This uses the pin number scheme as outlined in:
 * http://pi4j.com/pins/model-2b-rev1.html
 */
public class MotionSensor {
    public static void main(String[] args) throws InterruptedException {

        System.out.printf("PIR Module Test (CTRL+C to exit)\n");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // provision gpio pin #29, (header pin 40) as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);
        System.out.printf("Ready\n");

        // create a gpio callback trigger on the gpio pin
        Callable<Void> callback = () -> {
            System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
            return null;
        };
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
