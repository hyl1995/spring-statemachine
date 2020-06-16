package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderAction {

    public Action<OrderStates, OrderEvents> create() {
        return context -> {
            log.info("---订单创建，待支付---" + context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> pay() {
        return context -> {
            log.info("---用户完成支付，待收货---" + "传递的参数：" + context.getMessageHeaders().get("order"));

        };
    }

    /**
     * @description: 接单操作
     * @author jack
     * @date 2020/6/10 19:56
     */
    public Action<OrderStates, OrderEvents> take() {
        return context -> {
            log.info("---商家已接单---" + "传递的参数：" + context.getMessageHeaders().get("cmd"));
        };
    }

    /**
     * 选择配送
     * @return
     */
    public Action<OrderStates, OrderEvents> selectDeliver() {
        return context -> {
            log.info("---商家配送---" + "传递的参数：" + context.getMessageHeaders().get("order"));

        };
    }

    /**
     * 选择自提
     * @return
     */
    public Action<OrderStates, OrderEvents> selectPickUp() {
        return context -> {
            log.info("---用户自提---" + "传递的参数：" + context.getMessageHeaders().get("order"));

        };
    }

    /**
     * @description: 完成配货
     * @author jack
     * @date 2020/6/10 20:30
     */
    public Action<OrderStates, OrderEvents> prepareFinish() {
        return context -> {
            log.info("---用户完成支付，待收货---" + "传递的参数：" + context.getMessageHeaders().get("order"));
        };
    }

    /**
     * @description: 立即配送
     * @author jack
     * @date 2020/6/10 20:30
     */
    public Action<OrderStates, OrderEvents> deliver() {
        return context -> {
            log.info("---用户完成支付，待收货---" + "传递的参数：" + context.getMessageHeaders().get("order"));
        };
    }

    /**
     * @description: 确认送达
     * @author jack
     * @date 2020/6/10 20:30
     */
    public Action<OrderStates, OrderEvents> arrived() {
        return context -> {
            log.info("---用户完成支付，待收货---" + "传递的参数：" + context.getMessageHeaders().get("order"));
        };
    }


    public Action<OrderStates, OrderEvents> pickUpFinish() {
        return context -> {
            log.info("---用户完成支付，待收货---" + "传递的参数：" + context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> receive() {
        return context -> {
            log.info("---执行订单确认收货操作-----start" + "传递的参数：" + context.getMessageHeaders().get("cmd"));
        };
    }

    public Action<OrderStates, OrderEvents> refund() {
        return context -> {
            log.info("---商家已退款---" + "传递的参数：" + context.getMessageHeaders().get("order"));
        };
    }

    public Action<OrderStates, OrderEvents> cancel() {
        return context -> {
            log.info("---用户取消订单状态机事件-----start" + "传递的参数：" + context.getMessageHeaders().get("order"));
        };
    }
}
