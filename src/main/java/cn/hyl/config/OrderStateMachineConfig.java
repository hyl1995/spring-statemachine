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
				.withInternal()
				.source(OrderStates.UNPAID)
				.event(OrderEvents.CREATE).action(orderAction.create())
				.and()
				//支付订单
				.withExternal()
				.source(OrderStates.UNPAID).target(OrderStates.WAIT_TAKE)
				.event(OrderEvents.PAY).action(orderAction.pay())
				.and()

				//接单
				.withExternal()
				.source(OrderStates.WAIT_TAKE).target(OrderStates.SELECT_PICK_TYPE)
				.event(OrderEvents.TAKE).action(orderAction.take())
				.and()
				//选择提货方式
				.withChoice()
				.source(OrderStates.SELECT_PICK_TYPE)
				.first(OrderStates.DELIVERING, orderGuard.selectPickType(), orderAction.selectDeliver())//配送
				.last(OrderStates.WAIT_PICK_UP, orderAction.selectPickUp())//自提
				.and()

				//已送达
				.withExternal()
				.source(OrderStates.DELIVERING).target(OrderStates.FINISH)
				.event(OrderEvents.ARRIVED).action(orderAction.arrived())
				.and()

				//自提完毕
				.withExternal()
				.source(OrderStates.WAIT_PICK_UP).target(OrderStates.FINISH)
				.event(OrderEvents.PICK_UP_FINISH).action(orderAction.pickUpFinish())
				.and()

				//退款
				.withExternal()
				.source(OrderStates.FINISH).target(OrderStates.REFUNDED)
				.event(OrderEvents.REFUND).action(orderAction.refund())
				.and()
				//取消订单
				.withExternal()
				.source(OrderStates.UNPAID).target(OrderStates.CLOSED)
				.event(OrderEvents.CANCEL).action(orderAction.cancel());
	}
}