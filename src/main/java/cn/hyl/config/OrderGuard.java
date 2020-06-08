package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class OrderGuard {
    public Guard<OrderStates, OrderEvents> orderGuard() {
        return context -> {
            System.out.println("guard方法判断");
            return true;
        };
    }
}
