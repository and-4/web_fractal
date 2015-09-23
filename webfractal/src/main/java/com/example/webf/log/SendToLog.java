package com.example.webf.log;

import org.apache.log4j.Logger;

/*
 * Класс для вывода в консоль данных
 */

public class SendToLog {
	
	public final static boolean DEBUG = true;   
	final static Logger logger = Logger.getLogger(""); 
	
	public static boolean log(){
		if (DEBUG) {
			logger.fatal("SendToLog() ");}
		return true;
	}
	  
	public static boolean log(String str){
		if (DEBUG) {
			logger.fatal(str);}
		return true;
	}
	
	public static boolean log(Object obj){
		if (DEBUG) {
			logger.fatal(obj.toString());}
		return true;
	}
	
	/*
	 * Выводит в консоль строку с указанием вызывающего класса и номера строки в классе
	 */
	public static boolean longLog(String str){
		if (DEBUG) {
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			logger.fatal(className + "  " +  lineNumber +"  "+ str);}
		return true;
	}

}
