package com.gkframework.qos.server.agent.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectToFileUtils {
	   private static final Logger log = LoggerFactory.getLogger(ObjectToFileUtils.class);
	public static void writeObject(Object object, String filePath) {
		try {
            FileOutputStream outStream = new FileOutputStream(filePath);
			@Cleanup ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(object);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }

    public static Object readObject(String filePath){
    	FileInputStream freader;
    	try {
    		freader = new FileInputStream(filePath);
			@Cleanup ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            return objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
    	return null;
    }  
      
}
