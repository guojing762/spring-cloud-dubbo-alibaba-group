package org.springframework.cloud.alibaba.dubbo.bootstrap.customer;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @Author: guojing
 * @Date: 2019/5/9 10:29
 */
@RestController
public class CustomerController {

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

    @RequestMapping("/testSentinel")
    public void testSentinel(){

        FlowRule flowRule = new FlowRule();
        flowRule.setResource(
                "org.springframework.cloud.alibaba.dubbo.service.EchoService:echo(java.lang.String)");
        flowRule.setCount(10);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setLimitApp("spring-cloud-alibaba-dubbo-client");
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));

        for (int i = 0; i < 15; i++) {
            try {
                String message = sayService.echo("Jim");
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
