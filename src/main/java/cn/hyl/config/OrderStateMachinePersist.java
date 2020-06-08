package cn.hyl.config;
import cn.hyl.entity.OrderDO;
import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

/**
 * 伪持久化
 */
@Component
public class OrderStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, OrderDO> {

	@Override
	public void write(StateMachineContext<OrderStates, OrderEvents> context, OrderDO contextObj) throws Exception {
		//这里不做任何持久化工作
	}

	@Override
	public StateMachineContext<OrderStates, OrderEvents> read(OrderDO contextObj) throws Exception {
		StateMachineContext<OrderStates, OrderEvents> result = new DefaultStateMachineContext<>(OrderStates.getState(contextObj.getStatus()),
				null, null, null, null, "orderMachine");
		return result;
	}
}