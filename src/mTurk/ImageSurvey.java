package mTurk;

import java.awt.Desktop;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;

import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;
import com.amazonaws.mturk.dataschema.QuestionFormAnswersType.AnswerType;
import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.QualificationRequirement;

/*
import com.amazonaws.mturk.requester.ReviewPolicy;
import com.amazonaws.mturk.requester.HITLayoutParameter;
*/

public class ImageSurvey {
	
	private RequesterService service;
	
	private String hitTypeId;
	private String image_url;
	private String image1_url;
	
    private String title = "Similarity of two images";
    private String description="On what level do you think the images are similar with each other ??";
    private String keywords = "similarity";
    private String hitLayoutId="3JB2H6P6URZ0BAXZ4Q8KI5GEKVSJ0C";
    private String hitUrl;
    private String defaultHitUrl = "https://www.mturk.com/mturk/welcome";
    
    private int maxAssignments = 4;
    private double reward = 0.00;
    
    private QualificationRequirement[] qualRequirements;
    
    private Map<String, String > hitLayoutParameters = new HashMap<String,String>();
    
    private HIT hit;

	public ImageSurvey(String url, String url1)
	{

		service = new RequesterService(new PropertiesClientConfig());
		this.image_url = url;
		this.image1_url = url1;
	}
	

    public void createImageSurvey()
    {
    	
	     try 
	     {
	    	
	    	this.hitLayoutParameters.put("image_url", image_url);
	    	this.hitLayoutParameters.put("image1_url", image1_url);
			this.hitTypeId = service.registerHITType(
					RequesterService.DEFAULT_AUTO_APPROVAL_DELAY_IN_SECONDS, 
	    			RequesterService.DEFAULT_ASSIGNMENT_DURATION_IN_SECONDS, 
	    			reward, 
	    			title, 
	    			keywords, 
	    			description, 
	    			qualRequirements
	    			);
	    			
			System.out.println("HIT type ID \n" + hitTypeId);
			hit = service.createHIT(title, description, reward, maxAssignments, hitLayoutId, hitLayoutParameters); 

	       // Print out the HITId and the URL to view the HIT.
	       System.out.println("Created HIT: \n" + hit.getHITId());
	       System.out.println("HIT location: ");
	       hitUrl = service.getWebsiteURL() + "/mturk/preview?groupId=" + hit.getHITTypeId();
	       System.out.println(hitUrl);
	     }
	    catch (ServiceException e) 
	    {
	       System.err.println(e.getLocalizedMessage() + " endl");
	    }
	  }
    
    @SuppressWarnings("null")
	public String getHitResult()
    {
       String results = "";
       Assignment[] assignments = service.getAllAssignmentsForHIT(hit.getHITId());
       for (Assignment assign : assignments)
       {
    	   /*
    	   AnswerType answerType = null;
    	   RequesterService.getAnswerValue(assign.getAssignmentId(), answerType);
    	   */
    	   assign.getAnswer();
    	   results += assign.getAnswer();
       }
    	return results;
    }
    
    
    public void answerSurveyManually()
    {
    	System.out.println("No worker has accepted your hit, please enter the distance by yourself....!");
    	
    	try
    	{
    		Thread.sleep(5000);
    	}
    	catch(InterruptedException ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	
    	Desktop d=Desktop.getDesktop();
		try
		{
			if(hitUrl != null)
			{
				d.browse(new URI(hitUrl));
			}
			else
			{
				d.browse(new URI(defaultHitUrl));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    	
    }

}
