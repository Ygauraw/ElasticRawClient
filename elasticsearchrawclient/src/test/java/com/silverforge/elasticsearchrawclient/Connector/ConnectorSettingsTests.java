package com.silverforge.elasticsearchrawclient.Connector;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectorSettingsTests {

	@Test
	public void Test1() {
		ConnectorSettings settings = ConnectorSettings.builder().build();
		assertEquals(null, settings.getBaseUrl());
	}
}