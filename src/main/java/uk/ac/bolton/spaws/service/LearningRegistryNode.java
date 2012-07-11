/*
 *  (c) 2012 University of Bolton
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.bolton.spaws.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import uk.ac.bolton.spaws.model.impl.Node;

public class LearningRegistryNode{
	
	static Logger logger = Logger.getLogger(LearningRegistryNode.class.getName());
	
	private String url = "http://alpha.mimas.ac.uk";
	private String username = "fred";
	private String password = "flintstone";
	private Node node;
	
	private static  LearningRegistryNode instance;
	
	public static LearningRegistryNode getInstance(){
		if (instance == null) instance = new LearningRegistryNode();
		return instance;
	}
	
	private LearningRegistryNode(){
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration("service.properties");
			if (properties.containsKey("spaws.node.location")) url = properties.getString("spaws.node.location");
			if (properties.containsKey("spaws.node.username")) username = properties.getString("spaws.node.username");
			if (properties.containsKey("spaws.node.password")) password = properties.getString("spaws.node.password");
		} catch (ConfigurationException e) {
			logger.error("error loading properties from configuration file; using defaults instead");
		}
		
		try {
			node = new Node(new URL(url), username, password);
		} catch (MalformedURLException e) {
			logger.warn("SPAWS node URL is invalid; using defaults");
			try {
				node = new Node(new URL(url), username, password);
			} catch (MalformedURLException e1) {
				// Should never happen...
			}

		}
	}
	
	public Node getNode(){
		return node;
	}
	
	

}
