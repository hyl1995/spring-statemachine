package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;

/**
 * 源码在获取监听器时有锁，性能有问题
 */
//@WithStateMachine(id="orderMachine")
@Slf4j
public class OrderEventConfig {

    /**
     * 当前状态UNPAID
     */
    @OnTransition(target = "UNPAID")
    public void create() {
        log.info("---订单创建，待支付---");
    }
    
    /**
     * UNPAID->WAITING_FOR_RECEIVE 执行的动作
     */
    @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
    public void pay(Message<OrderEvents> message) {
        log.info("---用户完成支付，待收货---"+"传递的参数：" + message.getHeaders().get("order"));
    }
    
    /**
     * WAITING_FOR_RECEIVE->DONE 执行的动作
     */
    @OnTransition(source = "WAITING_FOR_RECEIVE", target = "SHIPPING")
    public void ship(Message<OrderEvents> message) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("---商家发货中---"+"传递的参数：" + message.getHeaders().get("order"));
    }

    /**
     * WAITING_FOR_RECEIVE->DONE 执行的动作
     */
    @OnTransition(source = "SHIPPING", target = "SHIPPED")
    public void logisticsDispatch(Message<OrderEvents> message) {
        log.info("---物流派送中---"+"传递的参数：" + message.getHeaders().get("order"));
    }

    /**
     * WAITING_FOR_RECEIVE->DONE 执行的动作
     */
    @OnTransition(source = "SHIPPED", target = "FINISH")
    public void receive(Message<OrderEvents> message) {
        log.info("---用户已收货，订单完成---"+"传递的参数：" + message.getHeaders().get("order"));
    }

    /**
     * WAITING_FOR_RECEIVE->DONE 执行的动作
     */
    @OnTransition(source = "FINISH", target = "REFUNDED")
    public void refund(Message<OrderEvents> message) {
        log.info("---商家已退款---"+"传递的参数：" + message.getHeaders().get("order"));
    }

    /**
     * WAITING_FOR_RECEIVE->DONE 执行的动作
     */
    @OnTransition(source = "UNPAID", target = "CLOSED")
    public void cancel(Message<OrderEvents> message) {
        log.info("---用户已取消订单---"+"传递的参数：" + message.getHeaders().get("order"));
    }
}
