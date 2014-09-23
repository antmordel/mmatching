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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import es.uma.lcc.composition.handlers.FileManager;
import es.uma.lcc.composition.parameterized_dsls.ParameterizedDSL;

public class SelectBindingsWizardPage extends WizardPage {

	private static final String FILE_EXTENSION_ERROR_ECORE = "Please select Ecore files (*.ecore)",
					FILE_EXTENSION_ERROR_GCS = "Please select GCS files (*.gcs)",
					FILE_EXTENSION_ERROR_BEH = "Please select behavior files (*.behavior)";
	
	private Composite container;
	
	private Label[] emptyLabels;
	private int emptyLabelsCounter;
	
	private org.eclipse.swt.widgets.List bindingsList;
	
	private ParameterizedDSL currentDSL;
	private Button addButton;

	protected SelectBindingsWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Select as many parametererized DSLs as you want to compose.");
		
		emptyLabels = new Label[50];
		emptyLabelsCounter = 0;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout containerLayout = new GridLayout();
		containerLayout.numColumns = 4;
		container.setLayout(containerLayout);
		
		checkIfFinished();
		
		/* Create list of parameterized DSLs */
		parameterizedDSLsList(parent);
		
		/* New label indicating the creation of another binding */
		
		createLabel("Add new parameterized DSL:", 1);
		createEmptyLabel(); createEmptyLabel(); createEmptyLabel();
		
		/*
		 * DSL params
		 */
		
		currentDSL = new ParameterizedDSL();
		
		/* alias */
		createAlias(parent);
		
		/* Select DSL MM */
		selectMM(parent);
		
		/* Select DSL GCS */
		selectGCS(parent);

		/* Select Behavior */
		selectBehavior(parent);
		
		/* Select bindings */
		selectBindings(parent);
		
		/* Add Button */
    addButton(parent);
		
		
		setControl(container);
	}

	private void checkIfFinished() {
		if (FileManager.getDefault().getListParams().size() > 0) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}
	
	private void parameterizedDSLsList(Composite parent) {
	  createLabel("Parameterized DSLs \n to be composed:", 1);
	  
	  bindingsList = createClassesList();
	  fillList();
	  createEmptyLabel(); createEmptyLabel();
  }
	
	private List createClassesList() {
		final List bindingsList = new List(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		
		GridData gridData = new GridData();
    gridData.widthHint = 200;
    gridData.heightHint = 150;
    bindingsList.setLayoutData(gridData);
    
    bindingsList.addListener(SWT.Selection, new Listener() {
    	@Override
      public void handleEvent(Event event) {
    		// TODO	      
      }
    });
	  
	  return bindingsList;
  }
	
	public void fillList() {
		if (bindingsList != null) {
			bindingsList.removeAll();
//			classes = new ArrayList<String>();
//			for (String _class : FileManager.getDefault().readParameterClasses()) {
//				classesList.add(_class);
//				classes.add(_class);
//			}
		}
	}
	
  private void createAlias(final Composite parent) {
    createLabel("Alias:", 1);
    
    final Text textAlias = new Text(container, SWT.SINGLE);
		textAlias.setEditable(true);
		textAlias.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));
		
		GridData _gridtextAlias = new GridData();
		_gridtextAlias.horizontalAlignment = GridData.END;
		_gridtextAlias.horizontalSpan = 2;
		_gridtextAlias.widthHint = 350;
		textAlias.setLayoutData(_gridtextAlias);
		
		if (currentDSL.getAlias() != null) {
			textAlias.setText(currentDSL.getAlias());
		} else {
			textAlias.setText("");
		}
		
		textAlias.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(textAlias.getText() != null) {
					currentDSL.setAlias(textAlias.getText());
				}
			}
		});
		
		createEmptyLabel();
  }
	
	private void selectMM(final Composite parent) {
		createLabel("DSL metamodel:", 1);

		final Text textSystemMM = new Text(container, SWT.SINGLE);
		textSystemMM.setEditable(false);
		textSystemMM.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextSystemDSL = new GridData();
		_gridTextSystemDSL.horizontalAlignment = GridData.END;
		_gridTextSystemDSL.horizontalSpan = 2;
		_gridTextSystemDSL.widthHint = 350;
		textSystemMM.setLayoutData(_gridTextSystemDSL);

		if (currentDSL.getMetamodel() != null) {
			textSystemMM.setText(currentDSL.getMetamodel().getFullPath().toOSString());
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
					currentDSL.setMetamodel(result);
					textSystemMM.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.METAMODEL_EXTENSION)){
						setErrorMessage(FILE_EXTENSION_ERROR_ECORE);
					} else if (getErrorMessage().equals(FILE_EXTENSION_ERROR_ECORE)){
						setErrorMessage("");
					}
					
					checkIfFinished();
					addButton.setEnabled(currentDSL.isCompleted());
				}
			}
		});
	}
	
	private void selectGCS(final Composite parent) {
		createLabel("DSL GCS:", 1);

		final Text textDSLGCS = new Text(container, SWT.SINGLE);
		textDSLGCS.setEditable(false);
		textDSLGCS.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextDSLGCS = new GridData();
		_gridTextDSLGCS.horizontalAlignment = GridData.END;
		_gridTextDSLGCS.horizontalSpan = 2;
		_gridTextDSLGCS.widthHint = 350;
		textDSLGCS.setLayoutData(_gridTextDSLGCS);

		if (currentDSL.getGcs() != null) {
			textDSLGCS.setText(currentDSL.getGcs().getFullPath().toOSString());
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
					currentDSL.setGcs(result);
					textDSLGCS.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.GCS_EXTENSION)){
						setErrorMessage(FILE_EXTENSION_ERROR_GCS);
					} else if (getErrorMessage().equals(FILE_EXTENSION_ERROR_GCS)){
						setErrorMessage("");
					}
					
					checkIfFinished();
					addButton.setEnabled(currentDSL.isCompleted());
				}
			}
		});
	}

	private void selectBehavior(final Composite parent) {
		createLabel("DSL Behavior:", 1);

		final Text textBehavior = new Text(container, SWT.SINGLE);
		textBehavior.setEditable(false);
		textBehavior.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextBehavior = new GridData();
		_gridTextBehavior.horizontalAlignment = GridData.END;
		_gridTextBehavior.horizontalSpan = 2;
		_gridTextBehavior.widthHint = 350;
		textBehavior.setLayoutData(_gridTextBehavior);

		if (currentDSL.getBehavior() != null) {
			textBehavior.setText(currentDSL.getBehavior().getFullPath().toOSString());
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
					currentDSL.setBehavior(result);
					textBehavior.setText(result.getFullPath().toOSString());
					
					if(!result.getFileExtension().equals(FileManager.BEHAVIOR_EXTENSION)){
						setErrorMessage(FILE_EXTENSION_ERROR_BEH);
					} else if (getErrorMessage().equals(FILE_EXTENSION_ERROR_BEH)){
						setErrorMessage("");
					}
					
					checkIfFinished();
					addButton.setEnabled(currentDSL.isCompleted());
				}
			}
		});
	}
	
	private void selectBindings(final Composite parent) {
		createLabel("Bindings model:", 1);

		final Text textBind = new Text(container, SWT.SINGLE);
		textBind.setEditable(false);
		textBind.setBackground(new Color(parent.getDisplay(), new RGB(255, 255, 255)));

		GridData _gridTextBind = new GridData();
		_gridTextBind.horizontalAlignment = GridData.END;
		_gridTextBind.horizontalSpan = 2;
		_gridTextBind.widthHint = 350;
		textBind.setLayoutData(_gridTextBind);

		if (currentDSL.getBindings() != null) {
			textBind.setText(currentDSL.getBindings().getFullPath().toOSString());
		} else {
			textBind.setText("");
		}

		final Button selectParameterizedMMButton = new Button(container, SWT.PUSH);
		selectParameterizedMMButton.setText("Browse");
		selectParameterizedMMButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent.getShell(),
				    new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Bindings");
				dialog.setMessage("Select bindings model file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (result != null) {
					currentDSL.setBindings(result);
					textBind.setText(result.getFullPath().toOSString());
					
					checkIfFinished();
					addButton.setEnabled(currentDSL.isCompleted());
				}
			}
		});
	}
	
	private void addButton(final Composite parent) {
		createEmptyLabel();
		createEmptyLabel();
		createEmptyLabel();
		addButton = new Button(container, SWT.PUSH);
		addButton.setText("Add binding");
		addButton.setEnabled(false);
		addButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				FileManager.getDefault().getListParams().add(currentDSL);
				bindingsList.add(currentDSL.getAlias());
				
				bindingsList.redraw();
				currentDSL = new ParameterizedDSL();
				addButton.setEnabled(false);
			}
		});
  }

	private void createLabel(String labelText, int span) {
		Label label = new Label(container, SWT.HORIZONTAL);
		label.setText(labelText);

		GridData _gridLabel = new GridData();
		_gridLabel.horizontalAlignment = GridData.END;
		_gridLabel.horizontalSpan = span;

		label.setLayoutData(_gridLabel);
	}
	
	@Override
	public IWizardPage getNextPage() {
		
	  return null;
	}
	
	private void createEmptyLabel() {
		emptyLabels[emptyLabelsCounter] = new Label(container, SWT.HORIZONTAL);
		emptyLabelsCounter++;
  }
	
	
}
