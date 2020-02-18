package org.example;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.xml.XsdSchemaRepository;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.xsd.SimpleXsdSchema;

import java.util.Collections;

@Configuration
public class EndPointConfigCalc {
    @Bean
    public SimpleXsdSchema amazonXsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schemas/CalcServices.xsd"));
    }

    @Bean
    public XsdSchemaRepository schemaRepository() {
        XsdSchemaRepository xsdSchemaRepository = new XsdSchemaRepository();
        xsdSchemaRepository.getSchemas().add(amazonXsdSchema());
        return xsdSchemaRepository;
    }

    @Bean
    public SoapMessageFactory messageFactory() {
        return new SaajSoapMessageFactory();
    }

    @Bean(name = "calcClient")
    public WebServiceClient calcClient() {
        return CitrusEndpoints
                .soap()
                .client()
                .defaultUri("http://www.dneonline.com/calculator.asmx")
                .build();
    }
}
