package cn.hyl;

import cn.hyl.config.OrderStateMachineBuilder;
import cn.hyl.entity.OrderDO;
import cn.hyl.entity.OrderEvents;
import cn.hyl.entity.OrderStates;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableStateMachine
@Slf4j
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;
    @Autowired
    private StateMachinePersister<OrderStates, OrderEvents, OrderDO> persister;

    @Override
    public void run(String... args) throws Exception {
        Long time1 = System.currentTimeMillis();
        singleTest();
        Long time2 = System.currentTimeMillis();
        System.out.println(String.format("总耗时：%s", time2 - time1));
    }


    /**
     * 单线程测试
     * @throws Exception
     */
    private void singleTest() throws Exception {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);

        // 创建流程
//        stateMachine.start();

//        // 触发PAY事件
//        stateMachine.sendEvent(OrderEvents.PAY);
//
//        // 触发RECEIVE事件
//        //用message传递数据
//        OrderDO order = new OrderDO();
//        order.setId(23L);
//        order.setStatus(2);
//        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.RECEIVE).setHeader("order", order).setHeader("otherObj", "otherObjValue").build();
//        stateMachine.sendEvent(message);
//
//        // 获取最终状态
//        log.info("最终状态：" + stateMachine.getState().getId());

        //从数据库获取订单状态后，构建状态机
        OrderDO order2 = new OrderDO();
        order2.setId(50L);
        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());

        //恢复
        persister.restore(stateMachine, order2);
        //查看恢复后状态机的状态
        System.out.println("恢复后的状态：" + stateMachine.getState().getId());
        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.TAKE).setHeader("order", order2).setHeader("otherObj", "otherObjValue").build();
        stateMachine.sendEvent(message);
        log.info("最终状态：" + stateMachine.getState().getId());
        order2 = new OrderDO();
        order2.setId(50L);
        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());

//        //恢复
//        persister.restore(stateMachine, order2);
//        //查看恢复后状态机的状态
//        System.out.println("恢复后的状态：" + stateMachine.getState().getId());
//        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
//        message = MessageBuilder.withPayload(OrderEvents.TAKE).setHeader("order", order2).setHeader("otherObj", "otherObjValue").build();
//        stateMachine.sendEvent(message);
//        log.info("最终状态：" + stateMachine.getState().getId());
    }

    /**
     * 多线程测试
     */
    public void ThreadPoolTest() {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        AtomicLong i = new AtomicLong(0);
        try {
            for (int j = 0; j < 20; j++) {
                singleThreadPool.execute(() -> {
                    try {
                        Long time1 = System.currentTimeMillis();
                        StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder.build(beanFactory);
                        Long time2 = System.currentTimeMillis();
                        OrderDO order2 = new OrderDO();
                        order2.setId(i.incrementAndGet());
                        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
                        //恢复

                        persister.restore(stateMachine, order2);

                        Long time3 = System.currentTimeMillis();
                        //查看恢复后状态机的状态
                        System.out.println("恢复后的状态：" + stateMachine.getState().getId());
                        order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
                        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.TAKE).setHeader("order", order2).setHeader("otherObj", "otherObjValue").build();
                        stateMachine.sendEvent(message);
                        log.info("最终状态：" + stateMachine.getState().getId());
                        Long time4 = System.currentTimeMillis();
                        System.out.println(String.format("构建耗时：%s， 恢复耗时：%s， 事件耗时：%s", (time2 - time1), (time3 - time2), (time4 - time3)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        singleThreadPool.shutdown();
    }

    private void statemachineTest() throws Exception {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

        AtomicLong i = new AtomicLong(0);
        try {
            for (int j = 0; j < 20; j++) {
                singleThreadPool.execute(() -> {
                    StateMachine stateMachine = beanFactory.getBean(StateMachine.class);
                    Long time1 = System.currentTimeMillis();
                    Long time2 = System.currentTimeMillis();
                    OrderDO order2 = new OrderDO();
                    order2.setId(i.incrementAndGet());
                    order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
                    //恢复
                    try {
                        persister.restore(stateMachine, order2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Long time3 = System.currentTimeMillis();
                    //查看恢复后状态机的状态
                    System.out.println("恢复后的状态：" + stateMachine.getState().getId());
                    order2.setStatus(OrderStates.WAIT_TAKE.ordinal());
                    Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.TAKE).setHeader("order", order2).setHeader("otherObj", "otherObjValue").build();
                    stateMachine.sendEvent(message);
                    log.info("最终状态：" + stateMachine.getState().getId());
                    Long time4 = System.currentTimeMillis();
                    System.out.println(String.format("构建耗时：%s， 恢复耗时：%s， 事件耗时：%s", (time2 - time1), (time3 - time2), (time4 - time3)));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        singleThreadPool.shutdown();
    }
}
