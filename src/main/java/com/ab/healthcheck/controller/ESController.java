package com.ab.healthcheck.controller;

import com.ab.healthcheck.model.Article;
import com.ab.healthcheck.model.Author;
import com.ab.healthcheck.repository.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/es/v1")
public class ESController {

    @Autowired
    ElasticsearchOperations elasticsearchTemplate;

    @Autowired
    ArticleRepository articleRepository;

    @PostMapping("/addarticle")
    public void addData() {

        Article article = new Article("Horses in the barn");
        article.setAuthors(Arrays.asList(new Author("Tom Hanks"), new Author("Tom Cruise")));
        articleRepository.save(article);
        System.out.println("addarticle");

    }

    @GetMapping("/getarticle")
    public void getData() {

        String nameToFind = "Tom Hanks";
        Page<Article> articleByAuthorName = articleRepository.findByAuthorsName(nameToFind, PageRequest.of(0, 10));
        System.out.println("getarticle");
    }

    @GetMapping("/getarticlecustomquery")
    public void getDataCustomQuery() {

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.regexpQuery("title", ".*data.*"))
                .build();
        SearchHits<Article> articles = elasticsearchTemplate.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        System.out.println("getarticlecustomquery");
    }

    @PutMapping("/updatearticle")
    public void updateData() {

        String articleTitle = "Spring Data Elasticsearch";
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", articleTitle).minimumShouldMatch("75%"))
                .build();

        SearchHits<Article> articles =
                elasticsearchTemplate.search(searchQuery, Article.class, IndexCoordinates.of("blog"));

        Article article = articles.getSearchHit(0).getContent();
        article.setTitle("Getting started with Search Engines");
        articleRepository.save(article);

        System.out.println("updatearticle");
    }

    @DeleteMapping("/deletearticle")
    public void deleteData() {

        String articleTitle = "Getting started with Search Engines";
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", articleTitle).minimumShouldMatch("75%"))
                .build();

        SearchHits<Article> articles =
                elasticsearchTemplate.search(searchQuery, Article.class, IndexCoordinates.of("blog"));

        Article article = articles.getSearchHit(0).getContent();
        articleRepository.delete(article);

        System.out.println("deletearticle");
    }
}
