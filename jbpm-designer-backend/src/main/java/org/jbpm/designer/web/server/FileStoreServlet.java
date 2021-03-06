/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.designer.web.server;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * 
 * Used to store sources to local file system.
 * 
 * @author Tihomir Surdilovic
 */
public class FileStoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
    	String fname = req.getParameter("fname");
    	String fext = req.getParameter("fext");
    	String data = req.getParameter("data");
        String dataEncoded = req.getParameter("data_encoded");

        String retData;
        if(dataEncoded != null && dataEncoded.length() > 0) {
            retData = new String(Base64.decodeBase64(dataEncoded));
        } else {
            retData = data;
        }

    	if(fext != null && (fext.equals("bpmn2") || fext.equals("svg"))) {
    		try {
				resp.setContentType("application/xml; charset=UTF-8");
				resp.setHeader("Content-Disposition",
				        "attachment; filename=\"" + fname + "." + fext + "\"");
				resp.getWriter().write(retData);
			} catch (Exception e) {
				resp.sendError(500, e.getMessage());
			}
    	}
    }
}
