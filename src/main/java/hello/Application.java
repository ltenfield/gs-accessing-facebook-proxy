package hello;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@SpringBootApplication
@Configuration
@ComponentScan
//@EnableAutoConfiguration
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration.class})
@EnableWebMvc
public class Application {

    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;

    private static final Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        if (!log.isDebugEnabled()) return;
        
        log.debug("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.debug(beanName);
        }
       
    }

    @Bean(name="httpClientCentral")
    public HttpClient httpClient() {
    	
    	CredentialsProvider cp = new BasicCredentialsProvider();
    	cp.setCredentials(new AuthScope("localhost",3128), new UsernamePasswordCredentials("user2","pass2"));
    	
    	RequestConfig rc = RequestConfig.custom()
    			.setProxy(new HttpHost("localhost",3128))
    			.build();
    	
    	HttpClientBuilder hcb = HttpClients.custom();
        hcb.setMaxConnTotal(DEFAULT_MAX_TOTAL_CONNECTIONS)
        .setMaxConnPerRoute(10)
        .setDefaultCredentialsProvider(cp)
        .setDefaultRequestConfig(rc);
        
        return hcb.build();
    }
    	
/*
      PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
      
      
     HttpClient defaultHttpClient = new DefaultHttpClient(connectionManager);

      connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
     connectionManager
       .setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
     connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
       "facebook.com")), 20);
     connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
       "twitter.com")), 20);
     connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
       "linkedin.com")), 20);
     connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
       "viadeo.com")), 20);

      defaultHttpClient.getParams().setIntParameter(
       CoreConnectionPNames.CONNECTION_TIMEOUT,
       DEFAULT_READ_TIMEOUT_MILLISECONDS);
     return defaultHttpClient;
    }
    */
} 