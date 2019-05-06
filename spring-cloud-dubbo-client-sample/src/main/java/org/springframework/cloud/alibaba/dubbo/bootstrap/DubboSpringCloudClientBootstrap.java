/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.alibaba.dubbo.bootstrap;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.alibaba.dubbo.bootstrap.customer.SayService;
import org.springframework.cloud.alibaba.dubbo.service.EchoService;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Dubbo Spring Cloud Client Bootstrap
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@RestController
@ComponentScan(basePackages={"org.springframework.cloud.alibaba.dubbo.bootstrap"})
public class DubboSpringCloudClientBootstrap {

    /**
     * @Reference(version = "${foo.service.version}", application = "${dubbo.application.id}",
     *         path = "dubbo://localhost:12345", timeout = 30000)
     */
    @Autowired
    private SayService sayService;

    @GetMapping("/echo")
    public String echo(String message) {
        return sayService.echo(message);
    }

    public static void main(String[] args) {
        FlowRule flowRule = new FlowRule();
        flowRule.setResource(
                "org.springframework.cloud.alibaba.dubbo.service:echo(java.lang.String)");
        flowRule.setCount(10);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setLimitApp("default");
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));

        SpringApplicationBuilder consumerBuilder = new SpringApplicationBuilder();
        ApplicationContext applicationContext = consumerBuilder
                .web(WebApplicationType.NONE).sources(DubboSpringCloudClientBootstrap.class)
                .run(args);

        SayService service = applicationContext.getBean(SayService.class);

        for (int i = 0; i < 15; i++) {
            try {
                String message = service.echo("Jim");
                System.out.println((i + 1) + " -> Success: " + message);
            }
            catch (SentinelRpcException ex) {
                System.out.println("Blocked");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
