package org.example;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.ws.client.WebServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

public class DivideIntegerTest extends TestNGCitrusTestRunner {
    @Autowired
    public WebServiceClient divideClient;

    @CitrusTest
    @Test
    public void TestNumberToWords(){
        soap(soapActionBuilder -> soapActionBuilder
                .client(divideClient)
                .send()
                .soapAction("http://tempuri.org/SOAP.Demo.DivideInteger")
                .payload(new ClassPathResource("samples/DivideIntegerRequest.xml"))
                .build()
        );
    }
}
