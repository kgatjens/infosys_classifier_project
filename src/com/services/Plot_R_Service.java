package com.services;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

public class Plot_R_Service {
	
	private RConnection connRServe = null;
	
	public Plot_R_Service(RConnection conn){
		this.connRServe = conn;
	}
	
	public REXP generatePlot(String filenamePlot, String r_Instructions) throws Exception {
        
		REXP xpTemp = null;
		
		try {
			  		 
			this.connRServe.parseAndEval(r_Instructions);
			 xpTemp = this.connRServe.parseAndEval("r=readBin('"+filenamePlot+"','raw',1024*1024); unlink('"+filenamePlot+"'); r");
			
		 }
		 	 
		 catch(Exception e) { 
			// something else
			 e.printStackTrace();
			throw new Exception(e.getMessage());
		 }
		 		 
		 return xpTemp;
		 
    }
	
}
