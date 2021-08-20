package com.example.zk.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class CountDownTest {

   /* private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        Thread t1  = new Thread(() -> {
                countDownLatch.countDown();
                System.out.println("线程1执行完毕");
                System.out.println("线程1执行完毕：" + countDownLatch.getCount());
        });

        Thread t2  = new Thread(() -> {
            countDownLatch.countDown();
            System.out.println("线程2执行完毕");
            System.out.println("线程2执行完毕：" + countDownLatch.getCount());
        });


//        Thread t3  = new Thread(() -> {
//            try {
//                System.out.println("线程3开始执行等待中。。。。。。");
//                countDownLatch.wait();
//                System.out.println("线程3执行完毕。。。。。。");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        t1.start();
        t2.start();
        countDownLatch.wait();
        System.out.println("我永远是在最后面。。。。。。");
    }*/

    /**
     * 模拟 主线程 依赖 线程A初始化一个数据，才能继续加载后续逻辑
     */
    public static void main(String[] args) throws InterruptedException {
        AtomicReference<String> key = new AtomicReference<>("");
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Thread t = new Thread(() -> {
            try {

                //休眠5秒，模拟数据的初始化
                TimeUnit.SECONDS.sleep(5);

                key.set("核心秘钥123456");
                System.out.println("数据1初始化完毕");

                //释放---此处可以在任何位置调用，很灵活
                countDownLatch.countDown();

                System.out.println("数据2初始化完毕");
                countDownLatch.countDown();

                System.out.println("数据3初始化完毕");
                countDownLatch.countDown();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        t.start();

        //等待数据初始化，阻塞
        countDownLatch.await();
        System.out.println("key：" + key.get());
    }

}
