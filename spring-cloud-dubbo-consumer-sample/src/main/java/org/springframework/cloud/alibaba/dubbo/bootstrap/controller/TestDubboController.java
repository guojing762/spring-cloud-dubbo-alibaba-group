package org.springframework.cloud.alibaba.dubbo.bootstrap.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.alibaba.dubbo.service.User;
import org.springframework.cloud.alibaba.dubbo.service.UserService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: guojing
 * @Date: 2019/5/8 9:57
 */
@RestController
public class TestDubboController {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Value("${provider.application.name}")
    private String providerApplicationName;

    @Reference
    private UserService userService;

    @GetMapping("/echo")
    public ResponseEntity<String> testGet(){
        // RestTemplate call
        User user = new User();
        user.setId(1L);
        user.setName("小马哥");
        user.setAge(33);


        System.out.printf("UserService.save(%s) : %s\n", user, userService.save(user));

        return restTemplate.getForEntity("http://" + providerApplicationName + "/param?param=小马哥", String.class);
    }
}
