package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderAction {
    public Action<OrderStates, OrderEvents> orderAction0() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action0方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction1() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action1方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction2() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action2方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction3() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action3方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction4() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action4方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction5() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action5方法执行"+ context.getMessageHeaders().get("order"));

        };
    }

    public Action<OrderStates, OrderEvents> orderAction6() {

        return (StateContext<OrderStates, OrderEvents> context) -> {
            System.out.println("action6方法执行"+ context.getMessageHeaders().get("order"));

        };
    }
}
