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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
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
import org.osgi.framework.Bundle;

import es.uma.lcc.mmatching.handlers.FileManager;
import es.uma.lcc.mmatching.maude.exceptions.MaudeErrorException;
import es.uma.lcc.mmatching.maude.exceptions.MaudePrefsException;
import es.uma.lcc.mmatching.resources.Resources;

public class GetMatchings {

	public GetMatchings() {
	}
	
	public void createMaudeProcess() {
//		MaudeProcess mp = new MaudeProcess();
//		try {
//	    mp.initMaudeProcess();
//	    System.out.println("Maude initialized.");
//    } catch (MaudePrefsException | MaudeErrorException | NullPointerException e) {
//	    System.out.println("[error] error initializing Maude process. Maybe it has not been configured.\n"+e.getMessage());
//    }
	}

	private String generateActualMMMaude() {
		/* Generate Maude module with 'Actual parameter Metamodel' */
		FileManager _fm = FileManager.getDefault();
		IFolder tmpFolder = null;
		try {
			tmpFolder = _fm.getTmpFolder();
		} catch (CoreException e) {
			System.out.println("[error] Error creating tmp folder.");
		}
		/* Create file if it does not exist */
		IFile tmpFile = tmpFolder.getFile(FileManager.MAUDE_MODEL_TMP);

		/* Generate Maude model (XMI) */
		try {
			this.generateMaudeModel(_fm.getActualMM().getFullPath().toOSString(), tmpFile.getFullPath().toOSString());
		} catch (ATLCoreException | IOException e) {
			System.out.println("[error] Error creating Maude model.");
		}

		/* Generate Maude code (String) */
		String outputModule = null;
		try {
			outputModule = this.generateMaudeCode(_fm.getTmpFolder().getFullPath().toOSString());
		} catch (CoreException e) {
			System.out.println("[error] Error creating Maude module (code).");
		}
		
		return outputModule;
	}

	/**
	 * Creates a Maude module with the term generated from the actual parameter.
	 * 
	 * @return TODO
	 * @throws ATLCoreException
	 * @throws IOException
	 */
	public void generateMaudeModel(String inModelPath, String outModelPath) throws ATLCoreException, IOException {
		/* Create infrastructure */
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();

		/* Loading Ecore MM */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");

		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Resources.class.getResource("Maude.ecore").openStream(), new HashMap<String, Object>());

		/* Loading Ecore Model */
		IModel ecoreModel = mF.newModel(ecoreMM);
		injector.inject(ecoreModel, inModelPath);

		/* Creating Maude model */
		IModel maudeModel = mF.newModel(maudeMM);

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(ecoreModel, "IN", "Ecore");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();

		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
		    Resources.class.getResourceAsStream("Ecore2Maude.asm"));

		/* Extract resulting model */
		extractor.extract(maudeModel, outModelPath);
	}

	public String generateMaudeCode(String inModelPath) {

		/* Call a Maude code serializer */
		MaudeM2T trans = new MaudeM2T();
		return trans.generate(inModelPath);
	}

	public static void main(String[] args) {
		GetMatchings gm = new GetMatchings();
//		try {
//			gm.generateMaudeModel(TestResources.class.getResource("DEVSMM.ecore").toExternalForm(),
//			    "file:/Users/amoreno/Documents/mmatching/es.uma.lcc.mmatching/src/es/uma/lcc/mmatching/test/outTest.xmi");
//			System.out
//			    .println(gm
//			        .generateMaudeCode("file:/Users/amoreno/Documents/mmatching/es.uma.lcc.mmatching/src/es/uma/lcc/mmatching/test/outTest.xmi"));
//		} catch (ATLCoreException | IOException e) {
//			e.printStackTrace();
//		}
//		gm.createMaudeProcess();
	}

}
