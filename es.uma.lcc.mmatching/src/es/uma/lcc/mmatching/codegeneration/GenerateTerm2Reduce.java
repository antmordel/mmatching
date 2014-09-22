/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 12th 2014
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
package es.uma.lcc.mmatching.codegeneration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import es.uma.lcc.mmatching.handlers.FileManager;
import es.uma.lcc.mmatching.resources.Resources;
import es.uma.lcc.mmatching.wizard.GettingMatchingsWizardPage;

public class GenerateTerm2Reduce {

	public GenerateTerm2Reduce() {
	}
	
	public String generateTerm2ReduceMaude(List<String> classes, String moduleName) {
		/* Generate Maude model with the term to reduce */
		FileManager _fm = FileManager.getDefault();
		IFolder tmpFolder = null;
		try {
			tmpFolder = _fm.getTmpFolder();
		} catch (CoreException e) {
			GettingMatchingsWizardPage.printToconsole("[error] Error creating tmp folder.");
		}
		/* Create file if it does not exist */
		IFile tmpFile = tmpFolder.getFile(FileManager.MAUDE_RED_MODEL_TMP);
		
		/* Create model with classes selected */
		IFile classesModel = tmpFolder.getFile(FileManager.CLASSES_LIST_MODEL);
		String classesContent = createClassesModel(classes);
		if (!classesModel.exists()) {
	        try {
	            classesModel.create(new ByteArrayInputStream(classesContent.getBytes()), true, null);
            } catch (CoreException e) {
            	GettingMatchingsWizardPage.printToconsole("[error] Error creating classes model.");
            }
        } else {
        	try {
	            classesModel.setContents(new ByteArrayInputStream(classesContent.getBytes()), true, false, null);
            } catch (CoreException e) {
            	GettingMatchingsWizardPage.printToconsole("[error] Error creating classes model.");
            }
        }

		/* Generate Maude model (XMI) */
		try {
			this.generateMaudeModel(_fm.getActualMM().getFullPath().toOSString(), 
					classesModel.getFullPath().toOSString(),
					tmpFile.getFullPath().toOSString());
		} catch (ATLCoreException | IOException e) {
			GettingMatchingsWizardPage.printToconsole("[error] Error creating Maude model.");
		}

		/* Generate Maude code (String) */
		String outputModule = null;
		outputModule = this.generateMaudeCode(tmpFile.getFullPath().toOSString(), moduleName);
		
		return outputModule;
	}

	private String createClassesModel(List<String> classes) {
		StringBuilder res = new StringBuilder();
		res.append("<?xml version=\"1.0\" encoding=\"ASCII\"?>\n").append("<classeslist:ClassesList\n")
		        .append("\t\txmi:version=\"2.0\"\n").append("\t\txmlns:xmi=\"http://www.omg.org/XMI\"")
		        .append("\t\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
		        .append("\t\txmlns:classeslist=\"http://classeslist/1.0\"\n")
		        .append("\t\txsi:schemaLocation=\"http://classeslist/1.0 ClassesList.ecore\">)\n");
		for(String s : classes) {
			res.append("\t<list>").append(s).append("</list>");
		}
		res.append("</classeslist:ClassesList>");
		return res.toString();
	}

	/**
	 * Creates a Maude module with the term generated from the actual parameter.
	 * 
	 * @throws ATLCoreException
	 * @throws IOException
	 */
	private void generateMaudeModel(String inModelPath, String inClassesPath, String outModelPath) throws ATLCoreException, IOException {
		
		/* Create infrastructure */
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();

		/* Loading Ecore MM */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		
		/* Loading ClassesList MM */
		IReferenceModel classesList = mF.newReferenceModel();
		injector.inject(classesList, Resources.class.getResource("ClassesList.ecore").openStream(),
				new HashMap<String, Object>());

		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Resources.class.getResource("Maude.ecore").openStream(), new HashMap<String, Object>());

		/* Loading Ecore model */
		IModel ecoreModel = mF.newModel(ecoreMM);
		injector.inject(ecoreModel, inModelPath);
		
		/* Loading Classes model */
		IModel classesModel = mF.newModel(classesList);
		injector.inject(classesModel, inClassesPath);

		/* Creating Maude model */
		IModel maudeModel = mF.newModel(maudeMM);

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(ecoreModel, "IN", "Ecore");
		transformationLauncher.addInModel(classesModel, "INLIST", "ClassesList");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();

		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
		    Resources.class.getResourceAsStream("CreateTermWithVars.asm"));

		/* Extract resulting model */
		extractor.extract(maudeModel, outModelPath);
	}

	private String generateMaudeCode(String inModelPath, String moduleName) {
		/* Call a Maude code serializer */
		MaudeM2TVariables trans = new MaudeM2TVariables();
		return trans.generate(inModelPath, moduleName);
	}
}
