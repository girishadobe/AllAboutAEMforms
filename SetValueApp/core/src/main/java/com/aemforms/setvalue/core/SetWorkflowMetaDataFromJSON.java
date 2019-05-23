package com.aemforms.setvalue.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;


@Component(property={Constants.SERVICE_DESCRIPTION+"=Set MetaData",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Set Workflow MetaData From JSON Payload"})
public class SetWorkflowMetaDataFromJSON implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(SetWorkflowMetaDataFromJSON.class);
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		log.debug("The process arguments passed ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
		String parameters[] = params.split(",");
		log.debug("The %%%% length of parameters is "+parameters.length);
		WorkflowData wfData = workItem.getWorkflowData();
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		String dataFilePath = payloadPath+"/Data.xml/jcr:content";
		Session session = workflowSession.adaptTo(Session.class);
		//workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
		Node xmlDataNode = null;
		try
			{
				xmlDataNode = session.getNode(dataFilePath);
			}
		catch (PathNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		catch (RepositoryException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
				InputStream xmlDataStream = xmlDataNode.getProperty("jcr:data").getBinary().getStream();
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(xmlDataStream,"UTF-8"));
			    StringBuilder responseStrBuilder = new StringBuilder();
			    String inputStr;
			    while ((inputStr = streamReader.readLine()) != null)
			        responseStrBuilder.append(inputStr);
			    JSONObject jo = new JSONObject(responseStrBuilder.toString());
		      	for(int i=0;i<parameters.length;i++)
				{
		      		JSONObject finalObject = null;
		      		String nameValuePair = parameters[i];
		      		log.debug("###The string I got was ..."+nameValuePair);
		      		
		    		String nameAndValue[] = nameValuePair.split("=");
		    		
		    		String metaDataPropertyName = nameAndValue[0];
		    		String []jsonObjects= nameAndValue[1].split("\\.");
		    	   		
		    		String propertyName = jsonObjects[jsonObjects.length-1];
					log.debug("The property name to extract is ..."+propertyName);
					for(int j=0;j<jsonObjects.length-1;j++)
					{
						if(j==0)
				    	{
				    		finalObject = jo.getJSONObject(jsonObjects[j]);
				    	}
				    	else
				    	{
				    		finalObject = finalObject.getJSONObject(jsonObjects[j]);
				    		System.out.println("The name is .."+finalObject.toString());
				    	}
				    	
					}
					
					wfData.getMetaDataMap().put(metaDataPropertyName,finalObject.get(propertyName));
					log.debug("Create property "+metaDataPropertyName+" and set its value to"+finalObject.get(propertyName));
				}

		       	workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
		       	log.debug("$$$$ Done updating the map");
		       	xmlDataStream.close();
       	
		}
		catch(Exception e)
		{
			
		}

	}

}
