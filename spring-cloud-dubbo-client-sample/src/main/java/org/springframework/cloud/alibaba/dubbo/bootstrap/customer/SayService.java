package org.springframework.cloud.alibaba.dubbo.bootstrap.customer;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cloud.alibaba.dubbo.service.EchoService;
import org.springframework.stereotype.Service;

@Service
public class SayService {

    @Reference(version = "${foo.service.version}",check = false)
    EchoService echoService;

 /*   @Autowired
    SelfConfigs selfConfigs;*/

    public String echo(String name) {
        String ss = echoService.echo(name);
        return ss;
    }

}