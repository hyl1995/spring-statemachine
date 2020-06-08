package cn.hyl;

import cn.hyl.config.OrderStateMachineBuilder;
import cn.hyl.entity.OrderDO;
import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class Test {
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;
    @Autowired
    private StateMachinePersister<OrderStates, OrderEvents, OrderDO> persister;

}
