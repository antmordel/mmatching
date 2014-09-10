package es.uma.lcc.mmatching.wizard;

import org.eclipse.jface.wizard.Wizard;


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
public class MMatchingWizard extends Wizard {
	
	protected SelectMMWizardPage firstPage;
	protected SetParameterWizardPage secondPage;
	
	public MMatchingWizard() {
	  super();
    setNeedsProgressMonitor(true);
  }
	
	@Override
  public String getWindowTitle() {
    return "MMatching";
  }

	
	@Override
  public void addPages() {
    firstPage = new SelectMMWizardPage("Select metamodels");
    secondPage = new SetParameterWizardPage("Set parameter classes");
    
    addPage(firstPage);
    addPage(secondPage);
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
