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
package es.uma.lcc.mmatching.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.m2m.atl.core.ATLCoreException;
import org.junit.Before;
import org.junit.Test;

import es.uma.lcc.mmatching.codegeneration.GenerateActualMMMaudeModule;

public class GetMatchingsTest extends GenerateActualMMMaudeModule {

	private GenerateActualMMMaudeModule gm;
	private static String OUTPUT_TEST_FILE =
			"file:/Users/amoreno/Documents/mmatching/es.uma.lcc.mmatching/src/es/uma/lcc/mmatching/test/outTest.xmi";

	@Before
	public void setUp() throws Exception {
		gm = new GenerateActualMMMaudeModule();
	}

	@Test
	public void testGenerateMaudeModel() {
		try {
	        gm.generateMaudeModel(TestResources.class.getResource("DEVSMM.ecore").toExternalForm(),
	        		OUTPUT_TEST_FILE);
        } catch (ATLCoreException | IOException e) {
	        fail(e.getMessage());
        }
	}
	
	@Test
	public void testGenerateMaudeCode() {
		String s = gm.generateMaudeCode(OUTPUT_TEST_FILE);
		assertTrue(s.endsWith("endm ---- system module DEVSMM\n\n"));
	}

}
