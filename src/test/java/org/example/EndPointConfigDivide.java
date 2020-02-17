package org.example;

import com.consol.citrus.db.driver.JdbcDriver;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.xml.XsdSchemaRepository;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.xsd.SimpleXsdSchema;

import java.util.Collections;

@Configuration
public class EndPointConfigDivide {
    @Bean
    public SimpleXsdSchema DivideXsdSchema(){
        return new SimpleXsdSchema(new ClassPathResource("schemas/DivideInteger.xsd"));
    }
    @Bean
    public XsdSchemaRepository schemaRepository(){
        XsdSchemaRepository xsdSchemaRepository = new XsdSchemaRepository();
        xsdSchemaRepository.getSchemas().add(DivideXsdSchema());
        return xsdSchemaRepository;
    }

    @Bean
    public NamespaceContextBuilder namespaceContextBuilder() {
        NamespaceContextBuilder namespaceContextBuilder = new NamespaceContextBuilder();
        namespaceContextBuilder.setNamespaceMappings(Collections.singletonMap("web", "http://www.dataaccess.com/webservicesserver"));
        return namespaceContextBuilder;
    }
    @Bean
    public SoapMessageFactory messageFactory(){return new SaajSoapMessageFactory();}

    @Bean(name = "divideClient")
    public WebServiceClient divideClient(){
        return CitrusEndpoints
                .soap()
                .client()
                .defaultUri("https://www.crcind.com:443/csp/samples/SOAP.Demo.cls")
                .build();
    }
/*
      @Bean
      public SingleConnectionDataSource dataSource() {
          SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
          dataSource.setDriverClassName(JdbcDriver.class.getName());
          dataSource.setUrl("jdbc:citrus:http://localhost:3306/");
          dataSource.setUsername("root");
          dataSource.setPassword("Vijay@88842");
          return dataSource;
      }
*/

    @Bean(destroyMethod = "close")
    public BasicDataSource todoListDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://localhost/bookstore");
        dataSource.setUsername("root");
        dataSource.setPassword("Vijay@88842");
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(5);
        dataSource.setMaxIdle(2);
        return dataSource;
    }
}
