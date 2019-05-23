package com.aemformssamples.configuration;

import java.lang.annotation.Annotation;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="AEM Forms Samples Doc Services Configuration", description="AEM Forms Samples Doc Services Configuration")
public @interface DocSvcConfiguration
{
  @AttributeDefinition(name="Allow Form Fill", description="Allow Form Fill", type=AttributeType.BOOLEAN)
  boolean FormFill() default false;
  
  @AttributeDefinition(name="Allow BarCode Decoding", description="Allow BarCode Decoding", type=AttributeType.BOOLEAN)
  boolean BarcodeDecoding() default false;
  
  @AttributeDefinition(name="Allow File Embedding", description="Allow File Embedding", type=AttributeType.BOOLEAN)
  boolean EmbeddingFiles() default false;
  
  @AttributeDefinition(name="Allow Commenting", description="Allow Commenting", type=AttributeType.BOOLEAN)
  boolean Commenting() default false;
  
  @AttributeDefinition(name="Allow DigitialSignatures", description="Allow File DigitialSignatures", type=AttributeType.BOOLEAN)
  boolean DigitialSignatures() default false;
  
  @AttributeDefinition(name="Allow FormDataExportImport", description="Allow FormDataExportImport", type=AttributeType.BOOLEAN)
  boolean FormDataExportImport() default false;
  
  @AttributeDefinition(name="Reader Extension Alias", description="Alias of your Reader Extension")
  String ReaderExtensionAlias() default "";
}
