/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.extension.registry;

import static org.teiid.designer.extension.ExtensionPlugin.Util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.osgi.util.NLS;
import org.teiid.designer.extension.Messages;
import org.teiid.designer.extension.definition.ModelExtensionAssistant;
import org.teiid.designer.extension.definition.ModelExtensionDefinition;
import org.teiid.designer.extension.definition.ModelExtensionDefinitionParser;
import org.teiid.designer.extension.properties.ModelExtensionPropertyDefinition;

import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.core.util.CoreStringUtil;

/**
 * 
 */
public final class ModelExtensionRegistry {
    /**
     * The metamodel URIs that can have extension properties associated with them.
     */
    private Set<String> extendableMetamodelUris;

    /**
     * Key is namespace prefix, value is model extension definition. Never <code>null</code>.
     */
    private final Map<String, ModelExtensionDefinition> definitions;

    /**
     * A collection of registry listeners (never <code>null</code>).
     */
    private final CopyOnWriteArrayList<RegistryListener> listeners;

    /**
     * Key is namespace URI, value is namespace prefix. Never <code>null</code>.
     */
    private final Map<String, String> namespaces;

    /**
     * Parser for the *.mxd file. Creates the model extension definition. Never <code>null</code>.
     */
    private ModelExtensionDefinitionParser parser;

    /**
     * @throws IllegalStateException if there is a problem with the model extension XSD
     */
    public ModelExtensionRegistry() throws IllegalStateException {
        this.definitions = new HashMap<String, ModelExtensionDefinition>();
        this.listeners = new CopyOnWriteArrayList<RegistryListener>();
        this.namespaces = new HashMap<String, String>();
        this.parser = new ModelExtensionDefinitionParser();
    }

    /**
     * @param definitionStream the model extension input stream (cannot be <code>null</code>)
     * @param assistant the model extension assistant (cannot be <code>null</code>)
     * @return the model extension definition (never <code>null</code>)
     * @throws Exception if the definition file is <code>null</code> or if there is a problem parsing the file
     */
    public ModelExtensionDefinition addDefinition( InputStream definitionStream,
                                                   ModelExtensionAssistant assistant ) throws Exception {
        ModelExtensionDefinition definition = this.parser.parse(definitionStream, assistant);
        assert definition != null : "parser should not return a null model extension definition"; //$NON-NLS-1$

        String namespacePrefix = definition.getNamespacePrefix();

        // don't allow a namespace prefix that has already been registered
        if (this.definitions.containsKey(namespacePrefix)) {
            throw new Exception(NLS.bind(Messages.namespacePrefixAlreadyRegistered, namespacePrefix));
        }

        String namespaceUri = definition.getNamespaceUri();

        // don't allow a namespace URI that has already been registered
        if (this.namespaces.containsKey(namespaceUri)) {
            throw new Exception(NLS.bind(Messages.namespaceUriAlreadyRegistered, namespaceUri));
        }

        // Determine if the definition extends a valid Metamodel
        String metamodelUri = definition.getMetamodelUri();

        if ((this.extendableMetamodelUris == null) || !this.extendableMetamodelUris.contains(metamodelUri)) {
            throw new Exception(NLS.bind(Messages.invalidMetamodelUriExtension, metamodelUri));
        }

        // add to registry
        this.definitions.put(namespacePrefix, definition);
        this.namespaces.put(namespaceUri, namespacePrefix);

        // notify registry listeners
        fireEvent(RegistryEvent.createAddDefinitionEvent(definition));

        return definition;
    }

    /**
     * @param listener the listener being added
     * @return <code>true</code> if the listener was successfully added
     */
    public boolean addListener( RegistryListener listener ) {
        CoreArgCheck.isNotNull(listener, "listener is null"); //$NON-NLS-1$
        return this.listeners.add(listener);
    }

    /**
     * @param path the resource path of the model extension definition is being requested (cannot be <code>null</code> or empty)
     * @return the model extension definition or <code>null</code> if not found
     */
    public ModelExtensionDefinition findDefinition( String path ) {
        CoreArgCheck.isNotEmpty(path, "path is empty"); //$NON-NLS-1$

        for (ModelExtensionDefinition med : getAllDefinitions()) {
            if (path.equals(med.getResourcePath())) {
                return med;
            }
        }

        // not found
        return null;
    }

    /**
     * @param event the event being broadcast (cannot be <code>null</code>)
     */
    private void fireEvent( RegistryEvent event ) {
        assert event != null : "event is null"; //$NON-NLS-1$

        for (RegistryListener listener : this.listeners) {
            try {
                listener.process(event);
            } catch (Exception e) {
                // don't let one listener exception stop others from being notified
                Util.log(e);
                removeListener(listener);
            }
        }
    }

    /**
     * @return a collection of all the model extension definitions (never <code>null</code>)
     */
    public Collection<ModelExtensionDefinition> getAllDefinitions() {
        return this.definitions.values();
    }

    /**
     * @return a collection of all model extension definition namespace prefixes (never <code>null</code>)
     */
    public Set<String> getAllNamespacePrefixes() {
        return new HashSet<String>(namespaces.values());
    }

    /**
     * @return a collection of all model extension definition namespace URIs (never <code>null</code>)
     */
    public Set<String> getAllNamespaceUris() {
        return this.namespaces.keySet();
    }

    /**
     * @param namespacePrefix the namespace prefix whose model extension definition is being requested (cannot be <code>null</code>
     *            or empty)
     * @return the model extension definition or <code>null</code> if not found
     */
    public ModelExtensionDefinition getDefinition( String namespacePrefix ) {
        CoreArgCheck.isNotEmpty(namespacePrefix, "namespacePrefix is empty"); //$NON-NLS-1$
        return this.definitions.get(namespacePrefix);
    }

    /**
     * @return a non-modifiable set of metamodel URIs that can have extension properties (never <code>null</code>)
     */
    public Set<String> getExtendableMetamodelUris() {
        if (this.extendableMetamodelUris == null) {
            return Collections.emptySet();
        }

        return this.extendableMetamodelUris;
    }

    /**
     * @param namespacePrefix the namespace prefix whose model extension assistant is being requested (cannot be <code>null</code>)
     * @return the model extension assistant (<code>null</code> if an assistant is not found)
     */
    public ModelExtensionAssistant getModelExtensionAssistant( String namespacePrefix ) {
        ModelExtensionDefinition definition = getDefinition(namespacePrefix);

        if (definition != null) {
            return definition.getModelExtensionAssistant();
        }

        return null;
    }

    /**
     * @param metaclassName the metaclass name whose model extension assistants are being requested
     * @return the model extension assistants of model extension definitions that extend the specified metaclass name (never
     *         <code>null</code>)
     */
    public Collection<ModelExtensionAssistant> getModelExtensionAssistants( String metaclassName ) {
        Collection<ModelExtensionAssistant> assistants = new ArrayList<ModelExtensionAssistant>();

        for (ModelExtensionDefinition definition : getAllDefinitions()) {
            if (definition.extendsMetaclass(metaclassName)) {
                assistants.add(definition.getModelExtensionAssistant());
            }
        }

        return assistants;
    }

    /**
     * @param metaclassName the metaclass name whose property definition is being requested (cannot be <code>null</code> or empty)
     * @param propId the property definition identifier (cannot be <code>null</code> or empty)
     * @return the requested property definition or <code>null</code> if not found
     */
    public ModelExtensionPropertyDefinition getPropertyDefinition( String metaclassName,
                                                                   String propId ) {
        CoreArgCheck.isNotEmpty(propId, "propId is empty"); //$NON-NLS-1$

        // get the namespace prefix from the id
        String namespacePrefix = ModelExtensionPropertyDefinition.Utils.getNamespacePrefix(propId);

        if (!CoreStringUtil.isEmpty(namespacePrefix)) {
            ModelExtensionDefinition definition = getDefinition(namespacePrefix);

            if (definition != null) {
                return definition.getPropertyDefinition(metaclassName, propId);
            }
        }

        // not found
        return null;
    }

    /**
     * @param namespacePrefix the namespace prefix containing the extension property definitions being requested (cannot be
     *            <code>null</code> or empty )
     * @param metaclassName the metaclass name whose extension property definitions are being requested (cannot be <code>null</code>
     *            or empty )
     * @return the property definitions (never <code>null</code>)
     */
    public Collection<ModelExtensionPropertyDefinition> getPropertyDefinitions( String namespacePrefix,
                                                                                String metaclassName ) {
        ModelExtensionDefinition definition = getDefinition(namespacePrefix);

        if (definition != null) {
            return definition.getPropertyDefinitions(metaclassName);
        }

        return Collections.emptyList();
    }

    /**
     * @param namespacePrefix the namespace prefix being checked (cannot be <code>null</code> or empty)
     * @return <code>true</code> if there is a model extension definition with that namespace prefix registered
     */
    public boolean isNamespacePrefixRegistered( String namespacePrefix ) {
        CoreArgCheck.isNotEmpty(namespacePrefix, "namespacePrefix is empty"); //$NON-NLS-1$
        return this.definitions.containsKey(namespacePrefix);
    }

    /**
     * @param namespaceUri the namespace URI being checked (cannot be <code>null</code> or empty)
     * @return <code>true</code> if there is a model extension definition with that namespace URI registered
     */
    public boolean isNamespaceUriRegistered( String namespaceUri ) {
        CoreArgCheck.isNotEmpty(namespaceUri, "namespaceUri is empty"); //$NON-NLS-1$
        return this.definitions.containsKey(this.namespaces.containsKey(namespaceUri));
    }

    /**
     * @param metamodelURI the metamodel URI being checked (cannot be <code>null</code> or empty)
     * @return <code>true</code> if the provided URI is included in the list of extendable metamodel URIs
     */
    public boolean isExtendable( String metamodelUri ) {
        CoreArgCheck.isNotEmpty(metamodelUri, "metamodelUri is empty"); //$NON-NLS-1$
        return this.extendableMetamodelUris.contains(metamodelUri);
    }

    /**
     * @param listener the listener being removed (cannot be <code>null</code>)
     * @return <code>true</code> if the listener was successfully removed
     */
    public boolean removeListener( RegistryListener listener ) {
        CoreArgCheck.isNotNull(listener, "listener is null"); //$NON-NLS-1$
        return this.listeners.remove(listener);
    }

    /**
     * <strong>This method should only be called at startup by the {@link org.teiid.designer.extension.ExtensionPlugin}.</strong>
     * 
     * @param extendableMetamodelUris the metamodel URIs that can have extension properties (cannot be <code>null</code>)
     * @throws Exception if this method is called more than once
     */
    public void setMetamodelUris( Set<String> extendableMetamodelUris ) throws Exception {
        CoreArgCheck.isNotNull(extendableMetamodelUris, "extendableMetamodelUris"); //$NON-NLS-1$

        // this will be set at plugin startup and should not be set again
        assert (this.extendableMetamodelUris == null) : "Extendable metamodel URIs being set for second time"; //$NON-NLS-1$
        this.extendableMetamodelUris = Collections.unmodifiableSet(extendableMetamodelUris);
    }

}