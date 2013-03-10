package org.onetwo.test;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Hello world!
 *
 */
public class DiscardServer 
{
    public static void main( String[] args ) {
        ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap boostrap = new ServerBootstrap(factory);
        DiscardServerHandler handler = new DiscardServerHandler();
        ChannelPipeline pipeline = boostrap.getPipeline();
        pipeline.addLast("handler", handler);
        boostrap.setOption("child.tcpNoDelay", true);
        boostrap.setOption("child.keepAlive", true);
        boostrap.bind(new InetSocketAddress(8080));
    }
}
