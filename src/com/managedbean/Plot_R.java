package com.managedbean;

import java.io.ByteArrayInputStream;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.exceptions.RserveProcessingException;
import com.services.Plot_R_Service;

@ManagedBean (name = "plotr")
public class Plot_R {

	private StreamedContent plotRImage;
	private String messageResponse;

	/*==========================================================================================*/
	
	public StreamedContent getPlotRImage() {
		return plotRImage;
	}

	public void setPlotRImage(StreamedContent plotRImage) {
		this.plotRImage = plotRImage;
	}

	public String getMessageResponse() {
		return messageResponse;
	}

	public void setMessageResponse(String messageResponse) {
		this.messageResponse = messageResponse;
	}
	
	/*==========================================================================================*/
		
	public Plot_R(){
		
		String device = "jpeg";
		String filenamePlot = "test.jpg";
		RConnection c = null;
		REXP xp;
		
		String rInstructions = "data(iris); attach(iris); plot(Sepal.Length, Petal.Length, col=unclass(Species)); dev.off()";
		
		String errR_Processing = "";
		
		try{
			
			c = new RConnection("127.0.0.1");
			xp = c.parseAndEval("try("+device+"('"+filenamePlot+"',quality=100))");
			
			if (c.parseAndEval("suppressWarnings(require('Cairo',quietly=TRUE))").asInteger()>0)
				 device = "CairoJPEG"; 
			 else
				 System.out.println("(consider installing Cairo package for better bitmap output)");
		 
			 if(xp.inherits("try-error")) { 
				 
				 errR_Processing = "Can't open "+device+" graphics device: " + xp.asString() + "<br />";
				 
				 REXP w = c.eval("if (exists('last.warning') && length(last.warning)>0) names(last.warning)[1] else 0");
				
				if (w.isString()) {
					errR_Processing+= w.asString() + "<br />";
				}
				
				throw new RserveProcessingException(errR_Processing);
				
			 }			
				
			 Plot_R_Service servicePlot = new Plot_R_Service(c);

			 xp = servicePlot.generatePlot(filenamePlot, rInstructions);
					 
			 plotRImage = new DefaultStreamedContent(new ByteArrayInputStream(xp.asBytes()), "images/jpeg");
			 
			 this.setMessageResponse("R Plot Generated Successfully...");
			
		}
		
		catch(RserveProcessingException e){
			this.setMessageResponse(e.getMessage());
		}		
				
		catch (RserveException rse) { 
			//RserveException (transport layer - e.g. Rserve is not running)
			this.setMessageResponse(rse.getMessage());
			
		}
		
		catch(REXPMismatchException mme){
			// REXP mismatch exception (we got something we didn't think we get)
			this.setMessageResponse(mme.getMessage());
			mme.printStackTrace();			
		}
		
		catch(Exception e) { 
			//something else
			this.setMessageResponse("Something went wrong, but it's not the Rserve: " + e.getMessage());
			e.printStackTrace();
		}
		
		finally{
			c.close();
		}
		
	}
	
	
}
