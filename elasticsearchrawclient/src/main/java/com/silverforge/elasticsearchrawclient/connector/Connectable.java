package com.silverforge.elasticsearchrawclient.connector;

import com.silverforge.elasticsearchrawclient.elasticFacade.model.InvokeResult;

public interface Connectable {
	ConnectorSettings getSettings();
	InvokeResult head(String path);
	InvokeResult get(String path);
	InvokeResult post(String path, String data);
	InvokeResult put(String path, String data);
	InvokeResult delete(String path);
	InvokeResult delete(String path, String data);
}
