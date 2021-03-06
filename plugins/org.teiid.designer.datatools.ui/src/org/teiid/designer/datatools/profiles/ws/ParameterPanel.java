/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.designer.datatools.profiles.ws;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.teiid.core.designer.util.CoreArgCheck;
import org.teiid.core.designer.util.I18nUtil;
import org.teiid.core.designer.util.StringConstants;
import org.teiid.core.designer.util.StringUtilities;
import org.teiid.datatools.connectivity.model.Parameter;
import org.teiid.designer.datatools.ui.DatatoolsUiConstants;
import org.teiid.designer.datatools.ui.DatatoolsUiPlugin;
import org.teiid.designer.ui.common.table.TableViewerBuilder;
import org.teiid.designer.ui.common.util.WidgetFactory;

/**
 * @since 8.6
 */
public class ParameterPanel implements DatatoolsUiConstants {
	static final String PREFIX = I18nUtil.getPropertyPrefix(ParameterPanel.class);
	
    TableViewerBuilder propertiesViewer;
	Button addPropertyButton;
	Button removePropertyButton;
	private Map parameterMap;
	private int visibleTableRows;
	private WSProfileDetailsWizardPage wsProfileDetailsWizardPage;
	private PropertyPage propertyPage;
	
	/**
	 * Constructor
	 * @param wsProfileDetailsWizardPage 
     * @param parent the parent Composite
	 * @param parameterMap 
     * @param propertiesManager the TeiidpropertiesManager
     * @param visibleTableRows the number of visible rows to be shown in the table
     */
    public ParameterPanel(WSProfileDetailsWizardPage wsProfileDetailsWizardPage, Composite parent, Map<String, Parameter> parameterMap, int visibleTableRows) {
    	super();
    	this.parameterMap = parameterMap;
    	this.visibleTableRows = visibleTableRows;
    	this.wsProfileDetailsWizardPage = wsProfileDetailsWizardPage;
    	createPanel(parent);
    }
    /**
	 * Constructor
     * @param propertyPage 
     * @param parent the parent Composite
     * @param parameterMap 
     * @param propertiesManager the TeiidpropertiesManager
     * @param visibleTableRows the number of visible rows to be shown in the table
     */
    public ParameterPanel(PropertyPage propertyPage, Composite parent, Map<String, Parameter> parameterMap, int visibleTableRows) {
    	super();
    	this.parameterMap = parameterMap;
    	this.visibleTableRows = visibleTableRows;
    	this.propertyPage = propertyPage;
    	createPanel(parent);
    }
    
    /*
     * create the panel
     * @param parent the parent composite
     */
	private void createPanel(Composite parent) {
		
    	Composite panel = WidgetFactory.createGroup(parent, StringConstants.EMPTY_STRING, SWT.FILL, 2, 1);  //$NON-NLS-1$
    	GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
    	gd.horizontalSpan = 2;
    	panel.setLayoutData(gd);

        this.propertiesViewer = new TableViewerBuilder(panel, (SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.BORDER));
        GridDataFactory.fillDefaults().grab(true, true).span(3, 1).hint(360,  160).applyTo(propertiesViewer.getTableComposite());

        ColumnViewerToolTipSupport.enableFor(this.propertiesViewer.getTableViewer());
        this.propertiesViewer.setContentProvider(new IStructuredContentProvider() {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( Object inputElement ) {
            	Map parameters = parameterMap;

                if (parameters == null || parameters.isEmpty()) {
                    return new Object[0];
                }
          
                return parameters.values().toArray();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( Viewer viewer,
                                      Object oldInput,
                                      Object newInput ) {
                // nothing to do
            }
        });

        // sort the table rows by display name
        this.propertiesViewer.setComparator(new ViewerComparator() {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public int compare( Viewer viewer,
                                Object e1,
                                Object e2 ) {

                return 0;
            }
        });

        Table table = this.propertiesViewer.getTable();
        ((GridData)table.getLayoutData()).heightHint = table.getItemHeight() * this.visibleTableRows;

        // create columns
        TableViewerColumn column = propertiesViewer.createColumn(SWT.LEFT, 30, 40, true);
        column.getColumn().setText(UTIL.getString("ParametersPanel_name") + "                   ");  //$NON-NLS-1$ //$NON-NLS-2$
        column.setLabelProvider(new PropertyLabelProvider(0));
        //column.setEditingSupport(new PropertyNameEditingSupport(this.propertiesViewer, 0));

        column = propertiesViewer.createColumn(SWT.LEFT, 30, 40, true);
        column.getColumn().setText(UTIL.getString("ParametersPanel_type"));  //$NON-NLS-1$
        column.getColumn().setToolTipText(UTIL.getString("AddParameterDialog_txtType_toolTip"));
        column.setLabelProvider(new PropertyLabelProvider(1));
        column.setEditingSupport(new PropertyNameEditingSupport(this.propertiesViewer.getTableViewer(), 1));
        
        column = propertiesViewer.createColumn(SWT.LEFT, 30, 40, true);
        column.getColumn().setText(UTIL.getString("ParametersPanel_default_value"));  //$NON-NLS-1$
        column.getColumn().setToolTipText(UTIL.getString("AddParameterDialog_txtDefaultValue_toolTip"));
        column.setLabelProvider(new PropertyLabelProvider(2));
        column.setEditingSupport(new PropertyNameEditingSupport(this.propertiesViewer.getTableViewer(), 2));
        
        this.propertiesViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( SelectionChangedEvent event ) {
                handlePropertySelected();
            }
        });

        //
        // add toolbar below the table
        //
        
        Composite toolbarPanel = WidgetFactory.createPanel(panel, SWT.NONE, GridData.VERTICAL_ALIGN_BEGINNING, 1, 2);
        
        this.addPropertyButton = WidgetFactory.createButton(toolbarPanel, GridData.FILL);
        this.addPropertyButton.setImage(DatatoolsUiPlugin.getDefault().getImage(Images.ADD_PROPERTY_ICON)); 
        this.addPropertyButton.setToolTipText(UTIL.getString("ParametersPanel_addNewParameterButton_tooltip")); //$NON-NLS-1$
        this.addPropertyButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddProperty();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        
        this.removePropertyButton = WidgetFactory.createButton(toolbarPanel, GridData.FILL);
        this.removePropertyButton.setImage(DatatoolsUiPlugin.getDefault().getImage(Images.REMOVE_PROPERTY_ICON));
        this.removePropertyButton.setToolTipText(UTIL.getString("ParametersPanel_removeParameterButton_tooltip"));  //$NON-NLS-1$
        this.removePropertyButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRemoveProperty();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        this.removePropertyButton.setEnabled(false);
        
        this.propertiesViewer.setInput(this);
	}
	
	void handlePropertySelected() {
		boolean hasSelection = !this.propertiesViewer.getSelection().isEmpty();
		this.removePropertyButton.setEnabled(hasSelection);
	}
	
    private Parameter getSelectedProperty() {
        IStructuredSelection selection = (IStructuredSelection)this.propertiesViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        return (Parameter)selection.getFirstElement();
    }
	
    void handleAddProperty() {
        assert (!this.propertiesViewer.getSelection().isEmpty());
        if (this.parameterMap == null) this.parameterMap = new LinkedHashMap();
        Set<String> keys = new HashSet<String>();
        for( Object key : parameterMap.keySet() ) {
        	keys.add((String)key);
        }


        AddParameterDialog dialog = new AddParameterDialog(propertiesViewer.getControl().getShell(), keys);

        if (dialog.open() == Window.OK) {
            // update model
            String name = dialog.getName();
            String type = dialog.getType() != null ? dialog.getType() : IWSProfileConstants.QUERY_STRING;
            String defaultValue = dialog.getDefaultValue();
            Parameter parameter = new Parameter(name, defaultValue, Parameter.Type.fromValue(type));

            this.parameterMap.put(name, parameter);
            
            // update UI from model
            this.propertiesViewer.refresh();

            // select the new property
            
            
            Parameter prop = null;
            
            for(TableItem item : this.propertiesViewer.getTable().getItems() ) {
            	if( item.getData() instanceof Parameter && ((Parameter)item.getData()).getName().equals(name) ) {
            		prop = (Parameter)item.getData();
            		break;
            	}
            }

            if( prop != null ) {
                this.propertiesViewer.setSelection(new StructuredSelection(prop), true);
            }
            
            if (this.wsProfileDetailsWizardPage!=null){
            	for( Object key : this.parameterMap.keySet() )  {
            		Parameter para = (Parameter)this.parameterMap.get((String)key);
            		wsProfileDetailsWizardPage.getProfileProperties().put(para.getPropertyKey(), para.getPropertyValue());
            	}
            	wsProfileDetailsWizardPage.setParameterMap(this.parameterMap);
            	wsProfileDetailsWizardPage.urlPreviewText.setText(wsProfileDetailsWizardPage.updateUrlPreview().toString());
            }else{
            	for( Object key : this.parameterMap.keySet() )  {
            		Parameter para = (Parameter)this.parameterMap.get((String)key);
            		propertyPage.getExtraProperties().put(para.getPropertyKey(), para.getPropertyValue());
            	}
            	propertyPage.setParameterMap(this.parameterMap);
            	propertyPage.urlPreviewText.setText(propertyPage.updateUrlPreview().toString());
            }
        }
    }
    
    void handleRemoveProperty() {
    	Parameter selectedProperty = getSelectedProperty();
        assert (selectedProperty != null);

        // update model
        parameterMap.remove(selectedProperty.getName());

        // update UI
        this.propertiesViewer.refresh();
        
        if (this.wsProfileDetailsWizardPage!=null){
        	wsProfileDetailsWizardPage.urlPreviewText.setText(wsProfileDetailsWizardPage.updateUrlPreview().toString());
        }else{
        	propertyPage.urlPreviewText.setText(wsProfileDetailsWizardPage.updateUrlPreview().toString());
        }
    }
	
	class PropertyLabelProvider extends ColumnLabelProvider {

        private final int columnID;

        public PropertyLabelProvider( int columnID ) {
            this.columnID = columnID;
        }

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if( element instanceof Parameter ) {
				if( columnID == 0 ) {
					return ((Parameter)element).getName();
				} else if( columnID == 1 ) {
					return ((Parameter)element).getType().toString();
				} else if( columnID == 2 ) {
					return ((Parameter)element).getDefaultValue();
				}
			}
			return super.getText(element);
		}
	}

    class PropertyNameEditingSupport extends EditingSupport {
    	int columnID;
    	
		private TextCellEditor editor;

		/**
		 * Create a new instance of the receiver.
		 * 
         * @param viewer the viewer where the editing support is being provided (cannot be <code>null</code>)
		 * @param columnID the column id
		 */
		public PropertyNameEditingSupport(ColumnViewer viewer, int columnID) {
			super(viewer);
			this.columnID = columnID;
			this.editor = new TextCellEditor((Composite) viewer.getControl());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
		 */
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
		 */
		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
		 */
		@Override
		protected Object getValue(Object element) {
			if( element instanceof Parameter ) {
				if( columnID == 0 ) {
					return ((Parameter)element).getName();
				} else if( columnID == 1 ) {
					return ((Parameter)element).getType().toString();
				} else if( columnID == 2 ) {
					return ((Parameter)element).getDefaultValue();
				}
			}
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object,
		 *      java.lang.Object)
		 */
		@Override
		protected void setValue(Object element, Object value) {
			if( element instanceof Parameter ) {
				String key = ((Parameter)element).getPropertyKey();
				if( columnID == 1 ) {
					String oldType = ((Parameter)element).getType().toString();
					String newType = (String)value;
					if( newType != null && newType.length() > 0 && !newType.equalsIgnoreCase(oldType)) {
						((Parameter)element).setType(Parameter.Type.fromValue(newType));
						parameterMap.put(key,element);
						propertiesViewer.refresh();
					}
				} else if( columnID == 2 ) {
						String oldDefaultValue = ((Parameter)element).getDefaultValue();
						String newDefaultValue = (String)value;
						if( newDefaultValue != null && newDefaultValue.length() > 0 && !newDefaultValue.equalsIgnoreCase(oldDefaultValue)) {
							((Parameter)element).setDefaultValue(newDefaultValue);
							parameterMap.put(key,element);
							propertiesViewer.refresh();
						}
					}
			}
		}

	}
    class AddParameterDialog  extends MessageDialog {

        private Button btnOk;
        private final Set<String> existingNames;
        private String name;
        private String type;
        private String defaultValue;

        /**
         * @param parentShell the parent shell (may be <code>null</code>)
         * @param existingPropertyNames the existing property names (can be <code>null</code>)
         */
        public AddParameterDialog( Shell parentShell,
                                  Set<String> existingPropertyNames ) {
            super(parentShell, UTIL.getString("AddParameterDialog_title"), null,   //$NON-NLS-1$
            		UTIL.getString("AddParameterDialog_message"), MessageDialog.INFORMATION,  //$NON-NLS-1$
                    new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);

            if( existingPropertyNames == null ) {
            	this.existingNames = new HashSet<String>(0);
            } else {
            	this.existingNames = existingPropertyNames;
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.dialogs.MessageDialog#createButton(org.eclipse.swt.widgets.Composite, int, java.lang.String, boolean)
         */
        @Override
        protected Button createButton( Composite parent,
                                       int id,
                                       String label,
                                       boolean defaultButton ) {
            Button btn = super.createButton(parent, id, label, defaultButton);

            if (id == IDialogConstants.OK_ID) {
                // disable OK button initially
                this.btnOk = btn;
                btn.setEnabled(false);
            }

            return btn;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
         */
        @Override
        protected Control createCustomArea( Composite parent ) {
            Composite pnl = new Composite(parent, SWT.NONE);
            pnl.setLayout(new GridLayout(2, false));
            pnl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            Label lblName = new Label(pnl, SWT.NONE);
            lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
            lblName.setText(UTIL.getString("AddParameterDialog_lblName_text"));  //$NON-NLS-1$

            Text txtName = new Text(pnl, SWT.BORDER);
            txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            txtName.setToolTipText(UTIL.getString("AddParameterDialog_txtName_toolTip")); //$NON-NLS-1$
            txtName.addModifyListener(new ModifyListener() {
                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleNameChanged(((Text)e.widget).getText());
                }
            });

            Label lblValue = new Label(pnl, SWT.NONE);
            lblValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
            lblValue.setText(UTIL.getString("AddParameterDialog_lblType_text"));  //$NON-NLS-1$

            Combo txtValue = WidgetFactory.createCombo(pnl,
                    SWT.SIMPLE,
                    GridData.FILL_HORIZONTAL);
            GridData gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            gd.verticalAlignment = GridData.BEGINNING;
            gd.grabExcessHorizontalSpace = true;
            txtValue.setLayoutData(gd);
            txtValue.add(IWSProfileConstants.QUERY_STRING);
            txtValue.add(IWSProfileConstants.URI);
            txtValue.select(0);
            txtValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            txtValue.setToolTipText(UTIL.getString("AddParameterDialog_txtType_toolTip"));  //$NON-NLS-1$
            txtValue.addSelectionListener(new SelectionListener() {

    			@Override
    			public void widgetSelected(SelectionEvent e) {
    				handleTypeChanged(((Combo)e.widget).getText());
    			}

    			@Override
    			public void widgetDefaultSelected(SelectionEvent e) {
    			}
    		});

            txtValue.setVisibleItemCount(2);
            
            Label lblDefaultValue = new Label(pnl, SWT.NONE);
            lblDefaultValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
            lblDefaultValue.setText(UTIL.getString("AddParameterDialog_lblDefaultValue_text"));  //$NON-NLS-1$

            Text txtDefaultValue = new Text(pnl, SWT.BORDER);
            txtDefaultValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            txtDefaultValue.setToolTipText(UTIL.getString("AddParameterDialog_txtDefaultValue_toolTip")); //$NON-NLS-1$
            txtDefaultValue.addModifyListener(new ModifyListener() {
                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleDefaultValueChanged((((Text)e.widget).getText()));
                }
            });
            
			return pnl;
    	}
        

        /**
         * @return the new property name (never <code>null</code>)
         * @throws IllegalArgumentException if called when dialog return code is not {@link Window#OK}.
         */
        public String getName() {
            CoreArgCheck.isEqual(getReturnCode(), Window.OK);
            return name;
        }
        
        /**
         * @return the new property value (never <code>null</code>)
         * @throws IllegalArgumentException if called when dialog return code is not {@link Window#OK}.
         */
        public String getType() {
            CoreArgCheck.isEqual(getReturnCode(), Window.OK);
            return type;
        }

        /**
		 * @return the defaultValue
		 */
		public String getDefaultValue() {
			return defaultValue;
		}

		/**
		 * @param defaultValue the defaultValue to set
		 */
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		/**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.window.Window#getShellStyle()
         */
        @Override
        protected int getShellStyle() {
            return super.getShellStyle() | SWT.RESIZE;
        }

        void handleNameChanged( String newName ) {
            this.name = newName;
            updateState();
        }
        
        void handleDefaultValueChanged( String newDefaultValue ) {
            this.setDefaultValue(newDefaultValue);
            updateState();
        }

        void handleTypeChanged(Object type) {
            this.type = (String)type;
            updateState();
        }

        private void updateState() {
            // check to see if new name is valid
            String msg = validateName();

            // update UI controls
            if (StringUtilities.isEmpty(msg)) {
                if (!this.btnOk.isEnabled()) {
                    this.btnOk.setEnabled(true);
                }

                if (this.imageLabel.getImage() != null) {
                    this.imageLabel.setImage(null);
                }

                this.imageLabel.setImage(getInfoImage());
                msg = UTIL.getString("AddParameterDialog_message"); //$NON-NLS-1$
            } else {
                // value is not valid
                if (this.btnOk.isEnabled()) {
                    this.btnOk.setEnabled(false);
                }

                this.imageLabel.setImage(getErrorImage());
            }

            this.messageLabel.setText(msg);
            this.messageLabel.pack();
        }

        private String validateName() {
            String errorMsg = validateName(this.name);

            if (errorMsg == null) {
                // make sure property ID doesn't already exist
                for (String existingName : this.existingNames) {
                    if (existingName.equals(this.name)) {
                        errorMsg = UTIL.getString("AddParameterDialog_customParameterAlreadyExists", this.name); //$NON-NLS-1$
                        break;
                    }
                }
            }

            return errorMsg;
        }

        /**
         * @param proposedName the proposed property name
         * @return an error message or <code>null</code> if name is valid
         */
        public String validateName( String proposedName ) {
            // must have a name
            if (StringUtilities.isEmpty(proposedName)) {
                return UTIL.getString("AddParameterDialog_emptyParameterName");  //$NON-NLS-1$
            }

            // make sure only letters
            for (char c : proposedName.toCharArray()) {
                if ( ! isValidChar(c)) {
                    return UTIL.getString("AddParameterDialog_invalidParametersName");  //$NON-NLS-1$
                }
            }

            // valid name
            return null;
        }
        
        private boolean isValidChar(char c) {
        	if((Character.isLetter(c) || Character.isDigit(c))) return true;
        	
        	return false;
        }
        
        /**
         * @param proposedValue the proposed value
         * @return an error message or <code>null</code> if value is valid
         */
        public String validateValue( String proposedValue ) {
            // must have a value
            if (StringUtilities.isEmpty(proposedValue)) {
                return UTIL.getString("AddParameterDialog_emptyParameterValue"); //$NON-NLS-1$
            }

            // valid
            return null;
        }

    }

}
