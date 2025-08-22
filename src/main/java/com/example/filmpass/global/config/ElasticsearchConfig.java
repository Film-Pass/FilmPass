package com.example.filmpass.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 1) 클라우드 엔드포인트로 교체 (호스트만 넣고, scheme/port는 분리)
        HttpHost host = new HttpHost("filmpasses.es.ap-northeast-2.aws.elastic-cloud.com", 9243, "https");

        // 2) Basic 인증 자격증명
        CredentialsProvider cp = new BasicCredentialsProvider();
        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "WBBvVFnb7jBailgswUMFv26r"));

        // 3) 타임아웃 등 옵션
        RestClientBuilder builder = RestClient.builder(host)
                .setHttpClientConfigCallback(hcb -> hcb
                        .setDefaultCredentialsProvider(cp))
                .setRequestConfigCallback(rcb -> rcb
                        .setConnectTimeout(5_000)
                        .setSocketTimeout(30_000));

        RestClient lowLevel = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(lowLevel, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
