/* Generated By:JJTree: Do not edit this line. WindowFunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=TeiidNodeFactory,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast.v8;

import org.teiid.runtime.client.lang.ast.AggregateSymbol;
import org.teiid.runtime.client.lang.ast.WindowFunction;
import org.teiid.runtime.client.lang.ast.SimpleNode;
import org.teiid.runtime.client.lang.ast.Teiid8ParserVisitor;
import org.teiid.runtime.client.lang.ast.WindowSpecification;
import org.teiid.runtime.client.lang.parser.v8.Teiid8Parser;

public class Window8Function extends SimpleNode implements WindowFunction {

    private AggregateSymbol function;

    private WindowSpecification windowSpecification;

    public Window8Function(int id) {
        super(id);
    }

    public Window8Function(Teiid8Parser p, int id) {
        super(p, id);
    }

    /**
     * @return the function
     */
    @Override
    public AggregateSymbol getFunction() {
        return this.function;
    }

    /**
     * @param function the function to set
     */
    @Override
    public void setFunction(AggregateSymbol function) {
        this.function = function;
    }

    /**
     * @return the windowSpecification
     */
    @Override
    public WindowSpecification getWindowSpecification() {
        return this.windowSpecification;
    }

    /**
     * @param windowSpecification the windowSpecification to set
     */
    @Override
    public void setWindowSpecification(WindowSpecification windowSpecification) {
        this.windowSpecification = windowSpecification;
    }

    @Override
    public Class<?> getType() {
        return function.getType();
    }

    /** Accept the visitor. **/
    @Override
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit((WindowFunction) this, data);
    }
}
/* JavaCC - OriginalChecksum=da43cffa8d1927cfd8b4f378cdcfeae0 (do not edit this line) */
