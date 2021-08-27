package com.example.zk;

import com.example.zk.config.BootstrapConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 启动类
 */
public class ZookeeperApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BootstrapConfiguration.class);
        context.start();
    }
}
