package com.rayaan.blogpost;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AdditionalConfig {
    Config postgresConfig;
    Config kafkaConfig;
    public AdditionalConfig(){
        postgresConfig = setPostgresConfig();
        kafkaConfig = setKafkaConfig();
    }
    private Config setPostgresConfig(){
        final Config config = ConfigFactory.load();
        return config.getConfig("blog-post.postgres");
    }
    private Config setKafkaConfig(){
        final Config config = ConfigFactory.load();
        return config.getConfig("blog-post.kafka");
    }

    public Config getKafkaConfig() {
        return kafkaConfig;
    }

    public Config getPostgresConfig() {
        return postgresConfig;
    }
}
