package ru.qwazer.solr.search;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 * @author ar
 * @since Date: 11.03.2014
 */
public class SavedCopyURLFunction extends ValueSourceParser {
    @Override
    public ValueSource parse(FunctionQParser fp) throws SyntaxError {
        String first = fp.parseArg();
        return new SavedCopyUrlValueSource(first);
    }
}
