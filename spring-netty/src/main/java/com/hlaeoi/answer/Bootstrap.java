package com.hlaeoi.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.hlaeoi.answer.server.NettyServer;

import io.netty.channel.ChannelFuture;

/**
 * 应用启动入口
 * @author : hlaeoi
 * @date : 2018/11/16
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hlaeoi.answer"})
public class Bootstrap implements CommandLineRunner {
	
	@Autowired
	private NettyServer server;

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class);
    }

	@Override
	public void run(String... args) throws Exception {
        ChannelFuture future = server.run();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
            	server.destroy();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
	}
    
    
}
