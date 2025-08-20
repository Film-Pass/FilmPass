package com.example.filmpass.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

@Configuration
public class ElasticsearchConfig {
    @Bean
    public ElasticsearchOperations elasticTemplate(ElasticsearchClient elasticsearchClient, ElasticsearchConverter converter) {
        return new ElasticsearchTemplate(elasticsearchClient, converter);
    }
}
