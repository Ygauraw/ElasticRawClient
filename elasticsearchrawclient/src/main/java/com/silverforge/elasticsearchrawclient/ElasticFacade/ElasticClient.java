package com.silverforge.elasticsearchrawclient.ElasticFacade;

import com.silverforge.elasticsearchrawclient.Connector.Connectable;
import com.silverforge.elasticsearchrawclient.Connector.Connector;
import com.silverforge.elasticsearchrawclient.Connector.ConnectorSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ElasticClient {
	private Connectable connector;
	public Raw raw = new Raw();

	public ElasticClient(Connectable connector)
		throws MalformedURLException {
		this.connector = connector;
	}

	public ElasticClient(ConnectorSettings settings)
		throws URISyntaxException {
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

		public String get(String path)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.get(path);
		}

		public String post(String path, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.post(path, data);
		}

		public String put(String path, String data)
			throws NoSuchAlgorithmException, IOException, KeyManagementException {

			return connector.put(path, data);
		}
	}
}
