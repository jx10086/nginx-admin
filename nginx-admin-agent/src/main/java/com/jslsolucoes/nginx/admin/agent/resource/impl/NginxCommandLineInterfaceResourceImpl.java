package com.jslsolucoes.nginx.admin.agent.resource.impl;

import java.io.File;

import javax.enterprise.context.RequestScoped;

import com.jslsolucoes.runtime.RuntimeBuilder;
import com.jslsolucoes.runtime.RuntimeResult;
import com.jslsolucoes.runtime.RuntimeResultType;

@RequestScoped
public class NginxCommandLineInterfaceResourceImpl {

	public NginxCommandLineInterfaceResourceImpl() {
		
	}
	
	public RuntimeResult killAll() {
		return RuntimeBuilder.newBuilder().withCommand("sudo killall nginx").execute();
	}
	
	public RuntimeResult start(String nginxBin, String nginxHome) {
		return RuntimeBuilder.newBuilder().withCommand("sudo " + nginxBin + " -c " + conf(nginxHome)).execute();
	}
	
	public RuntimeResult stop(String nginxBin, String nginxHome) {
		return RuntimeBuilder.newBuilder().withCommand("sudo " + nginxBin + " -c " + conf(nginxHome) + " -s quit").execute();
	}
	
	public RuntimeResult version(String nginxBin, String nginxHome) {
		return RuntimeBuilder.newBuilder().withCommand("sudo " + nginxBin + " -c " + conf(nginxHome) + " -v").execute();
	}
	
	public RuntimeResult reload(String nginxBin, String nginxHome) {
		return RuntimeBuilder.newBuilder().withCommand("sudo " + nginxBin + " -c " + conf(nginxHome) + " -s reload").execute();
	}
	
	public RuntimeResult testConfiguration(String nginxBin, String nginxHome) {
		return RuntimeBuilder.newBuilder().withCommand("sudo " + nginxBin + " -c " + conf(nginxHome) + " -t").execute();
	}
	
	public RuntimeResult status() {
		return RuntimeBuilder.newBuilder().withCommand("sudo pgrep nginx").execute();
	}
	
	private String conf(String nginxHome) {
		File file = new File(nginxHome,"nginx.conf");
		return file.getAbsolutePath();
	}

	public RuntimeResult restart(String nginxBin, String nginxHome) {
		RuntimeResult runtimeResultForStop = stop(nginxBin, nginxHome);
		RuntimeResult runtimeResultForStart = start(nginxBin, nginxHome);
		return new RuntimeResult(runtimeResultForStop.isSuccess() && runtimeResultForStart.isSuccess() ? RuntimeResultType.SUCCESS : RuntimeResultType.ERROR, runtimeResultForStop.getOutput().concat("\n").concat(runtimeResultForStart.getOutput()));
	}

}