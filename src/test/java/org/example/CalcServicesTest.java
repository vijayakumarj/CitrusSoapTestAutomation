package org.example;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.ws.client.WebServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class CalcServicesTest extends TestNGCitrusTestRunner {

    /**@BeforeSuite
    public void beforeSuite(ITestContext testContext) throws Exception {
        System.out.println("inside before suite");
        super.beforeSuite(testContext);
    }**/

    @Autowired
    public WebServiceClient calcClient;

    @CitrusTest
    @Test
    public void TestAdd(){
        echo("TestAdd");
        soap(soapActionBuilder -> soapActionBuilder
        .client(calcClient)
        .send()
        .soapAction("http://tempuri.org/Add")
        .payload(new ClassPathResource("samples/addRequest.xml"))
        .build()
        );

        soap(soapActionBuilder -> soapActionBuilder
        .client(calcClient)
        .receive()
       // .validate("AddResponse.AddResult","15")

        .payload(new ClassPathResource("samples/addResponse.xml"))
        );
    }
    @CitrusTest
    @Test(enabled = false)
    public void TestSubtract(){
        echo("TestSubtract");
        soap(soapActionBuilder -> soapActionBuilder
        .client(calcClient)
        .send()
        .soapAction("http://tempuri.org/Subtract")
        //.build()
        .payload(new ClassPathResource("samples/subtractRequest.xml")).build()

        );
        soap(soapActionBuilder -> soapActionBuilder
                .client(calcClient)
                .receive()
                .payload(new ClassPathResource("samples/subtractResponse.xml")));
    }
}
