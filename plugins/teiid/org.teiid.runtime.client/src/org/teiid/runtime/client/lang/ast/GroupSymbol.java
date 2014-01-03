/* Generated By:JJTree: Do not edit this line. GroupSymbol.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast;

import org.teiid.runtime.client.lang.parser.TeiidParser;

public class GroupSymbol extends Symbol {

    /** Definition of the symbol, may be null */
    private String definition;

    public GroupSymbol(int id) {
        super(id);
    }

    public GroupSymbol(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * Get the definition for the group symbol, which may be null
     * @return Group definition, may be null
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Set the definition for the group symbol, which may be null
     * @param definition Definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /** Accept the visitor. **/
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit(this, data);
    }
}
/* JavaCC - OriginalChecksum=f7012acf7f9a059597f0384f4fcb74fa (do not edit this line) */
