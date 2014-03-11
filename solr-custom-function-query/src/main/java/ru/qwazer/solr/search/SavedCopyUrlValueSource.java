package ru.qwazer.solr.search;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.StrDocValues;
import org.apache.lucene.queries.function.valuesource.LiteralValueSource;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;

import java.io.IOException;
import java.util.Map;

/**
 * Return string with saved copy url representation
 *
 * @author ar
 * @since Date: 11.03.2014
 */
public class SavedCopyUrlValueSource extends LiteralValueSource {


    public SavedCopyUrlValueSource(String string) {
        super(string);
    }


    @Override
    public FunctionValues getValues(Map context, AtomicReaderContext readerContext) throws IOException {

        final IndexReader topReader = ReaderUtil.getTopLevelContext(readerContext).reader();
        final AtomicReader r = SlowCompositeReaderWrapper.wrap(topReader);

        return new StrDocValues(this) {
            @Override
            public String strVal(int doc) {
                    String result = null;
                String rawId = null;
                try {
                    BytesRef ref = new BytesRef();
                    BinaryDocValues values = FieldCache.DEFAULT.getTerms(r, string, true);
                    values.get(doc, ref);
                    rawId = ref.utf8ToString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "/hbase-client/savedCopy?rawId=" + rawId + "&p=" + DigestUtils.md5Hex(rawId);
            }

            @Override
            public boolean bytesVal(int doc, BytesRef target) {
                target.copyBytes(bytesRef);
                return true;
            }

            @Override
            public String toString(int doc) {
                return string;
            }
        };
    }
}
