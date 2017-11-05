package com.kolawolebalogun.application;


import com.kolawolebalogun.domain.Message;
import com.kolawolebalogun.infrastructure.MessageDecoder;
import com.kolawolebalogun.infrastructure.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KolawoleBalogun on 9/17/17.
 */

@ServerEndpoint(value = "/data-sync/{userName}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class DataSyncEndpoint {
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session, @PathParam("userName") final String userName) throws IOException, EncodeException {
        session.setMaxIdleTimeout(5 * 60 * 1000);
        session.getUserProperties().putIfAbsent("userName", userName);
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        for (Session peer : peers) {
            if (!session.getId().equals(peer.getId())) {
                peer.getBasicRemote().sendObject(message);
            }
        }
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
    }

    @OnMessage
    public void onPongMessage(PongMessage message, Session session) {
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        peers.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        peers.remove(session);
        System.out.println(String.format("%s experience an error \n%s", session.getId(), error.getMessage()));
        try {
            session.close();
        } catch (IOException ignored) {
        }
    }

}
