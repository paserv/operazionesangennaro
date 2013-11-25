package test;

public class Test {

	public static void main(String[] args) {
		String start = "Non si pagherà la Tares sulle aree occupate con dehor e plateatici comunque utilizzati. Nell’incontro con il Comune, le associazioni di categoria avevano chiesto di escludere i plateatici dall’applicazione della maggiorazione del 100% sul canone";
		System.out.println(start);
		String nuovaStr = start.replaceAll ("[ \\p{Punct}]", " ");
		String nuovaStr2 = nuovaStr.replaceAll("  ", " ");
		System.out.println(nuovaStr);
		System.out.println(nuovaStr2);
	}
	
}
