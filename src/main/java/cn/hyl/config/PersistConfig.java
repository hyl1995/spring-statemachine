package cn.hyl.config;

import cn.hyl.entity.OrderDO;
import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class PersistConfig {
	@Autowired
	private OrderStateMachinePersist orderStateMachinePersist;

	
	@Bean(name="orderPersister")
    public StateMachinePersister<OrderStates, OrderEvents, OrderDO> orderPersister() {
		return new DefaultStateMachinePersister<>(orderStateMachinePersist);
	}

}