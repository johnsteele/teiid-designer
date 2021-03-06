/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.diagram.ui.util;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.teiid.designer.core.util.INewModelObjectHelper;
import org.teiid.designer.core.workspace.ModelResource;
import org.teiid.designer.diagram.ui.pakkage.PackageDiagramUtil;
import org.teiid.designer.ui.viewsupport.ModelUtilities;


/**
 * @since 8.0
 */
public class DiagramUiNewModelObjectHelper implements INewModelObjectHelper {

    /**
     * @since 4.3
     */
    public DiagramUiNewModelObjectHelper() {
        super();
    }

    /**
     * @see org.teiid.designer.core.util.INewModelObjectHelper#canHelpCreate(java.lang.Object)
     * @since 4.3
     */
    @Override
	public boolean canHelpCreate( Object newObject ) {

        // First case is a standard virtual table
        // If the createdObject is VirtualTable, set supportsUpdate to false
        if (newObject != null && newObject instanceof EObject) {
            EObject newEObject = (EObject)newObject;
            return isValidPackage(newEObject);
        }
        return false;
    }

    private boolean isValidPackage( EObject eObject ) {
        int objType = RelationalUmlEObjectHelper.getEObjectType(eObject);
        if (objType == RelationalUmlEObjectHelper.UML_PACKAGE) return true;

        return false;
    }

    /**
     * @see org.teiid.designer.core.util.INewModelObjectHelper#helpCreate(java.lang.Object, Map)
     * @since 4.3
     */
    @Override
	public boolean helpCreate( Object newObject,
                               Map properties ) {
        if (newObject != null && newObject instanceof EObject && canHelpCreate(newObject)) {
            EObject newEObject = (EObject)newObject;
            if (isValidPackage(newEObject)) {
                ModelResource mr = ModelUtilities.getModelResourceForModelObject(newEObject);
                if (mr != null) {
                    PackageDiagramUtil.createPackageDiagram(newEObject, mr);
                }
            }
        }
        return true;
    }

}
