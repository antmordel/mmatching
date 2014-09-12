package es.uma.lcc.mmatching.codegeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import es.uma.lcc.mmatching.resources.Resources;
import es.uma.lcc.mmatching.test.TestResources;
import es.uma.lcc.mmatching.wizard.FileManager;

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
public class GetMatchings {

	public GetMatchings() {
	}

	public void generateMatchings() {
		/* Generate Maude module with 'Actual parameter Metamodel' */

	}

	/**
	 * Creates a Maude module with the term generated from the actual parameter.
	 * 
	 * @return TODO
	 * @throws ATLCoreException
	 * @throws IOException
	 */
	private void generateMaudeModel(String inModelPath, String outModelPath) throws ATLCoreException, IOException {
		/* Create infrastructure */
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();

		/* Loading Ecore MM */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		System.out.println("EcoreMM loaded");

		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Resources.class.getResource("Maude.ecore").openStream(), new HashMap<String, Object>());

		/* Loading Ecore Model */
		IModel ecoreModel = mF.newModel(ecoreMM);
		injector.inject(ecoreModel, inModelPath);
		System.out.println("Ecore Model loaded.");
		
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

	private void generateMaudeCode(String inModelPath, String outCodePath) {
		
	}
	
	public static void main(String[] args) {
		GetMatchings gm = new GetMatchings();
		try {
			gm.generateMaudeModel(TestResources.class.getResource("DEVSMM.ecore").toExternalForm(),
					"file:/home/amoreno/Documents/mmatching/es.uma.lcc.mmatching/bin/es/uma/lcc/mmatching/test/outTest.xmi");
		} catch (ATLCoreException | IOException e) {
			e.printStackTrace();
		}
	}

}
