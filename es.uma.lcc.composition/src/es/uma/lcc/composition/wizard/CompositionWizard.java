/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 23th 2014
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

package es.uma.lcc.composition.wizard;

import org.eclipse.jface.wizard.Wizard;


public class CompositionWizard extends Wizard {
	
	protected SelectMMWizardPage firstPage;
	
	public CompositionWizard() {
	  super();
    setNeedsProgressMonitor(true);
  }
	
	@Override
  public String getWindowTitle() {
    return "Composition";
  }

	
	@Override
  public void addPages() {
    firstPage = new SelectMMWizardPage("Domain Specific Language to compose");
    
    addPage(firstPage);
  }


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canFinish() {
		boolean res = false;
	  return res;
	}
	
}
