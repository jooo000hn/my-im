package io.goku.chat.core.handler;

import io.goku.chat.core.connetion.IMConnection;

/**
 * Handler
 * 
 * @author Tony
 * @createAt Feb 17, 2015
 *
 */
public abstract class AbstractIMHandler<T> implements IHandler {
	
	public abstract short getId();
	
	public abstract void dispatch(IMConnection connection, T data);
	
}
