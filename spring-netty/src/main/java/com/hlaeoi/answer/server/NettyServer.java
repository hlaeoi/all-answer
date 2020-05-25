package com.hlaeoi.answer.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hlaeoi.answer.handler.NettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NettyServer {
	
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    @Value("${netty.port}")
    private int port;

    /**
     * 启动服务
     * @throws InterruptedException 
     */
	public ChannelFuture run() throws InterruptedException {
		ServerBootstrap b = new ServerBootstrap(); // (2)
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
				.childHandler(channelInitializer()).option(ChannelOption.SO_BACKLOG, 128) // (5)
				.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

		// Bind and start to accept incoming connections.
		ChannelFuture f = b.bind(port).sync(); // (7)
		channel = f.channel();

		return f;
	}

    public void destroy() {
		log.info("Shutdown Netty Server...");
		if (channel != null) {
			channel.close();
		}
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		log.info("Shutdown Netty Server Success!");
    }
    
    @Bean
    public ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() { 
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				ch.pipeline().addLast(new NettyServerHandler());
			}
		};
    }

}
