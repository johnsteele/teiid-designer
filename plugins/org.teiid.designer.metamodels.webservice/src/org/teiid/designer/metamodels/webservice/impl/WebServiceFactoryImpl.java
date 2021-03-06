/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.metamodels.webservice.impl;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.teiid.designer.metamodels.webservice.Input;
import org.teiid.designer.metamodels.webservice.Interface;
import org.teiid.designer.metamodels.webservice.Operation;
import org.teiid.designer.metamodels.webservice.OperationUpdateCount;
import org.teiid.designer.metamodels.webservice.Output;
import org.teiid.designer.metamodels.webservice.SampleFile;
import org.teiid.designer.metamodels.webservice.SampleFromXsd;
import org.teiid.designer.metamodels.webservice.SampleMessages;
import org.teiid.designer.metamodels.webservice.WebServiceFactory;
import org.teiid.designer.metamodels.webservice.WebServicePackage;


/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 *
 * @since 8.0
 */
public class WebServiceFactoryImpl extends EFactoryImpl implements WebServiceFactory {

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public WebServiceFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create( EClass eClass ) {
        switch (eClass.getClassifierID()) {
            case WebServicePackage.OPERATION:
                return createOperation();
            case WebServicePackage.INPUT:
                return createInput();
            case WebServicePackage.OUTPUT:
                return createOutput();
            case WebServicePackage.INTERFACE:
                return createInterface();
            case WebServicePackage.SAMPLE_MESSAGES:
                return createSampleMessages();
            case WebServicePackage.SAMPLE_FILE:
                return createSampleFile();
            case WebServicePackage.SAMPLE_FROM_XSD:
                return createSampleFromXsd();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object createFromString( EDataType eDataType,
                                    String initialValue ) {
        switch (eDataType.getClassifierID()) {
            case WebServicePackage.ISTATUS:
                return createIStatusFromString(eDataType, initialValue);
            case WebServicePackage.OPERATION_UPDATE_COUNT: {
                OperationUpdateCount result = OperationUpdateCount.get(initialValue);
                if (result == null) throw new IllegalArgumentException(
                                                                       "The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                return result;
            }
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String convertToString( EDataType eDataType,
                                   Object instanceValue ) {
        switch (eDataType.getClassifierID()) {
            case WebServicePackage.ISTATUS:
                return convertIStatusToString(eDataType, instanceValue);
            case WebServicePackage.OPERATION_UPDATE_COUNT:
                return instanceValue == null ? null : instanceValue.toString();
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public Operation createOperation() {
        OperationImpl operation = new OperationImpl();
        return operation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public Input createInput() {
        InputImpl input = new InputImpl();
        return input;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public Output createOutput() {
        OutputImpl output = new OutputImpl();
        return output;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public Interface createInterface() {
        InterfaceImpl interface_ = new InterfaceImpl();
        return interface_;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public SampleMessages createSampleMessages() {
        SampleMessagesImpl sampleMessages = new SampleMessagesImpl();
        return sampleMessages;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public SampleFile createSampleFile() {
        SampleFileImpl sampleFile = new SampleFileImpl();
        return sampleFile;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public SampleFromXsd createSampleFromXsd() {
        SampleFromXsdImpl sampleFromXsd = new SampleFromXsdImpl();
        return sampleFromXsd;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public IStatus createIStatusFromString( EDataType eDataType,
                                            String initialValue ) {
        return (IStatus)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String convertIStatusToString( EDataType eDataType,
                                          Object instanceValue ) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
	public WebServicePackage getWebServicePackage() {
        return (WebServicePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static WebServicePackage getPackage() { // NO_UCD
        return WebServicePackage.eINSTANCE;
    }

} // WebServiceFactoryImpl
