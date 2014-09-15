package es.uma.lcc.mmatching.wizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import es.uma.lcc.mmatching.handlers.FileManager;

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
public class SetParameterWizardPage extends WizardPage {

	private Composite container;
	
	private Label[] emptyLabels;
	private int emptyLabelsCounter;
	
	private List classesList;
	private java.util.List<String> classes;

	protected SetParameterWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Select those classes that comprise the parameter. Press <ctrl> to select several classes.");
		
		emptyLabels = new Label[50];
		emptyLabelsCounter = 0;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 2;
		container.setLayout(containerLayout);
		
		setLabel();
		
		if (classesList == null) {
			classesList = createClassesList();
		}
		
		fillList();

		setControl(container);
	}
	
	public void fillList() {
		if (classesList != null) {
			classesList.removeAll();
			classes = new ArrayList<String>();
			for (String _class : FileManager.getDefault().readParameterClasses()) {
				classesList.add(_class);
				classes.add(_class);
			}
		}
	}

	private List createClassesList() {
		createEmptyLabel();
		final List classesList = new List(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		
		GridData gridData = new GridData();
    gridData.widthHint = 200;
    gridData.heightHint = 300;
    classesList.setLayoutData(gridData);
    
    classesList.addListener(SWT.Selection, new Listener() {
    	@Override
      public void handleEvent(Event event) {
				int[] selection = classesList.getSelectionIndices();
	      for (int i = 0; i < selection.length; i++) {
	        System.out.println(classes.get(i)+"  ");
        }
	      
	      checkIfFinished();
      }
    });
	  
	  return classesList;
  }
	
	private void checkIfFinished() {
		if(classes.size() > 0) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

	private void setLabel() {
		Label label = new Label(container, SWT.HORIZONTAL);
		label.setText("Parameter classes:");

		GridData _gridLabel = new GridData();
		_gridLabel.horizontalAlignment = GridData.END;
		_gridLabel.horizontalSpan = 1;

		label.setLayoutData(_gridLabel);
		
		createEmptyLabel();
  }
	
	private void createEmptyLabel() {
		emptyLabels[emptyLabelsCounter] = new Label(container, SWT.HORIZONTAL);
		emptyLabelsCounter++;
  }
	
	
}
