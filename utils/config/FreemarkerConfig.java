package com.zhph.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhph.tag.Auth;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;


@Component
public class FreemarkerConfig {
	
	  @Autowired
	  private Configuration configuration;
	  
	  
	  @Autowired
	  private Auth auth;

	  @PostConstruct
	  public void setSharedVariable() throws TemplateModelException {
	    configuration. setSharedVariable("auth", auth);
	  }

}
