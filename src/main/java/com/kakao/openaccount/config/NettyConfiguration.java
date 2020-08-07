package com.kakao.openaccount.config;

import com.kakao.openaccount.netty.handler.SimpleChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

    private final NettyProperties nettyProperties;

    @Bean(name = "bootstrap")
    public Bootstrap bootstrap(SimpleChannelInitializer simpleChannelInitializer) {
        Bootstrap b = new Bootstrap();
        b.group(group())
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .handler(simpleChannelInitializer);
        b.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
        return b;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public EventLoopGroup group() {
        return new NioEventLoopGroup();
    }

    @Bean
    public InetSocketAddress tcpSocketAddress() {
        return new InetSocketAddress(nettyProperties.getHost(), nettyProperties.getPort());
    }

}
