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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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

import es.uma.lcc.composition.handlers.FileManager;

public class SelectDSLWizardPage extends WizardPage {

	private static final String FILE_EXTENSION_ERROR_ECORE = "Please select Ecore files (*.ecore)",
			FILE_EXTENSION_ERROR_GCS = "Please select GCS files (*.gcs)",
					FILE_EXTENSION_ERROR_BEH = "Please select behavior files (*.behavior)";
	
	private Composite container;

	protected SelectDSLWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Please select the DSL to be composed");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 4;
		container.setLayout(containerLayout);
		
		checkIfFinished();
		
		/* Select DSL MM */
		selectMM(parent);
		
		/* Select DSL GCS */
		selectGCS(parent);

		/* Select Behavior */
		selectBehavior(parent);

		setControl(container);
	}
	
	private void checkIfFinished() {
		if (FileManager.getDefault().getDSLMM() != null && FileManager.getDefault().getDSLBehavior() != null &&
				FileManager.getDefault().getDSLGCS() != null) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}
	
	private void selectMM(final Composite parent) {
		createLabel("DSL metamodel:");

		final Text textSystemMM = new Text(container, SWT.SINGLE);
		textSystemMM.setEditable(false);
		textSystemMM.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextSystemDSL = new GridData();
		_gridTextSystemDSL.horizontalAlignment = GridData.END;
		_gridTextSystemDSL.horizontalSpan = 2;
		_gridTextSystemDSL.widthHint = 350;
		textSystemMM.setLayoutData(_gridTextSystemDSL);

		if (FileManager.getDefault().getDSLMM() != null) {
			textSystemMM.setText(FileManager.getDefault().getDSLMM().getFullPath().toOSString());
		} else {
			textSystemMM.setText("");
		}

		final Button selectSystemMMButton = new Button(container, SWT.PUSH);
		selectSystemMMButton.setText("Browse");
		selectSystemMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Metamodel");
				dialog.setMessage("Select Metamodel file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					FileManager.getDefault().setDSLMM(result);
					textSystemMM.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.METAMODEL_EXTENSION)){
						System.out.println(result.getFileExtension());
						setErrorMessage(FILE_EXTENSION_ERROR_ECORE);
					}
					
					checkIfFinished();
				}
			}
		});
	}
	
	private void selectGCS(final Composite parent) {
		createLabel("DSL GCS:");

		final Text textDSLGCS = new Text(container, SWT.SINGLE);
		textDSLGCS.setEditable(false);
		textDSLGCS.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextDSLGCS = new GridData();
		_gridTextDSLGCS.horizontalAlignment = GridData.END;
		_gridTextDSLGCS.horizontalSpan = 2;
		_gridTextDSLGCS.widthHint = 350;
		textDSLGCS.setLayoutData(_gridTextDSLGCS);

		if (FileManager.getDefault().getDSLMM() != null) {
			textDSLGCS.setText(FileManager.getDefault().getDSLGCS().getFullPath().toOSString());
		} else {
			textDSLGCS.setText("");
		}

		final Button selectSystemMMButton = new Button(container, SWT.PUSH);
		selectSystemMMButton.setText("Browse");
		selectSystemMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Graphical Concrete Syntax");
				dialog.setMessage("Select GCS file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					FileManager.getDefault().setDSLGCS(result);
					textDSLGCS.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.GCS_EXTENSION)){
						System.out.println(result.getFileExtension());
						setErrorMessage(FILE_EXTENSION_ERROR_GCS);
					}
					
					checkIfFinished();
				}
			}
		});
	}

	private void selectBehavior(final Composite parent) {
		createLabel("DSL Behavior:");

		final Text textBehavior = new Text(container, SWT.SINGLE);
		textBehavior.setEditable(false);
		textBehavior.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextBehavior = new GridData();
		_gridTextBehavior.horizontalAlignment = GridData.END;
		_gridTextBehavior.horizontalSpan = 2;
		_gridTextBehavior.widthHint = 350;
		textBehavior.setLayoutData(_gridTextBehavior);

		if (FileManager.getDefault().getDSLBehavior() != null) {
			textBehavior.setText(FileManager.getDefault().getDSLBehavior().getFullPath().toOSString());
		} else {
			textBehavior.setText("");
		}

		final Button selectParameterizedMMButton = new Button(container, SWT.PUSH);
		selectParameterizedMMButton.setText("Browse");
		selectParameterizedMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Behavior");
				dialog.setMessage("Select behavior file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					FileManager.getDefault().setDSLBehavior(result);
					textBehavior.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.BEHAVIOR_EXTENSION)){
						setErrorMessage(FILE_EXTENSION_ERROR_BEH);
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
	
}
