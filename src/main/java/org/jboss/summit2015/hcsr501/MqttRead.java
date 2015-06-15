package org.jboss.summit2015.hcsr501;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.util.List;

/**
 * Simple MQTT client that subscribes to the motion sensor topic
 */
public class MqttRead implements MqttCallback {
    private volatile int receiveCount = 0;

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.printf("connectionLost\n");
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.printf("messageArrived, topic=%s, msg=%s\n", topic, mqttMessage.toString());
        receiveCount ++;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void run(int count, String id) throws Exception {
        String topic = "RHSummit2015_temp_rpi_hcsr501/"+id;

        // TODO: if your running multiple clients you need to make this unique, "RecvMotionMQTT#N-" + id;
        // where #N = #1, #2, #3, ...
        String clientID = "RecvMotionMQTT-" + id;
        MqttClient client = new MqttClient("tcp://iot.eclipse.org:1883", clientID, new MemoryPersistence());
        client.connect();
        client.subscribe(topic);
        client.setCallback(this);
        System.out.printf("Connected to: tcp://iot.eclipse.org:1883 as %s, reading %d messages\n", clientID, count);
        while(receiveCount < count) {
            Thread.sleep(1000);
        }
        client.disconnect();
        System.exit(0);
    }
    public static void main(String[] args) throws Exception {
        int count = 1;
        String clientID;
        if(args.length > 0)
            count = Integer.parseInt(args[0]);
        if(args.length == 2) {
            clientID = args[1];
        } else {
            // Take clientID from last octet of IP address
            InetAddress localhost = InetAddress.getLocalHost();
            byte[] addr = localhost.getAddress();
            clientID = String.format("%d", addr[3]);
        }

        MqttRead reader = new MqttRead();
        reader.run(count, clientID);
    }
}
