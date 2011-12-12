/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.extension.ui.editors;

import static org.teiid.designer.extension.ui.UiConstants.UTIL;
import static org.teiid.designer.extension.ui.UiConstants.EditorIds.MED_OVERVIEW_PAGE;
import static org.teiid.designer.extension.ui.UiConstants.Form.COMBO_STYLE;
import static org.teiid.designer.extension.ui.UiConstants.Form.TEXT_STYLE;
import static org.teiid.designer.extension.ui.UiConstants.ImageIds.MED_EDITOR;

import java.beans.PropertyChangeEvent;
import java.util.Set;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.teiid.designer.extension.definition.ModelExtensionDefinition.PropertyName;
import org.teiid.designer.extension.definition.ModelExtensionDefinitionValidator;
import org.teiid.designer.extension.ui.Activator;
import org.teiid.designer.extension.ui.Messages;

import com.metamatrix.core.util.CoreStringUtil;
import com.metamatrix.modeler.internal.ui.forms.MessageFormDialog;

public final class OverviewEditorPage extends MedEditorPage {

    private Text txtNamespacePrefix;
    private Text txtNamespaceUri;
    private CCombo cbxMetamodelUris;
    private Text txtDescription;
    private Text txtVersion;

    private final ErrorMessage descriptionError;
    private final ErrorMessage metamodelUriError;
    private final ErrorMessage namespacePrefixError;
    private final ErrorMessage namespaceUriError;

    public OverviewEditorPage( ModelExtensionDefinitionEditor medEditor ) {
        super(medEditor, MED_OVERVIEW_PAGE, Messages.medEditorOverviewPageTitle);

        this.descriptionError = new ErrorMessage();
        validateDescription();

        this.metamodelUriError = new ErrorMessage();
        validateMetamodelUri();

        this.namespacePrefixError = new ErrorMessage();
        validateNamespacePrefix();

        this.namespaceUriError = new ErrorMessage();
        validateNamespaceUri();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.extension.ui.editors.MedEditorPage#createBody(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @SuppressWarnings("unused")
    @Override
    protected void createBody( Composite body,
                               FormToolkit toolkit ) {
        BODY: {
            body.setLayout(new GridLayout());
            body.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        }

        final Section finalSection;

        SECTION: {
            Section section = toolkit.createSection(body, ExpandableComposite.NO_TITLE | ExpandableComposite.TITLE_BAR
                    | ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT);
            section.setLayout(new GridLayout());
            section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            finalSection = section;
        }

        final Composite finalContainer;

        CONTAINER: {
            Composite container = toolkit.createComposite(finalSection);
            container.setLayout(new GridLayout(2, false));
            container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            finalSection.setClient(container);
            finalContainer = container;
        }

        NAMESPACE_PREFIX: {
            toolkit.createLabel(finalContainer, Messages.namespacePrefixLabel);

            this.txtNamespacePrefix = toolkit.createText(finalContainer, CoreStringUtil.Constants.EMPTY_STRING, TEXT_STYLE);
            this.txtNamespacePrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // set value
            refreshNamespacePrefixControl();

            this.txtNamespacePrefix.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleNamespacePrefixChanged(((Text)e.widget).getText());
                }
            });

            // associate control with error message
            this.namespacePrefixError.setControl(this.txtNamespacePrefix);
        }

        NAMESPACE_URI: {
            toolkit.createLabel(finalContainer, Messages.namespaceUriLabel);

            this.txtNamespaceUri = toolkit.createText(finalContainer, CoreStringUtil.Constants.EMPTY_STRING, TEXT_STYLE);
            this.txtNamespaceUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // set value
            refreshNamespaceUriControl();

            this.txtNamespaceUri.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleNamespaceUriChanged(((Text)e.widget).getText());
                }
            });

            // associate control with error message
            this.namespaceUriError.setControl(this.txtNamespaceUri);
        }

        METAMODEL_URI: {
            toolkit.createLabel(finalContainer, Messages.extendedMetamodelUriLabel);

            this.cbxMetamodelUris = new CCombo(finalContainer, COMBO_STYLE);
            toolkit.adapt(this.cbxMetamodelUris, true, false);
            this.cbxMetamodelUris.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            ((GridData)this.cbxMetamodelUris.getLayoutData()).heightHint = this.cbxMetamodelUris.getItemHeight() + 4;

            // populate metamodel names
            Set<String> metamodelNames = Activator.getDefault().getExtendableMetamodelNames();
            this.cbxMetamodelUris.setItems(metamodelNames.toArray(new String[metamodelNames.size()]));

            // set value
            refreshMetamodelUriControl();

            this.cbxMetamodelUris.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleMetamodelUriChanged(((CCombo)e.widget).getText());
                }
            });

            // associate control with error message
            this.metamodelUriError.setControl(this.cbxMetamodelUris);
        }

        VERSION: {
            toolkit.createLabel(finalContainer, Messages.versionLabel);

            this.txtVersion = toolkit.createText(finalContainer, CoreStringUtil.Constants.EMPTY_STRING, SWT.READ_ONLY | TEXT_STYLE);
            this.txtVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

            // set value
            refreshVersionControl();
        }

        DESCRIPTION: {
            Label label = toolkit.createLabel(finalContainer, Messages.descriptionLabel);
            label.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));

            this.txtDescription = toolkit.createText(finalContainer, CoreStringUtil.Constants.EMPTY_STRING, SWT.BORDER | SWT.MULTI
                    | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
            this.txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            // set value
            refreshDescriptionControl();

            this.txtDescription.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( ModifyEvent e ) {
                    handleDescriptionChanged(((Text)e.widget).getText());
                }
            });

            // associate control with error message
            this.descriptionError.setControl(this.txtDescription);
        }

        // clear any initial messages that were created before the control was set
        IMessageManager msgMgr = ((ModelExtensionDefinitionEditor)getEditor()).getMessageManager();
        msgMgr.removeMessage(this.descriptionError.getKey());
        msgMgr.removeMessage(this.metamodelUriError.getKey());
        msgMgr.removeMessage(this.namespacePrefixError.getKey());
        msgMgr.removeMessage(this.namespaceUriError.getKey());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.EditorPart#getTitleToolTip()
     */
    @Override
    public String getTitleToolTip() {
        return Messages.medEditorOverviewPageToolTip;
    }

    void handleDescriptionChanged( String newDescription ) {
        getMed().setDescription(newDescription);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.extension.ui.editors.MedEditorPage#handleMedReloaded()
     */
    @Override
    public void handleMedReloaded() {
        // make sure GUI has been constructed before reloading
        if (this.txtNamespacePrefix != null) {
            refreshNamespacePrefixControl();
            refreshNamespaceUriControl();
            refreshMetamodelUriControl();
            refreshVersionControl();
            refreshDescriptionControl();
        }
    }

    void handleMetamodelUriChanged( String newMetamodelName ) {
        String oldUri = getMed().getMetamodelUri();
        String newMetamodelUri = Activator.getDefault().getMetamodelUri(newMetamodelName);

        if (CoreStringUtil.valuesAreEqual(newMetamodelUri, oldUri)) {
            return;
        }

        boolean doIt = true;

        // changing metamodel will remove all metaclasses and associated properties so get confirmation from user
        if (!CoreStringUtil.isEmpty(oldUri) && (getMed().getExtendedMetaclasses().length != 0)) {
            if (!MessageFormDialog.openQuestion(getShell(), Messages.changeMetamodelDialogTitle,
                                                Activator.getDefault().getImage(MED_EDITOR), Messages.changeMetamodelDialogMsg)) {
                doIt = false;
            }
        }

        if (doIt) {
            getMed().setMetamodelUri(newMetamodelUri);
        } else if (!CoreStringUtil.isEmpty(oldUri)) {
            String oldMetamodelName = Activator.getDefault().getMetamodelName(oldUri);
            this.cbxMetamodelUris.setText(oldMetamodelName);
        }
    }

    void handleNamespacePrefixChanged( String newNamespacePrefix ) {
        getMed().setNamespacePrefix(newNamespacePrefix);
    }

    void handleNamespaceUriChanged( String newNamespaceUri ) {
        getMed().setNamespaceUri(newNamespaceUri);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.extension.ui.editors.MedEditorPage#handlePropertyChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void handlePropertyChanged( PropertyChangeEvent e ) {
        String propName = e.getPropertyName();

        if (PropertyName.DESCRIPTION.toString().equals(propName)) {
            validateDescription();
        } else if (PropertyName.METAMODEL_URI.toString().equals(propName)) {
            validateMetamodelUri();
        } else if (PropertyName.NAMESPACE_PREFIX.toString().equals(propName)) {
            validateNamespacePrefix();
        } else if (PropertyName.NAMESPACE_URI.toString().equals(propName)) {
            validateNamespaceUri();
        }
    }

    private void refreshDescriptionControl() {
        String description = getMed().getDescription();

        if (!CoreStringUtil.valuesAreEqual(this.txtDescription.getText(), description)) {
            this.txtDescription.setText((description == null) ? CoreStringUtil.Constants.EMPTY_STRING : description);
            validateDescription();
        }
    }

    private void refreshMetamodelUriControl() {
        String metamodelUri = getMed().getMetamodelUri();
        int currentIndex = this.cbxMetamodelUris.getSelectionIndex();
        int newIndex = -1; // no selection

        // find new selected index
        if (!CoreStringUtil.isEmpty(metamodelUri)) {
            String name = Activator.getDefault().getMetamodelName(metamodelUri);

            if (!CoreStringUtil.isEmpty(name)) {
                newIndex = this.cbxMetamodelUris.indexOf(name);
            }

            // metamodel URI not found
            if (newIndex == -1) {
                UTIL.log(NLS.bind(Messages.overviewPageInvalidMetamodelUriMsg, metamodelUri));
            }
        }

        // change selection if necessary
        if (newIndex != currentIndex) {
            if (newIndex == -1) {
                this.cbxMetamodelUris.setText(CoreStringUtil.Constants.EMPTY_STRING);
            } else {
                this.cbxMetamodelUris.select(newIndex);
            }

            validateMetamodelUri();
        }
    }

    private void refreshNamespacePrefixControl() {
        String namespacePrefix = getMed().getNamespacePrefix();

        if (!CoreStringUtil.valuesAreEqual(this.txtNamespacePrefix.getText(), namespacePrefix)) {
            this.txtNamespacePrefix.setText((namespacePrefix == null) ? CoreStringUtil.Constants.EMPTY_STRING : namespacePrefix);
            validateNamespacePrefix();
        }
    }

    private void refreshNamespaceUriControl() {
        String namespaceUri = getMed().getNamespaceUri();

        if (!CoreStringUtil.valuesAreEqual(this.txtNamespaceUri.getText(), namespaceUri)) {
            this.txtNamespaceUri.setText((namespaceUri == null) ? CoreStringUtil.Constants.EMPTY_STRING : namespaceUri);
            validateNamespaceUri();
        }
    }

    private void refreshVersionControl() {
        String version = Integer.toString(getMed().getVersion());

        if (!CoreStringUtil.valuesAreEqual(this.txtVersion.getText(), version)) {
            this.txtVersion.setText(version);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.FormPage#setFocus()
     */
    @Override
    public void setFocus() {
        if (this.txtNamespacePrefix != null) {
            this.txtNamespacePrefix.setFocus();
        } else {
            super.setFocus();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.extension.ui.editors.MedEditorPage#setResourceReadOnly(boolean)
     */
    @Override
    protected void setResourceReadOnly( boolean readOnly ) {
        // return if GUI hasn't been constructed yet
        if (this.cbxMetamodelUris == null) {
            return;
        }

        this.cbxMetamodelUris.setEnabled(!readOnly);
        this.txtDescription.setEnabled(!readOnly);
        this.txtNamespacePrefix.setEnabled(!readOnly);
        this.txtNamespaceUri.setEnabled(!readOnly);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.extension.ui.editors.MedEditorPage#updateAllMessages()
     */
    @Override
    protected void updateAllMessages() {
        validateDescription();
        validateMetamodelUri();
        validateNamespacePrefix();
        validateNamespaceUri();
    }

    private void validateDescription() {
        this.descriptionError.setStatus(ModelExtensionDefinitionValidator.validateDescription(getMed().getDescription()));
        updateMessage(this.descriptionError);
    }

    private void validateMetamodelUri() {
        this.metamodelUriError.setStatus(ModelExtensionDefinitionValidator.validateMetamodelUri(getMed().getMetamodelUri(),
                                                                                                getRegistry().getExtendableMetamodelUris()));
        updateMessage(this.metamodelUriError);
    }

    private void validateNamespacePrefix() {
        this.namespacePrefixError.setStatus(ModelExtensionDefinitionValidator.validateNamespacePrefix(getMed().getNamespacePrefix(),
                                                                                                      getRegistry().getAllNamespacePrefixes()));
        updateMessage(this.namespacePrefixError);
    }

    private void validateNamespaceUri() {
        this.namespaceUriError.setStatus(ModelExtensionDefinitionValidator.validateNamespaceUri(getMed().getNamespaceUri(),
                                                                                                getRegistry().getAllNamespaceUris()));
        updateMessage(this.namespaceUriError);
    }

}
