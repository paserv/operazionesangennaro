package it.pipe.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.csvreader.CsvReader;

public class PipelineEngine {

	private String configFilePath;
	private static String methodName = "getOutput";
	private static String confFolder = "conf/";
	
	private static ArrayList<PipeModule> allModules;
	

	public static void main(String[] args) {

		PipelineEngine eng = new PipelineEngine("conf/pipeline/facebookPipeline.txt");
		eng.run();
		
	}

	public PipelineEngine(String path) {
		setConfigFilePath(path);
		allModules = getModules();
	}
	
	public ArrayList<String> run() {
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		
		int size = allModules.size();
		for (int i = 0; i < size; i++){
				String currModule = allModules.get(i).getModuleName();
				try {
					Class currMod = Class.forName(currModule);
					Constructor constr = currMod.getConstructor(String.class, String.class);
					Object currObj = constr.newInstance(allModules.get(i).getModuleName(), confFolder + allModules.get(i).getConfPath());
					
					Method getName = currMod.getMethod("getModuleName", null);
					
					Method[] met = currMod.getMethods();
					for (int j=0; j < met.length; j++) {
						Method currMet = met[j];
						if (currMet.getName().equalsIgnoreCase(methodName)) {
							System.out.println("Step " + i + ": " + getName.invoke(currObj));
							System.out.println("\tInitial size: " + output.size());
							temp = (ArrayList<String>) currMet.invoke(currObj, output);
							output = temp;
							System.out.println("\tFinal size: " + output.size());
						}
					}
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			
		}
		
		System.out.println("Final size is " + output.size());
		return output;
	}

	
	

	public ArrayList<PipeModule> getModules() {
		ArrayList<PipeModule> result = new ArrayList<PipeModule>();
		CsvReader reader;
		try {
			reader = new CsvReader(configFilePath);
			reader.readHeaders();
			while (reader.readRecord()){
				String currModule = reader.get("module");
				String currConf = reader.get("conf");
				PipeModule currPM = new PipeModule(currModule, currConf);
				result.add(currPM);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public static ArrayList<PipeModule> getAllModules() {
		return allModules;
	}

	public static void setAllModules(ArrayList<PipeModule> allModules) {
		PipelineEngine.allModules = allModules;
	}
	
	

}
