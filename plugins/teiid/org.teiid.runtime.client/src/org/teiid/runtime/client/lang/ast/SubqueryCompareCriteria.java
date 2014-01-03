/* Generated By:JJTree: Do not edit this line. SubqueryCompareCriteria.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.runtime.client.lang.ast;

import org.teiid.runtime.client.lang.TeiidNodeFactory.CommonNodes;
import org.teiid.runtime.client.lang.parser.TeiidParser;

public class SubqueryCompareCriteria extends AbstractCompareCriteria {

    public enum PredicateQuantifier {

        /** "Some" predicate quantifier (equivalent to "Any") */
        SOME,

        /** "Any" predicate quantifier (equivalent to "Some") */
        ANY,

        /** "All" predicate quantifier */
        ALL;

        public int getQuantifier() {
            return ordinal() + 2;
        }
    }

    private PredicateQuantifier predicateQuantifier = PredicateQuantifier.ALL;

    private QueryCommand command;

    public SubqueryCompareCriteria(int id) {
        super(id);
    }

    public SubqueryCompareCriteria(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * Get the predicate quantifier - returns one of the following:
     * <ul><li>{@link #NO_QUANTIFIER}</li>
     * <li>{@link #ANY}</li>
     * <li>{@link #SOME}</li>
     * <li>{@link #ALL}</li></ul>
     * @return the predicate quantifier
     */
    public PredicateQuantifier getPredicateQuantifier() {
        return this.predicateQuantifier;
    }

    /**
     * Returns the predicate quantifier as a string.
     * @return String version of predicate quantifier
     */
    public String getPredicateQuantifierAsString() {
        return this.predicateQuantifier.name();
    }

    /**
     * Set the predicate quantifier - use one of the following:
     * <li>{@link PredicateQuantifier#ANY}</li>
     * <li>{@link PredicateQuantifier#SOME}</li>
     * <li>{@link PredicateQuantifier#ALL}</li></ul>
     *
     * @param predicateQuantifier the predicate quantifier
     */
    public void setPredicateQuantifier(PredicateQuantifier predicateQuantifier) {
        this.predicateQuantifier = predicateQuantifier;
    }

    public QueryCommand getCommand() {
        return this.command;
    }

    /**
     * Set the subquery command (either a SELECT or a procedure execution).
     * @param command Command to execute to get the values for the criteria
     */
    public void setCommand(QueryCommand command) {
        this.command = command;
    }

    @Override
    public Expression getRightExpression() {
        ScalarSubquery scalarSubquery = parser.createCommonNode(CommonNodes.SCALAR_SUBQUERY);
        scalarSubquery.setCommand(getCommand());
        return scalarSubquery;
    }

    /** Accept the visitor. **/
    public void jjtAccept(Teiid8ParserVisitor visitor, Object data) {
        visitor.visit(this, data);
    }
}
/* JavaCC - OriginalChecksum=e9b141cd60d09da32342d127668258f8 (do not edit this line) */
