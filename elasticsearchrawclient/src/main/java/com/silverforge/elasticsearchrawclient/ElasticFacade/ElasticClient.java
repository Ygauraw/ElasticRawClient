package com.silverforge.elasticsearchrawclient.ElasticFacade;

import com.silverforge.elasticsearchrawclient.Connector.Connectable;
import com.silverforge.elasticsearchrawclient.Connector.Connector;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ElasticClient {

	private Connectable connector;

	public Raw raw = new Raw();

	public ElasticClient(ConnectorSettings settings)
		throws MalformedURLException {
		connector = new Connector(settings);
	}

	public void createIndex() {

	}

	public void addDocument() {

	}

	public void bulkDocuments() {

	}

	public void search() {

	}

	public final class Raw {

		private Raw() {}

		public String get()
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.get();
		}

		public String post(String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.post(data);
		}

		public String put(String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.put(data);
		}
	}
}
