package com.lyqf.bullet.client;

import com.lyqf.bullet.client.handler.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author
 */

@Slf4j
@SuppressWarnings("all")
public class BulletChatClient {

    private static final EventLoopGroup group = new NioEventLoopGroup();

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                final String url = "ws://127.0.0.1:8889?roomId=1&token=" + atomicInteger.incrementAndGet();
                final BulletChatClient client = new BulletChatClient();
                URI uri = URI.create(url);
                try {
                    client.open(uri);
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void open(URI uri) throws Exception {
        Bootstrap b = new Bootstrap();
        String protocol = uri.getScheme();
        if (!"ws".equals(protocol)) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }

        // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
        // If you change it to V00, ping is not supported and remember to change
        // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
        final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
            .newHandshaker(uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, 1280000));

        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpClientCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("ws-handler", handler);
            }
        });

        // System.out.println("WebSocket Client connecting");
        b.connect(uri.getHost(), uri.getPort()).sync().channel();
        log.info("conncet success");
        handler.handshakeFuture().sync();
    }

}
