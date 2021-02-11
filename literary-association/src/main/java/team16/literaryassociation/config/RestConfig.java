package team16.literaryassociation.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Arrays;

@Configuration
public class RestConfig {

    private static final String URL_FORMAT = "%s://%s:%s";


    @Value("${server.ssl.algorithm}")
    private String algorithm;

    @Value("${server.ssl.key-store}")
    private String keystore;

    @Value("${server.ssl.key-store-type}")
    private String keystoreType;

    @Value("${server.ssl.key-store-password}")
    private String keystorePassword;

    @Value("${server.ssl.key-alias}")
    private String applicationKeyAlias;

    @Value("${server.ssl.trust-store}")
    private String truststore;

    @Value("${server.ssl.trust-store-type}")
    private String truststoreType;

    @Value("${server.ssl.trust-store-password}")
    private String truststorePassword;

    @Value("${PROTOCOL:https}")
    private String protocol;

    @Value("${DOMAIN:localhost}")
    private String domain;

    @Value("${PORT:8083}")
    private String port;

    public String url() {
        return String.format(URL_FORMAT, protocol, domain, port);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                httpClient(keystoreType, keystore, keystorePassword, applicationKeyAlias,
                        truststoreType, truststore, truststorePassword)));
    }


    @Bean
    public HttpClient httpClient(String keystoreType, String keystore, String keystorePassword, String alias,
                                 String truststoreType, String truststore, String truststorePassword) {
        try {

            //ovo dodala, nije htelo da nadje u resources
            ClassPathResource resource1 = new ClassPathResource("la_backend.p12");
            ClassPathResource resource2 = new ClassPathResource("truststore.p12");

            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            keyStore.load(new FileInputStream(resource1.getFile()), keystorePassword.toCharArray());
            KeyStore trustStore = KeyStore.getInstance(truststoreType);
            trustStore.load(new FileInputStream(resource2.getFile()), truststorePassword.toCharArray());

            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)
                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> alias)
                    .build();

            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{algorithm},
                    null, (hostname, sslSession) -> true);

            return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
        } catch (Exception e) {
            throw new IllegalStateException("Error while configuring rest template", e);
        }
    }
}
