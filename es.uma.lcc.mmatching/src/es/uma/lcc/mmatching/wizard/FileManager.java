package es.uma.lcc.mmatching.wizard;

import org.eclipse.core.resources.IFile;

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

}
