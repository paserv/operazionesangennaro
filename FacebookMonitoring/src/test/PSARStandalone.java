package test;

import java.util.ArrayList;
import java.util.Iterator;

import it.osg.datasource.facebook.label.PSARData;
import it.osg.service.model.Graph;

public class PSARStandalone {

	public static void main(String[] args) {
		PSARData result = new PSARData();
		
		ArrayList<Graph> gr = result.getGraphData(new Object[]{"Ballaro.Rai", "01-06-2013 00:00:00", "19-06-2013 10:07:06"});
		
		Iterator<Graph> iter = gr.iterator();
		while (iter.hasNext()) {
			Graph curr = iter.next();
			System.out.println(curr.getAxis() + " = " + curr.getOrdinate());
		}
		
	}
	
}
