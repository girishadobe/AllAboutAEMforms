package com.aemforms.setvalue.core;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Tag DoR Service Configuration", description = "Tag DoR Service Configuration")
public @interface TagDorServiceConfiguration {
	 @AttributeDefinition(name = "DAM Folder in which you want to store DoR ", description = "DAM Folder in which you want to store DoR")
	  String damFolder();
	 @AttributeDefinition(name = "Document of Record Path as specified in Adaptive Form ", description = "DoR as specified in AF")
	  String dorPath();
	 @AttributeDefinition(name = "Data File Path as specified in Adaptive Form ", description = "Data File Path as speicifed in AF")
	  String dataFilePath();
}
