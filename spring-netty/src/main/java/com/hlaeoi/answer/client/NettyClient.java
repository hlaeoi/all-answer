package com.hlaeoi.answer.client;

import com.hlaeoi.answer.handler.NettyClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                //该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
                        //添加编解码
						ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
						ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
						ch.pipeline().addLast(new NettyClientHandler());
					}
				});

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
            log.info("客户端成功....");
            //发送消息
            future.channel().writeAndFlush("你好啊");
            // 等待连接被关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
    	NettyClient client = new NettyClient();
    	client.start();
	}
}
