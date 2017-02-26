package com.taobao.diamond.client.impl;

import static com.taobao.diamond.client.impl.DiamondEnv.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 事件订阅和发布工具类。
 */
public class EventDispatcher {

    /**
     * 添加事件监听器
     */
    static public void addEventListener(EventListener listener) {
        for (Class<? extends Event> type : listener.interest()) {
            getListenerList(type).addIfAbsent(listener);
        }
    }

    /**
     * 发布事件，首先发布该事件暗示的其他事件，最后通知所有对应的监听器。
     */
    static public void fireEvent(Event event) {
        if (null == event) { // 保护
            return;
        }

        // 发布该事件暗示的其他事件
        for (Event implyEvent : event.implyEvents()) {
            try {
                if (event != implyEvent) { // 避免死循环
                    fireEvent(implyEvent);
                }
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }

        for (EventListener listener : getListenerList(event.getClass())) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }
    }

    // 多线程安全
    static synchronized CopyOnWriteArrayList<EventListener> getListenerList(
            Class<? extends Event> eventType) {
        CopyOnWriteArrayList<EventListener> listeners = listenerMap.get(eventType);
        if (null == listeners) {
            listeners = new CopyOnWriteArrayList<EventListener>();
            listenerMap.put(eventType, listeners);
        }
        return listeners;
    }

    // ========================
    
    static final Map<Class<? extends Event>, CopyOnWriteArrayList<EventListener>> listenerMap //
    = new HashMap<Class<? extends Event>, CopyOnWriteArrayList<EventListener>>();

    // ========================

    /**
     * Client事件。
     */
    static public abstract class Event {
        /**
         * 该事件可能暗示了其他事件。默认实现为空。
         */
        @SuppressWarnings("unchecked")
        protected List<Event> implyEvents() {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 事件监听器。
     */
    static public abstract class EventListener {
        public EventListener() {
            EventDispatcher.addEventListener(this); // 自动注册给EventDispatcher
        }
        
        /**
         * 感兴趣的事件列表
         */
        abstract public List<Class<? extends Event>> interest();

        /**
         * 处理事件
         */
        abstract public void onEvent(Event event);
    }
    
    /** serverList has changed */
    static public class ServerlistChangeEvent extends Event {}
}