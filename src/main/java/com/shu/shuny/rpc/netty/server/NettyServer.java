package com.shu.shuny.rpc.netty.server;

import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.common.util.PropertyConfigerHelper;
import com.shu.shuny.model.SunnyRequest;
import com.shu.shuny.serialization.NettyDecoderHandler;
import com.shu.shuny.serialization.NettyEncoderHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.PreDestroy;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyServer {
    private static NettyServer nettyServer = new NettyServer();

    /**
     * 执行通道
     */
    private Channel channel;
    /**
     * 服务端boss线程组
     */
    private EventLoopGroup bossGroup;

    /**
     * 服务端worker线程组
     */
    private EventLoopGroup workerGroup;
    /**
     * 序列化类型配置信息
     */
    private SerializeTypeEnum serializeType = PropertyConfigerHelper.getSerializeType();


    public void start(final int port) {
        synchronized (NettyServer.class) {
            if (bossGroup != null || workerGroup != null) {
                return;
            }

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //注册解码器NettyDecoderHandler
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                    ch.pipeline().addLast(new NettyDecoderHandler(SunnyRequest.class, serializeType));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    //注册编码器NettyEncoderHandler
                    ch.pipeline().addLast(new NettyEncoderHandler(serializeType));
                    //注册服务端业务逻辑处理器NettyServerInvokeHandler
                    ch.pipeline().addLast(new NettyServerInvokeHandler());
                }
            });
            try {
                channel = serverBootstrap.bind(port).sync().channel();
            } catch (InterruptedException e) {
                throw new BizException(e);
            }
        }
    }


    /**
     * 停止Netty服务
     */
    public void stop() {
        if (null == channel) {
            throw new BizException("Netty Server Stoped");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }


    public static NettyServer singleton() {
        return nettyServer;
    }

    @PreDestroy
    private void destroy() {
        stop();
    }
}
