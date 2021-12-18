package com.geode.net;

import com.geode.logging.Logger;
import com.geode.net.tls.TLSUtils;

import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The type Client.
 */
public class Client implements Initializable, Runnable
{
    private static final Logger logger = new Logger(Client.class);
    /**
     * The Client infos.
     */
    protected final ClientInfos clientInfos;
    /**
     * The Handler.
     */
    protected ClientProtocolHandler handler;
    /**
     * The G state.
     */
    protected GState gState;

    /**
     * Instantiates a new Client.
     *
     * @param clientInfos the client infos
     */
    public Client(ClientInfos clientInfos)
    {
        this.clientInfos = clientInfos;
        gState = GState.DOWN;
    }

    public Socket initSocket() throws Exception
    {
        if(clientInfos.isTLSEnable())
        {
            logger.info("enabling TLS...", getClientInfos().getName());
            SSLSocketFactory factory = TLSUtils.getSocketFactory(clientInfos);
            return factory.createSocket(
                InetAddress.getByName(clientInfos.getHost()),
                clientInfos.getPort()
            );
        }
        return new Socket(getClientInfos().getHost(), getClientInfos().getPort());
    }

    @Override
    public void init()
    {
        logger.info("initialisation", getClientInfos().getName());
        try
        {
            Socket socket = initSocket();
            logger.info("client connected : " + socket, getClientInfos().getName());
            handler = new ClientProtocolHandler(socket, clientInfos.getProtocolClass());
            gState = GState.READY;
        } catch (Exception e)
        {
            logger.fatal("client connection error: " + e.getMessage(), getClientInfos().getName());
            gState = GState.BROKEN;
        }
    }

    @Override
    public void run()
    {
        init();
        if (gState == GState.READY)
        {
            gState = GState.RUNNING;
            handler.start();
            logger.info("client handler is running", getClientInfos().getName());
        } else
        {
            logger.fatal("client " + gState + " can not run", getClientInfos().getName());
        }
    }

    /**
     * Gets protocol class.
     *
     * @return the protocol class
     */
    public Class<?> getProtocolClass()
    {
        return clientInfos.getProtocolClass();
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public ClientProtocolHandler getHandler()
    {
        return handler;
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(ClientProtocolHandler handler)
    {
        this.handler = handler;
    }

    /**
     * Gets handler safe.
     *
     * @return the handler safe
     */
    public ClientProtocolHandler getHandlerSafe()
    {
        while (!handler.isRunning()) ;
        return handler;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public GState getgState()
    {
        return gState;
    }

    /**
     * Sets state.
     *
     * @param gState the g state
     */
    public void setgState(GState gState)
    {
        this.gState = gState;
    }

    /**
     * Gets client infos.
     *
     * @return the client infos
     */
    public ClientInfos getClientInfos()
    {
        return clientInfos;
    }


}
