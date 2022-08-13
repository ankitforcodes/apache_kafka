package org.training.demo.kafka;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());
    public static void main(String[] args) {
        System.out.println("hello");

        //Step1: Create Producer Properties
        Properties properties = new Properties();
        //M1: properties.setProperty("bootstrap.servers", "159.89.162.32:9092");
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "159.89.162.32:9092");

        //Use the serializer according to your key/value datatype
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //STEP 2: CREATE PRODUCER
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


        //STEP 3: CREATE PRODUCER RECORD
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("click-data", "customer5:login");


        //STEP 4: SEND DATA
        producer.send(producerRecord);

        //STEP 5: FLUSH DATA
        producer.flush();

        //STEP 6: CLOSR PRODUCER
        producer.close();



    }
}
