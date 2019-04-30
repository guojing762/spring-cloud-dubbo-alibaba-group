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

import org.apache.dubbo.config.annotation.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.alibaba.dubbo.bootstrap.customer.SayService;
import org.springframework.cloud.alibaba.dubbo.service.EchoService;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        SpringApplication.run(DubboSpringCloudClientBootstrap.class);
    }
}
