package org.training.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoShutdown {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoShutdown.class.getSimpleName());
    public static void main(String[] args) {

    //STEP 1:  CREATE CONSUMER PROPERTIES
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "159.89.162.32:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "ankit-consumer-group1");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");    //earliest or latest

    //STEP 2: CREATE CONSUMER
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    //GET Reference to current thread
    final Thread mainThread = Thread.currentThread();

    //Add Shutdown hook in new Thread and call consumer.wakeup from this new thread
    Runtime.getRuntime().addShutdownHook(new Thread(){
        public void run(){
            log.info("Shutdown requested! Call consumer.wakeup to raise an exception that will stop .poll");
            consumer.wakeup();

            //.poll has stopped and we join the Shutdown thread to main thread
            try{
                mainThread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    });


    try{
        //STEP 3: SUBSCRIBE TO TOPIC
        consumer.subscribe(Arrays.asList("click-data"));

        //STEP 4: KEEP POLLING FOR NEW DATA
        while(true){
            log.info("Started Polling");
            // Get as many message as possible from Kafka
            // But if there is no message, wait till 100ms and then return back
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord <String, String> record: records){
                log.info("Key: " + record.key() +
                        " Value: " + record.value() +
                        " Partition: " + record.partition() +
                        " Offset: " + record.offset()
                );
            }
        }
    } catch (WakeupException e){
        log.info("Wake up exception raised. Ignoring it, since its raised intentionally");
    } catch (Exception e){
        log.error("Some error occured");
    } finally {
        consumer.close();
    }


    }
}

/*
Notice few things when you run this code for the first time
* How it tries to find any previously committed offset so that it can start reading from that offset onwards
 - Found no committed offset for partition click-data-1
 - Found no committed offset for partition click-data-2
 - Found no committed offset for partition click-data-0

* It fetches batch of data from one partition before going to next partition to reduce network costs
* If you stop this code - it will not close gracefully - we will see in next program
* If you start the program again, we will not get any messages, because group id is same, but if you change group.id you will get messages
 */