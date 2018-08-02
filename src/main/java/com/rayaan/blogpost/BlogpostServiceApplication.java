package com.rayaan.blogpost;

import com.rayaan.blogpost.kafka.event.KafkaBlogpostEventProducer;
import com.rayaan.blogpost.repository.BlogpostRepository;
import com.rayaan.blogpost.repository.postgres.PostgresBlogpostRepository;
import com.rayaan.blogpost.repository.postgres.PostgresInitialSetup;
import com.rayaan.blogpost.resource.BlogResources;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.typesafe.config.*;

public class BlogpostServiceApplication extends Application<BlogpostServiceConfiguration> {
    public static void main(String[] args) throws Exception{
            new BlogpostServiceApplication().run("server","config.yml");
    }

    public void run(BlogpostServiceConfiguration blogpostServiceConfiguration, Environment environment) throws Exception {
        AdditionalConfig additionalConfig = new AdditionalConfig();
        // Service service;
        // if (additionalConfig.getEnv == "test") service = new Service(new InMemoryBlogpostRepository);
        // else service = new Service(PostgresBlogPostRepository());
        BlogpostRepository postgresBlogpostRepository = new PostgresBlogpostRepository(new PostgresInitialSetup(additionalConfig),additionalConfig);
        BlogResources blogResources = new BlogResources(postgresBlogpostRepository);
        environment.jersey().register(blogResources);
        environment.jersey().setUrlPattern(ConfigFactory.load().getString("blog-post.url-prefix"));
    }
}
