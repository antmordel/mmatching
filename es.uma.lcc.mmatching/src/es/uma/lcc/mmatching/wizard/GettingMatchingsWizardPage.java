/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date Sep 15th 2014
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
package es.uma.lcc.mmatching.wizard;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class GettingMatchingsWizardPage extends WizardPage {

	private Composite container;
	
	private Label[] emptyLabels;
	private int emptyLabelsCounter;
	
	private List classesList;
	private java.util.List<String> classes;
	
	private Document document;
	private TextViewer viewer;

	protected GettingMatchingsWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Searching initial matchings...");
		
		emptyLabels = new Label[50];
		emptyLabelsCounter = 0;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 1;
		container.setLayout(containerLayout);
		
		createConsole();
		
		printToconsole("prueba\n");
		printToconsole("prueba2");
		
		setControl(container);
	}
	
	private void createConsole() {
		Label label = new Label(container, SWT.HORIZONTAL);
		label.setText("Console:");

		GridData _gridLabel = new GridData();
		_gridLabel.horizontalAlignment = GridData.BEGINNING;
		_gridLabel.horizontalSpan = 1;

		label.setLayoutData(_gridLabel);
		
		/* Creating console text viewer */		
		viewer = new TextViewer(container, SWT.WRAP | SWT.V_SCROLL);
		GridData viewerData = new GridData(GridData.FILL_BOTH);
		viewerData.horizontalSpan = 2;
	  viewer.getControl().setLayoutData(viewerData);
	  
	  viewer.setEditable(false);
	  document = new Document();
	  viewer.setDocument(document);
	}
	
	private void printToconsole(String output) {
		document.set(document.get()+output);
	  viewer.setTopIndex(document.getNumberOfLines());
	}

	private void createEmptyLabel() {
		emptyLabels[emptyLabelsCounter] = new Label(container, SWT.HORIZONTAL);
		emptyLabelsCounter++;
  }
	
	
}
