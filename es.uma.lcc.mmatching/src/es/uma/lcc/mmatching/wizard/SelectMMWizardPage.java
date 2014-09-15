package es.uma.lcc.mmatching.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

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
public class SelectMMWizardPage extends WizardPage {

	private static final String FILE_EXTENSION_ERROR = "Please select Ecore metamodels.";
	
	private Composite container;

	protected SelectMMWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 4;
		container.setLayout(containerLayout);
		
		checkIfFinished();
		
		/* Select System DSL MM */
		selectSystemMM(parent);

		/* Select Parameterized DSL MM */
		selectParameterizedMM(parent);

		setControl(container);
	}
	
	private void checkIfFinished() {
		if (FileManager.getDefault().getActualMM() != null && FileManager.getDefault().getParameterizedMM() != null) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}
	
	private void selectSystemMM(final Composite parent) {
		createLabel("Actual parameter DSL metamodel:");

		final Text textSystemMM = new Text(container, SWT.SINGLE);
		textSystemMM.setEditable(false);
		textSystemMM.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextSystemDSL = new GridData();
		_gridTextSystemDSL.horizontalAlignment = GridData.END;
		_gridTextSystemDSL.horizontalSpan = 2;
		_gridTextSystemDSL.widthHint = 350;
		textSystemMM.setLayoutData(_gridTextSystemDSL);

		if (FileManager.getDefault().getActualMM() != null) {
			textSystemMM.setText(FileManager.getDefault().getActualMM().getFullPath().toOSString());
		} else {
			textSystemMM.setText("");
		}

		final Button selectSystemMMButton = new Button(container, SWT.PUSH);
		selectSystemMMButton.setText("Browse");
		selectSystemMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Actual Parameter Metamodel");
				dialog.setMessage("Select Metamodel file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					FileManager.getDefault().setActualMM(result);
					textSystemMM.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.METAMODEL_EXTENSION)){
						System.out.println(result.getFileExtension());
						setErrorMessage(FILE_EXTENSION_ERROR);
					}
					
					checkIfFinished();
				}
			}
		});
	}

	private void selectParameterizedMM(final Composite parent) {
		createLabel("Parameterized DSL metamodel:");

		final Text textParameterizedMM = new Text(container, SWT.SINGLE);
		textParameterizedMM.setEditable(false);
		textParameterizedMM.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextParameterizedDSL = new GridData();
		_gridTextParameterizedDSL.horizontalAlignment = GridData.END;
		_gridTextParameterizedDSL.horizontalSpan = 2;
		_gridTextParameterizedDSL.widthHint = 350;
		textParameterizedMM.setLayoutData(_gridTextParameterizedDSL);

		if (FileManager.getDefault().getParameterizedMM() != null) {
			textParameterizedMM.setText(FileManager.getDefault().getParameterizedMM().getFullPath().toOSString());
		} else {
			textParameterizedMM.setText("");
		}

		final Button selectParameterizedMMButton = new Button(container, SWT.PUSH);
		selectParameterizedMMButton.setText("Browse");
		selectParameterizedMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("System Metamodel");
				dialog.setMessage("Select Metamodel file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					FileManager.getDefault().setParameterizedMM(result);
					textParameterizedMM.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.METAMODEL_EXTENSION)){
						setErrorMessage(FILE_EXTENSION_ERROR);
					}
					
					checkIfFinished();
				}
			}
		});
	}

	private void createLabel(String labelText) {
		Label label = new Label(container, SWT.HORIZONTAL);
		label.setText(labelText);

		GridData _gridLabel = new GridData();
		_gridLabel.horizontalAlignment = GridData.END;
		_gridLabel.horizontalSpan = 1;

		label.setLayoutData(_gridLabel);
	}
	
	@Override
	public IWizardPage getNextPage() {
		SetParameterWizardPage nextPage = (SetParameterWizardPage) super.getNextPage();
		nextPage.fillList();
		
	  return nextPage;
	}
	
	
	
	
}
