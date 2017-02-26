package io.goku.chat.core.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tony
 * @createAt Feb 17, 2015
 *
 */
public class IMHandlerManager {
	
	private Map<Short, AbstractIMHandler> mHandlers = null;
	private static IMHandlerManager mInstance = null;
	
	private IMHandlerManager() {
		mHandlers = new HashMap<Short, AbstractIMHandler>();
	}
	
	public static IMHandlerManager getInstance() {
		if (mInstance == null) {
			mInstance = new IMHandlerManager();
		}
		return mInstance;
	}

	public AbstractIMHandler find(Short handlerId) {
		return mHandlers.get(handlerId);
	}
	
	public void register(Class<?> clazz) {
		try {
			AbstractIMHandler handler = (AbstractIMHandler) clazz.newInstance();
			mHandlers.put(handler.getId(), handler);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void register(AbstractIMHandler handler) {
		mHandlers.put(handler.getId(), handler);
	}
}
