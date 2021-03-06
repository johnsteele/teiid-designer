/* Generated By:JJTree: Do not edit this line. ObjectTable.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.query.sql.lang;

import java.util.ArrayList;
import java.util.List;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import org.teiid.designer.annotation.Since;
import org.teiid.designer.query.sql.lang.IObjectTable;
import org.teiid.designer.runtime.version.spi.TeiidServerVersion.Version;
import org.teiid.query.parser.LanguageVisitor;
import org.teiid.query.parser.TeiidParser;
import org.teiid.query.sql.symbol.DerivedColumn;

/**
 *
 */
@Since(Version.TEIID_8_0)
public class ObjectTable extends TableFunctionReference implements IObjectTable<LanguageVisitor> {

    /**
     * Default language key for script engine
     */
    public static final String DEFAULT_LANGUAGE = "teiid_script"; //$NON-NLS-1$

    private List<ObjectColumn> columns = new ArrayList<ObjectColumn>();

    private String rowScript;

    private List<DerivedColumn> passing = new ArrayList<DerivedColumn>();

    private String scriptingLanguage;

    private CompiledScript compiledScript;

    private ScriptEngine scriptEngine;

    /**
     * @param p
     * @param id
     */
    public ObjectTable(TeiidParser p, int id) {
        super(p, id);
    }

    /**
     * @return columns
     */
    public List<ObjectColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns
     */
    public void setColumns(List<ObjectColumn> columns) {
        this.columns = columns;
    }

    /**
     * @return scripting language
     */
    public String getScriptingLanguage() {
        return scriptingLanguage;
    }
    
    /**
     * @param scriptingLanguage
     */
    public void setScriptingLanguage(String scriptingLanguage) {
        this.scriptingLanguage = scriptingLanguage;
    }

    /**
     * @return passing
     */
    public List<DerivedColumn> getPassing() {
        return passing;
    }
    
    /**
     * @param passing
     */
    public void setPassing(List<DerivedColumn> passing) {
        this.passing = passing;
    }
    
    /**
     * @return row script
     */
    public String getRowScript() {
        return rowScript;
    }
    
    /**
     * @param query
     */
    public void setRowScript(String query) {
        this.rowScript = query;
    }

    /**
     * @return script engine
     */
    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
    
    /**
     * @param scriptEngine
     */
    public void setScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    /**
     * @return compiled script
     */
    public CompiledScript getCompiledScript() {
        return compiledScript;
    }
    
    /**
     * @param compiledScript
     */
    public void setCompiledScript(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.columns == null) ? 0 : this.columns.hashCode());
        result = prime * result + ((this.passing == null) ? 0 : this.passing.hashCode());
        result = prime * result + ((this.rowScript == null) ? 0 : this.rowScript.hashCode());
        result = prime * result + ((this.scriptingLanguage == null) ? 0 : this.scriptingLanguage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectTable other = (ObjectTable)obj;
        if (this.columns == null) {
            if (other.columns != null)
                return false;
        } else if (!this.columns.equals(other.columns))
            return false;
        if (this.passing == null) {
            if (other.passing != null)
                return false;
        } else if (!this.passing.equals(other.passing))
            return false;
        if (this.rowScript == null) {
            if (other.rowScript != null)
                return false;
        } else if (!this.rowScript.equals(other.rowScript))
            return false;
        if (this.scriptingLanguage == null) {
            if (other.scriptingLanguage != null)
                return false;
        } else if (!this.scriptingLanguage.equals(other.scriptingLanguage))
            return false;
        return true;
    }

    /** Accept the visitor. **/
    @Override
    public void acceptVisitor(LanguageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ObjectTable clone() {
        ObjectTable clone = new ObjectTable(this.parser, this.id);

        if(getColumns() != null)
            clone.setColumns(cloneList(getColumns()));
        if(getScriptingLanguage() != null)
            clone.setScriptingLanguage(getScriptingLanguage());
        if(getCompiledScript() != null)
            clone.setCompiledScript(getCompiledScript());
        if(getScriptEngine() != null)
            clone.setScriptEngine(getScriptEngine());
        if(getPassing() != null)
            clone.setPassing(cloneList(getPassing()));
        if(getRowScript() != null)
            clone.setRowScript(getRowScript());
        if(getName() != null)
            clone.setName(getName());
        clone.setOptional(isOptional());
        clone.setMakeInd(isMakeInd());
        clone.setNoUnnest(isNoUnnest());
        clone.setMakeDep(isMakeDep());
        clone.setMakeNotDep(isMakeNotDep());
        clone.setPreserve(isPreserve());

        return clone;
    }

}
/* JavaCC - OriginalChecksum=2c94f0f8a18498b57fc9853980a571ea (do not edit this line) */
