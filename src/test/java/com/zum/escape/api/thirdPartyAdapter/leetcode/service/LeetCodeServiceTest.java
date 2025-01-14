package com.zum.escape.api.thirdPartyAdapter.leetcode.service;


import com.zum.escape.api.thirdPartyAdapter.leetcode.response.ProblemResponse;
import com.zum.escape.api.users.domain.User;
import com.zum.escape.api.users.dto.URL;
import com.zum.escape.api.users.service.UserLogin;
import com.zum.escape.api.users.service.UserProblemCrawlService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

public class LeetCodeServiceTest {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";
    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void init() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        restTemplate.getMessageConverters().add(converter);
    }

    @Test
    public void getProblemsTest() {
        try {
            HttpEntity<String> headers = new HttpEntity<>(makeHttpHeaders());
            ResponseEntity<ProblemResponse> problemResponse = restTemplate.exchange(URL.PROBLEMS, HttpMethod.GET, headers, ProblemResponse.class);

            System.out.println("StatusCode : " + problemResponse.getStatusCode());
            System.out.println("Headers : " + problemResponse.getHeaders());
            System.out.println("Body : " + problemResponse.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e.getMessage());
        }
    }

    private HttpHeaders makeHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
        return headers;
    }


    @Test
    public void jsoupTest() throws IOException {
        Connection connection = Jsoup.connect(URL.USER + "nokchax");
        connection.header(HttpHeaders.USER_AGENT, USER_AGENT);


        Document document = connection.get();
        Elements elements = document.getElementsByClass("fa-question").parents().get(0).getElementsByClass("progress-bar-success");
        System.out.println("=============================================================");
        System.out.println(elements.get(0).text());
        System.out.println("=============================================================");
        Elements href = document.getElementsByAttributeValueStarting("href", "/problems");
        System.out.println("=============================================================");
        System.out.println(href);
        System.out.println("=============================================================");
        for(Element e : href) {
            String problem = e.getElementsByTag("b").text();
            String accepted= e.getElementsByTag("span").get(0).text();
            String time = e.getElementsByClass("text-muted").get(0).text();
            System.out.println(problem + " : " + accepted + " : " + time);
        }

    }
    @Test
    public void LoginTest() throws IOException {
        User user = new User("test@naver.com","test","test","test");
        UserLogin userLogin = new UserLogin(user);
        userLogin.doLogin();
        System.out.println(new UserProblemCrawlService().getUserProblems(user).toUserProblemList(user));
    }
}