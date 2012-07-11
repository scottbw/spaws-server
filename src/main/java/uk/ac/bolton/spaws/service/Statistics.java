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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.bolton.spaws.ParadataManager;
import uk.ac.bolton.spaws.filter.NormalizingFilter;
import uk.ac.bolton.spaws.model.IStats;
import uk.ac.bolton.spaws.model.ISubmission;
import uk.ac.bolton.spaws.model.impl.Node;
import uk.ac.bolton.spaws.model.impl.Submitter;

public class Statistics extends HttpServlet {

	private static final long serialVersionUID = -3938800114662500571L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String widgetId = req.getParameter("id");
		String submitterName = req.getParameter("submitter");
		String external = req.getParameter("external");
		
		Node node = LearningRegistryNode.getInstance().getNode();
		Submitter submitter = new Submitter();
		submitter.setSigner(submitterName);
		submitter.setSubmitter(submitterName);
		submitter.setSubmissionAttribution(submitterName);
		
		try {
			ParadataManager paradataManager = new ParadataManager(submitter, node);
			
			List<ISubmission> submissions;
			
			//
			// Show external submissions by default
			//
			if (submitter != null && external != null){
				submissions = paradataManager.getExternalSubmissions(widgetId, IStats.VERB);
			} else {
				submissions = new NormalizingFilter(IStats.VERB).filter(paradataManager.getSubmissions(widgetId));
			}
			
			String separator = "";
			resp.getWriter().write("[");
			for (ISubmission submission: submissions){
				IStats stats = (IStats)submission.getAction();
				resp.getWriter().write(separator);
				resp.getWriter().write("{\"submitter\":\""+submission.getSubmitter().getSubmitter()+"\",");
				resp.getWriter().write("\"downloads\":\""+stats.getDownloads()+"\",");
				resp.getWriter().write("\"likes\":\""+stats.getLikes()+"\",");
				resp.getWriter().write("\"views\":\""+stats.getViews()+"\",");
				resp.getWriter().write("\"embeds\":\""+stats.getDownloads()+"\"}");
				separator = ",";
			}
			resp.getWriter().write("]");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(500);
		}
		
		resp.getWriter().flush();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
