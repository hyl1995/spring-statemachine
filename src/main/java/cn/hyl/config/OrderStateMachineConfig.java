package cn.hyl.config;


import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;


//@Configuration
//@EnableStateMachine(name="orderMachine")
@Slf4j
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {
	@Autowired
	private OrderAction orderAction;
	@Autowired
	private OrderGuard orderGuard;

	@Override
	public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
		log.info("构建订单状态机");

		states.withStates().
				initial(OrderStates.UNPAID).
				states(EnumSet.allOf(OrderStates.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
		transitions
				.withExternal()
				.source(OrderStates.UNPAID).target(OrderStates.WAITING_FOR_RECEIVE)
				.event(OrderEvents.PAY).action(orderAction.orderAction1())
				.and()
				.withExternal()
				.source(OrderStates.WAITING_FOR_RECEIVE).target(OrderStates.SHIPPING)
				.event(OrderEvents.SHIP).guard(orderGuard.orderGuard()).action(orderAction.orderAction2())
				.and()
				.withExternal()
				.source(OrderStates.SHIPPING).target(OrderStates.SHIPPED)
				.event(OrderEvents.LOGISTICS_DISPATCH).action(orderAction.orderAction3())
				.and()
				.withExternal()
				.source(OrderStates.SHIPPED).target(OrderStates.FINISH)
				.event(OrderEvents.RECEIVE).action(orderAction.orderAction4())
				.and()
				.withExternal()
				.source(OrderStates.FINISH).target(OrderStates.REFUNDED)
				.event(OrderEvents.REFUND).action(orderAction.orderAction5())
				.and()
				.withExternal()
				.source(OrderStates.UNPAID).target(OrderStates.CLOSED)
				.event(OrderEvents.CANCEL).action(orderAction.orderAction6());
	}
}