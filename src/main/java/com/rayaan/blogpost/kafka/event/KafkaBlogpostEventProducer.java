package com.rayaan.blogpost.kafka.event;

import com.rayaan.blogpost.AdditionalConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import com.typesafe.config.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class KafkaBlogpostEventProducer {
    //private final String TOPIC = "blog-post-event-topic-v1";
    //private final String BOOTSTRAP_SERVERS = "localhost:9092";
    private final Producer<String, String> producer;
    String Topic;
    public KafkaBlogpostEventProducer(String bootstrapServer, String Topic) {
        this.Topic = Topic;
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer) ;
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"Kafka-blog-post-event-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        producer = new KafkaProducer<>(props);
    }

    public void produce(String blogId, String Description){
        producer.send(
                new ProducerRecord<>(
                        Topic,
                        blogId,
                        Description));
    }

}
