package es.uma.lcc.mmatching.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 9th 2014
 * 
 * 
 *       This file is part of MMatching.
 *
 *       MMatching is free software: you can redistribute it and/or modify it
 *       under the terms of the GNU General Public License as published by the
 *       Free Software Foundation, either version 3 of the License, or (at your
 *       option) any later version.
 * 
 *       MMatching is distributed in the hope that it will be useful, but
 *       WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *       General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License along
 *       with MMatching. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
public class FileManager {
	
	public static String METAMODEL_EXTENSION = "ecore";

	private static FileManager self;
	
	private IFile systemMM,
			          parameterizedMM;

	private FileManager() {
	}

	public static FileManager getDefault() {
		if (self == null) {
			self = new FileManager();
		}
		return self;
	}

	public IFile getSystemMM() {
		return systemMM;
	}

	public void setSystemMM(IFile systemMM) {
		this.systemMM = systemMM;
	}

	public IFile getParameterizedMM() {
		return parameterizedMM;
	}

	public void setParameterizedMM(IFile parameterizedMM) {
		this.parameterizedMM = parameterizedMM;
	}
	
	public List<String> readParameterClasses() {
		ArrayList<String> res = new ArrayList<>();
		
		if (parameterizedMM != null) {
			
			ResourceSetImpl resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());

			URI uri = URI.createURI(parameterizedMM.getFullPath().toOSString());
			Resource resource = resourceSet.createResource(uri);
			try {
				resource.load(null);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			for (EObject current : resource.getContents()){ 
				if(current instanceof EPackageImpl) { // we search the package
					for (EObject _class : current.eContents()){ // we loop searching all classes
						if (_class instanceof EClassImpl) {
							res.add(((EClassImpl) _class).getName());
						}
					}
				}
			}
		}
		
		return res;
	}

}
