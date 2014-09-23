/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 9th 2014
 * 
 * 
 *       This file is part of Composition.
 *
 *       Composition is free software: you can redistribute it and/or modify it
 *       under the terms of the GNU General Public License as published by the
 *       Free Software Foundation, either version 3 of the License, or (at your
 *       option) any later version.
 * 
 *       Composition is distributed in the hope that it will be useful, but
 *       WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *       General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License along
 *       with Composition. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package es.uma.lcc.composition.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import es.uma.lcc.composition.parameterized_dsls.ParameterizedDSL;

public class FileManager {
	
	public static String METAMODEL_EXTENSION = "ecore",
						 GCS_EXTENSION = "gcs",
						 BEHAVIOR_EXTENSION = "behavior",
						 MAUDE_MODEL_TMP = "maude_model.xmi",
						 MAUDE_RED_MODEL_TMP = "maude_red.xmi",
						 CLASSES_LIST_MODEL = "classesList.xmi";

	private static FileManager self;
	
	private IFile DSLMM,
								DSLGCS,
			          DSLBehavior;
	
	private List<ParameterizedDSL> listParams;

	private IFolder tmp;

	private FileManager() {
		setListParams(new ArrayList<ParameterizedDSL>());
	}

	public static FileManager getDefault() {
		if (self == null) {
			self = new FileManager();
		}
		return self;
	}

	public IFile getDSLMM() {
		return DSLMM;
	}

	public void setDSLMM(IFile actualMM) {
		this.DSLMM = actualMM;
	}
	
	public IFile getDSLGCS() {
		return DSLGCS;
	}

	public void setDSLGCS(IFile dSLGCS) {
		DSLGCS = dSLGCS;
	}

	public IFile getDSLBehavior() {
		return DSLBehavior;
	}

	public void setDSLBehavior(IFile parameterizedMM) {
		this.DSLBehavior = parameterizedMM;
	}
	
	public List<ParameterizedDSL> getListParams() {
	  return listParams;
  }

	public void setListParams(List<ParameterizedDSL> listParams) {
	  this.listParams = listParams;
  }

	public IFolder getTmpFolder() throws CoreException{
		String project = this.getDSLMM().getProject().getName();
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
		tmp = currentProject.getFolder(".tmp");
		if (!tmp.exists()) {
			tmp.create(true, true, null);
		}
		return tmp;
	}
	
	public static String readFile(URL file) throws IOException {
		InputStream inp = file.openStream();
		String fileContents = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();
		return fileContents;
	}
}
