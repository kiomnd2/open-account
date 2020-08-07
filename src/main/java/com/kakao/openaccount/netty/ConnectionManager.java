package com.kakao.openaccount.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
@Component
@Slf4j
public class ConnectionManager {

    private final Bootstrap bootstrap;
    private final InetSocketAddress socketAddress;
    private Channel channel;

    public Channel start() {
        try{
            ChannelFuture f = bootstrap.connect(socketAddress).sync();
            log.info("connect is started host {}",socketAddress.getHostName());
            channel = f.channel().closeFuture().sync().channel();

        }catch (InterruptedException e ) {
            Thread.currentThread().interrupt();
        }

        return channel;
    }

    @PreDestroy
    public void stop() {
        if ( channel != null ) {
            channel.close();
            channel.parent().close();
        }
    }

}
