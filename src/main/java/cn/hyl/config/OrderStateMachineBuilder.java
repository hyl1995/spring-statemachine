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

		try {
			builder.configureConfiguration()
					.withConfiguration()
					.machineId(MACHINEID)
					.beanFactory(beanFactory);

			builder.configureStates()
					.withStates()
					.initial(OrderStates.UNPAID)
					.choice(OrderStates.SELECT_PICK_TYPE)
					.end(OrderStates.CLOSED).end(OrderStates.FINISH)
					.states(EnumSet.allOf(OrderStates.class));

			builder.configureTransitions()
					//创建订单
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.build();
	}
}