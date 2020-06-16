package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderGuard {

    public Guard<OrderStates, OrderEvents> selectPickType() {
        log.info("---选择提货方式---");
        return context -> false;
    }
}
