/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.florincernat.webchat;

/**
 *
 * @author florin
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
 
@ServerEndpoint(value = "/chat/{room}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class WebChatEndpoint {
	
 
	@OnOpen
	public void open(final Session session, @PathParam("room") final String room) {
		System.out.println("session openend, room: " + room);
		session.getUserProperties().put("room", room);
	}
 
	@OnMessage
	public void onMessage(final Session session, final ChatMessage chatMessage) {
		String room = (String) session.getUserProperties().get("room");
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()&& room.equals(s.getUserProperties().get("room"))) {
					s.getBasicRemote().sendObject(chatMessage);
				}
			}
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
}
