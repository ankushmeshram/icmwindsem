package org.icmwind.core.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.semanticweb.owlapi.model.OWLException;


public class TestLuceneAndOnto {

	int s = 0;
	/**
	 * @param args
	 * @throws OWLException 
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws OWLException, CorruptIndexException, LockObtainFailedException, IOException, ParseException {

		OntologyProcessImpl opi = OntologyProcessImpl.getInstance();
		opi.openFile("C:\\ICMWind\\eclipse_workspace\\icmwind\\gse_config\\ontologies\\WindTurbineOnto.owl");
		List<String> classNames = opi.getNormClassNamesList();
		
		// process class names to replace "_" with " ".
		System.out.println("\n----Pre-Processing of ClassNames----");
		System.out.println(classNames.toString());
		

		FileProcessImpl fpi = FileProcessImpl.getInstance();
		fpi.openFile("C:\\ICMWind\\eclipse_workspace\\icmwind\\data_files\\TestData.csv");
		List<String> headNames = fpi.getNormHeadNamesList();

		// process header names to remove "%","  " to "" and "-" to " "
		System.out.println("\n----Pre-Processing of HeaderNames----");
		System.out.println(headNames.toString());
		
				
		//	intialise Analyzer
		StandardAnalyzer analyzer =  new StandardAnalyzer(Version.LUCENE_36);
		Directory directory = new RAMDirectory();
		
		// setup for indexing
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		
		for(String clsName : classNames) {
			Document document = new Document();
			document.add(new Field("class", clsName, Store.YES, Index.ANALYZED));
			writer.addDocument(document);
		}
		System.out.println();
		
		writer.close();
		
		//	setup for searching
		IndexReader reader = IndexReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		int hitsPerPage = 10;
		
		int l = 0;
		for(String name : headNames) {
			System.out.println((l+1) + ". Input: " + name);
			System.out.println(wildCardSearch(analyzer, searcher, hitsPerPage, "class", name).toString()); 
			System.out.println();
			l++;
		}
		
		reader.close();
	}
	
	public static Query fuzzySearchQuery(String fieldName, String searchTerm) {
		Term term = new Term(fieldName, searchTerm);
		Query query = new FuzzyQuery(term, 0.001F);
		return query;
	}
	
	
	public static Set<String> wildCardSearch(Analyzer analyzer, IndexSearcher searcher, int hitsPerPage, String fieldName, String searchTerm) 
			throws ParseException, IOException {
		
		// Create WildCard query from fieldname and searchterm
		Query query = new QueryParser(Version.LUCENE_36, fieldName, analyzer).parse(searchTerm + "*");
		
		// Perform Search 
		TopScoreDocCollector collector =  TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(query, collector);
		
		// Collect Result
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		Set<String> resultSet = new HashSet<String>();
		String matchedTerm = null;
		
		// if matches are found
		if(hits.length > 0) {
			for(ScoreDoc scoreDoc : hits) {
				Document document = searcher.doc(scoreDoc.doc);
				resultSet.add(document.get("class"));
				if(document.get("class").contains(searchTerm)) {
					matchedTerm = document.get("class");
					System.out.println(matchedTerm);
				}
			}
			System.out.println("Result: " + resultSet.toString());
		
//			if(!(matchedTerm.equals(null))) {
//				resultSet.clear();
//				resultSet.add(matchedTerm);
//			}
			
			return resultSet;
		
		} else {
			
			//if no match found. 
			resultSet.add("No Match Found.");
			return resultSet;
		}
			
		
			 
	}

}


