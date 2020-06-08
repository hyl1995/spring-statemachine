package cn.hyl.config;

import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
@Slf4j
public class OrderStateMachineBuilder {

	private final static String MACHINEID = "orderMachine";
	@Autowired
	private OrderAction orderAction;
	@Autowired
	private OrderGuard orderGuard;

	/**
	  * 构建状态机
	  * 
	 * @param beanFactory
	 * @return
	 * @throws Exception
	 */
	public StateMachine<OrderStates, OrderEvents> build(BeanFactory beanFactory) throws Exception {
		 StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

		 log.info("构建订单状态机");

		builder.configureStates()
				.withStates()
				.initial(OrderStates.UNPAID)
				.end(OrderStates.CLOSED).end(OrderStates.FINISH)
				.states(EnumSet.allOf(OrderStates.class));
		 
		 builder.configureStates()
		 			.withStates()
		 			.initial(OrderStates.UNPAID)
		 			.states(EnumSet.allOf(OrderStates.class));

		 builder.configureTransitions()
					 .withInternal()//创建订单
					 .source(OrderStates.UNPAID)
					 .event(OrderEvents.CREATE).action(orderAction.orderAction0())
					 .and()
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
		 return builder.build();
	 }
}