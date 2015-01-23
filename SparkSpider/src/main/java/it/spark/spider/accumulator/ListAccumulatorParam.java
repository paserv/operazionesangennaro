package it.spark.spider.accumulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.AccumulableParam;

public class ListAccumulatorParam implements AccumulableParam<List<String>, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public List<String> addAccumulator(List<String> arg0, String arg1) {
		arg0.add(arg1);
		return arg0;
	}

	@Override
	public List<String> addInPlace(List<String> arg0, List<String> arg1) {
		arg0.addAll(arg1);
		return arg0;
	}

	@Override
	public List<String> zero(List<String> arg0) {
		return new ArrayList<String>();
	}



}
