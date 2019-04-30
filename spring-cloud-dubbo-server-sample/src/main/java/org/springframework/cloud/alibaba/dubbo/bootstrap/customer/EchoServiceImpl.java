package org.springframework.cloud.alibaba.dubbo.bootstrap.customer;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.cloud.alibaba.dubbo.service.EchoService;


/**
 *  @Service(
 *         version = "${foo.service.version}",
 *         application = "${dubbo.application.id}",
 *         protocol = "${dubbo.protocol.id}",
 *         registry = "${dubbo.registry.id}"
 * )
 */
@Service(version = "${foo.service.version}")
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return "[echo] Hello, " + message;
    }
}