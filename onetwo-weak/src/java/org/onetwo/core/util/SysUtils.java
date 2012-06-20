package org.onetwo.core.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.onetwo.core.exception.ServiceException;

public abstract class SysUtils {

	public static void handleException(Exception e){
		if(e instanceof ServiceException){
			throw (ServiceException) e;
		}else{
			throw new ServiceException(e);
		}
	}
	
	public static void close(Closeable io){
		if(io!=null){
			try {
				io.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String exec(String cmd) {
		return exec(cmd, null, null);
	}
	

	public static String exec(String cmd, String[] envp, File dir) {
		return exec(cmd, envp, dir, null);
	}

	public static String exec(String cmd, String[] envp, File dir, AExecute executeable) {
		System.out.println("execute : " + cmd);
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		Process processor = null;
		try {
			processor = Runtime.getRuntime().exec(cmd.trim(), null, dir);
			reader = new BufferedReader(new InputStreamReader(processor.getInputStream()));

			String str = null;
			while ((str = reader.readLine()) != null) {
				sb.append(str).append("\n");
				if(executeable!=null){
					executeable.execute(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SysUtils.close(reader);
			if(processor!=null)
				processor.destroy();
		}
		return sb.toString();
	}
}
