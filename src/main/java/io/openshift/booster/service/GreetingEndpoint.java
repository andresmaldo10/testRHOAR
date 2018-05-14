/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openshift.booster.service;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;

import io.openshift.booster.JmsConfig;

@Path("/")
@Component
public class GreetingEndpoint {
	
	 @Autowired
	 private JmsConfig conf;
	
    private final GreetingProperties properties;

    public GreetingEndpoint(GreetingProperties properties) {
        this.properties = properties;
    }

    @POST
    @Path("/greeting")
    @Produces("application/json")
    public Greeting greeting(@RequestBody String msg) {
		Map map = new Gson().fromJson(msg, Map.class);
		String response  = map.get("name") + "Hello " ;
		//conf.jmsTemplate().convertAndSend("q1.out",response);
        String message = String.format(properties.getMessage(), response);
        conf.jmsTemplate().convertAndSend("BG.QUE010.WHATSAPP_SEND",message);
        return new Greeting(message);
        
    }
}
