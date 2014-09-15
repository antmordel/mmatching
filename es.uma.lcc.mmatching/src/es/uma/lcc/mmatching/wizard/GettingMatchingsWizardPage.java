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

import java.io.IOException;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import es.uma.lcc.ama.maudedaemon.parser.ParseException;
import es.uma.lcc.mmatching.codegeneration.GenerateActualMMMaudeModule;
import es.uma.lcc.mmatching.maude.MaudeProcess;
import es.uma.lcc.mmatching.maude.exceptions.JobFailedException;
import es.uma.lcc.mmatching.maude.exceptions.MaudeErrorException;
import es.uma.lcc.mmatching.maude.exceptions.MaudePrefsException;

public class GettingMatchingsWizardPage extends WizardPage {

	private Composite container;
	
	private Label[] emptyLabels;
	private int emptyLabelsCounter;
	
	private List classesList;
	private java.util.List<String> classes;
	
	private static Document document;
	private static TextViewer viewer;

	protected GettingMatchingsWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		
		emptyLabels = new Label[50];
		emptyLabelsCounter = 0;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 1;
		container.setLayout(containerLayout);
		
		createButton();
		createConsole();
		
		setControl(container);
	}
	
	private void createButton() {
		Button buttonSearch = new Button(container, SWT.PUSH);
		buttonSearch.setText("Start");
		
		buttonSearch.addListener(SWT.Selection, new Listener() {

			@Override
            public void handleEvent(Event event) {
				clearConsole();
				
				GenerateActualMMMaudeModule _gm = new GenerateActualMMMaudeModule();
				String maudeModule = _gm.generateActualMMMaude();
				
				printToconsole("- Maude code generated.\n");
				
				MaudeProcess mp = new MaudeProcess();
				try {
					mp.initMaudeProcess();
					printToconsole("- Maude process initialized.\n");
				} catch (MaudePrefsException | MaudeErrorException | NullPointerException e) {
					printToconsole("[error] error initializing Maude process. Maybe it has not been configured.\n"
					        + e.getMessage());
				}
				
				try {
	                mp.loadInfrastructure();
	                printToconsole("- MMatching infrastructure loaded.\n");
                } catch (JobFailedException | MaudeErrorException | MaudePrefsException | ParseException | IOException e) {
                	printToconsole("[error] error loading MMatching infrastructure.\n"
					        + e.getMessage());
                }
				
				try {
	                mp.load(maudeModule);
	                printToconsole("- ActualMM loaded.\n");
                } catch (JobFailedException | MaudeErrorException | MaudePrefsException | ParseException | IOException e) {
                	printToconsole("[error] error loading Actual Metamodel Maude module.\n"
					        + e.getMessage());
                }
            }
		});
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
	
	public static void printToconsole(String output) {
		if(document != null && viewer != null) {
			document.set(document.get() + output);
			viewer.setTopIndex(document.getNumberOfLines());
		}
	}
	
	private void clearConsole() {
		if (document != null && viewer != null) {
			document.set("");
			viewer.setTopIndex(document.getNumberOfLines());
		}
	}

	private void createEmptyLabel() {
		emptyLabels[emptyLabelsCounter] = new Label(container, SWT.HORIZONTAL);
		emptyLabelsCounter++;
  }
	
	
}
