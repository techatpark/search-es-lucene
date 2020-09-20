package com.techatpark.search;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class MyCustomAnalyzer extends Analyzer{

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        TokenStream result = new StandardTokenizer();
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, CharArraySet.EMPTY_SET );
        result = new PorterStemFilter(result);
        result = new CapitalizationFilter(result);
        return new TokenStreamComponents(new StandardTokenizer(), result);
    }

}
