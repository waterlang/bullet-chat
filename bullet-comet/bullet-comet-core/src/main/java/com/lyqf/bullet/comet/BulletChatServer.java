package com.lyqf.bullet.comet;

import com.lyqf.bullet.comet.netty.handler.AuthHandler;
import com.lyqf.bullet.comet.netty.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author
 */

@Slf4j
public class BulletChatServer {

    private final Integer port;
    private DefaultEventLoopGroup defLoopGroup = null;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap serverBootstrap;
    protected ChannelFuture channelFuture;

    /**
     *
     * @param port
     */
    public BulletChatServer(int port) {
        this.port = port;
    }

    /**
     *
     */
    private void init() {
        defLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "netty-server-defLoop_" + index.incrementAndGet());
            }
        });

        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "netty-server-boss_" + index.incrementAndGet());
            }
        });

        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "netty-server-work_" + index.incrementAndGet());
            }
        });

        serverBootstrap = new ServerBootstrap();
    }

    /**
     * 
     */
    public void start() {
        this.init();

        serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_BACKLOG, 1024).localAddress(new InetSocketAddress(port))
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(defLoopGroup, new HttpServerCodec(), // ???????????????
                        new HttpObjectAggregator(65536), // ?????????????????????????????????????????????
                        new ChunkedWriteHandler(), // ????????????????????????????????????????????????????????????
                        new IdleStateHandler(360, 0, 0), // ???????????????????????????
                        new AuthHandler(), // ?????????????????????
                        new MessageHandler() // ?????????????????????
                    );
                }
            });

        try {
            channelFuture = serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) channelFuture.channel().localAddress();
            log.info("WebSocketServer start success, port is:{}", addr.getPort());

            // ?????????????????????Channel??????????????????Channel todo
//            executorService.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    log.info("scanNotActiveChannel --------");
//                    UserInfoManager.scanNotActiveChannel();
//                }
//            }, 3, 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("WebSocketServer start fail,", e);
        }
    }

}
