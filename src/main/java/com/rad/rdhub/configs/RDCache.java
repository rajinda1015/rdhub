package com.rad.rdhub.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@RefreshScope
@Configuration(value = "rdCache")
public class RDCache {

	@Autowired
	private EurekaClient eurekaClient;
	
	@Value("${registered.service.id.rduserportal}")
	private String appNameUserPortal;
	
	@Value("${registered.service.id.rdhub}")
	private String appNameHub;
	
	@Value("${communication.protocol.rduserportal}")
	private String comProtocolUserPortal;
	
	@Value("${communication.protocol.rdhub}")
	private String comProtocolHub;
	
	public StringBuffer userPortalUrl;
	public StringBuffer hubUrl;
	
	public String getUserPortalInfo() {
		if (null == userPortalUrl || 0 == userPortalUrl.length()) {
			userPortalUrl = new StringBuffer();
			Application application = eurekaClient.getApplication(appNameUserPortal);
			InstanceInfo info = application.getInstances().get(0);
			userPortalUrl.append(comProtocolUserPortal).append(info.getIPAddr() + ":" + info.getPort());
		}
		return userPortalUrl.toString();
	}
	
	public String getHuInfo() {
		if (null == hubUrl || 0 == hubUrl.length()) {
			hubUrl = new StringBuffer();
			Application application = eurekaClient.getApplication(appNameHub);
			InstanceInfo info = application.getInstances().get(0);
			hubUrl.append(comProtocolHub).append(info.getIPAddr() + ":" + info.getPort());
		}
		return hubUrl.toString();
	}
}
