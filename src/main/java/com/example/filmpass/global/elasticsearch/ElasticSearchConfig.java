package com.example.filmpass.global.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 1. 기존 RestClient 생성 (Apache HttpComponents 기반)
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        // 2. RestClientTransport 생성, JSON 매퍼로 Jackson 사용
        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        // 3. ElasticsearchClient 생성 및 반환
        return new ElasticsearchClient(transport);
    }
}

