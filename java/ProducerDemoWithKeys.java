package org.training.demo.kafka;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getSimpleName());
    public static void main(String[] args) {
        System.out.println("Producer With Callback Multiple Messages");

        //Step1: Create Producer Properties
        Properties properties = new Properties();
        //M1: properties.setProperty("bootstrap.servers", "159.89.162.32:9092");
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "159.89.162.32:9092");

        //Use the serializer according to your key/value datatype
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //STEP 2: CREATE PRODUCER
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Note that we are providing "key" to the producer record
        // we will run this code twice and see if same key went to same partition
        for (int i = 0; i < 10; i++) {
            //STEP 3: CREATE PRODUCER RECORD
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("click-data", "id_" + i ,"customer5:login" + i);


            //STEP 4: SEND DATA
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // gets executed everytime a record is successfully sent or exception is raised
                    if (exception == null) {
                        // record was successfully sent
                        // Kafka responds with the metadata of the record it received
                        log.info("\n#######**********###########\n" +
                                " Partition: " + metadata.partition() +
                                " KEY: " + producerRecord.key() +
                                 "\n #######**********###########\n"
                        );
                    } else {
                        log.error("Error Occured: ", exception);
                    }
                }
            });

            /*
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            */
        }
        //STEP 5: FLUSH DATA
        producer.flush();

        //STEP 6: CLOSE PRODUCER
        producer.close();



    }
}

/*
batch.size: When multiple records are sent to the same partition, the producer will batch them together.
This parameter controls the amount of memory in bytes that will be used for each batch.
When the batch is full, all the messages in the batch will be sent
This does not mean that the producer will wait for the batch to become full. The producer will send half-full batches and even batches with just a single message in them


linger.ms: amount of time to wait for additional messages before sending the current batch
* KafkaProducer sends a batch of messages either when the current batch is full or when the linger.ms limit is reached
* By default, the producer will send messages as soon as there is a sender thread available to send them, even if there’s just one message in the batch
* By setting linger.ms higher than 0, we instruct the producer to wait a few milliseconds to add additional messages to the batch before sending it to the brokers.
* This increases latency but also increases throughput (because we send more messages at once, there is less overhead per message)




 */