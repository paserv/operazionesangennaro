package it.spark.spider.accumulator;

import java.util.HashSet;
import java.util.Set;

import org.apache.spark.AccumulableParam;

public class HashSetAccumulatorParam implements AccumulableParam<Set<String>, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public Set<String> addAccumulator(Set<String> arg0, String arg1) {
		arg0.add(arg1);
		return arg0;
	}

	@Override
	public Set<String> addInPlace(Set<String> arg0, Set<String> arg1) {
		arg0.addAll(arg1);
		return arg0;
	}

	@Override
	public Set<String> zero(Set<String> arg0) {
		return new HashSet<String>();
	}


}
