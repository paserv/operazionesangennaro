package it.osg.datasource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;



public abstract class TableSourceGenerator {

	public abstract ArrayList<String> getTableData(Object[] objects);
	
	public static ArrayList<String> getData(String className, Object[] objects) {
		ArrayList<String> result = new ArrayList<String>();
		
		Class currClass;
		try {
			currClass = Class.forName(className);
			Constructor constr = currClass.getConstructor();
			Object currObj = constr.newInstance();
			
			//Method getName = currClass.getMethod("getGraphData", null);
			
			Method[] met = currClass.getMethods();
			for (int j=0; j < met.length; j++) {
				Method currMet = met[j];
				if (currMet.getName().equalsIgnoreCase("getTableData")) {
					//System.out.println("Invoking " + getName.invoke(currObj));
					result = (ArrayList<String>) currMet.invoke(currObj, (Object)objects);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}

	

	
	
}
