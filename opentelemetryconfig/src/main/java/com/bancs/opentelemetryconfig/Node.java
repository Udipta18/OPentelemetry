package com.bancs.opentelemetryconfig;

import io.opentelemetry.context.Context;

public class Node {
	
	private Context parent;
	private Context current;
	public Context getParent() {
		return parent;
	}
	public void setParent(Context parent) {
		this.parent = parent;
	}
	public Context getCurrent() {
		return current;
	}
	public void setCurrent(Context current) {
		this.current = current;
	}
	
	

}
