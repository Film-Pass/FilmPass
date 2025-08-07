package com.example.filmpass.domain.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch._types.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient client;

    public String saveToElastic(String title, String createdAt) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("title", title);
        jsonMap.put("createdAt", createdAt);

        IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                .index("my-index")
                .document(jsonMap)
        );

        IndexResponse response = client.index(request);

        if (response.result() == Result.Created || response.result() == Result.Updated) {
            return response.id();
        } else {
            throw new IOException("Failed to index document");
        }
    }
}