/*
 * #%L
 * Wildfly Camel :: Testsuite
 * %%
 * Copyright (C) 2013 - 2014 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wildfly.camel.test.activemq;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.wildfly.camel.test.common.http.HttpRequest;
import org.wildfly.camel.test.common.http.HttpRequest.HttpResponse;

public class ActiveMQExampleIT {

    @Test
    public void testFileToActiveMQRoute() throws Exception {
        
        File destination = new File(System.getProperty("jboss.home") + "/standalone/data/orders");
        InputStream input = getClass().getResourceAsStream("/activemq/order.xml");
        Path targetPath = destination.toPath().resolve("order.xml");
        Files.copy(input, targetPath);
        input.close();

        // Give camel a chance to consume the test order file
        Thread.sleep(2000);

        HttpResponse result = HttpRequest.get(getEndpointAddress("/example-camel-activemq/orders")).getResponse();
        System.out.println(">>>>>>>>>>>>>>>");
        System.out.println(result.getBody());
        System.out.println(">>>>>>>>>>>>>>>");
        Assert.assertTrue(result.getBody().contains("UK: 1"));
    }

    private String getEndpointAddress(String contextPath) throws MalformedURLException {
        return "http://localhost:8080" + contextPath;
    }
}
