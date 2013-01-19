package org.icmwind.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Search {
	Analyzer analyzer = null;
	Directory index = null;
	
	IndexWriterConfig config = null;
	IndexWriter writer = null;
	
	IndexReader reader = null;
	IndexSearcher searcher = null;
	
	int hitsPerPage = 0;
	
	/**
	 * 
	 */
	public Search() {
		System.out.println("Initialising Search.");
		
		this.analyzer = new StandardAnalyzer(Version.LUCENE_36);
		this.index = new RAMDirectory();
	}

	// Initialise the set up with number of hits to return per page in result set
	public void intialiseIndexing() throws CorruptIndexException, LockObtainFailedException, IOException {
		System.out.println("Initializing Indexer.");
		
		this.config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		this.writer = new IndexWriter(index, config);
	}
	
	public void initialiseSearching(int numberOfHits) throws CorruptIndexException, IOException {
		System.out.println("Initialising Searcher.");
		
		this.reader = IndexReader.open(index);
		this.searcher = new IndexSearcher(reader);
	
		this.hitsPerPage = numberOfHits;
	}
	
	// Index set of values for given fieldName  
	public void indexItems(String fieldName, Collection<String> values) throws CorruptIndexException, IOException {
		for(String valueText : values)
			indexItem(fieldName, valueText);
		closeIndexing();
	}
	
	// Index value for given fieldName
	// One Document(One Field)
	public void indexItem(String fieldName, String valueText) throws CorruptIndexException, IOException {
		Document document = new Document();
		document.add(new Field(fieldName, valueText, Store.YES, Index.ANALYZED));
		writer.addDocument(document);
//		System.out.println("Index: " + fieldName + "-" + valueText);
	}
	
	// Close indexing
	public void closeIndexing() throws CorruptIndexException, IOException {
		System.out.println("Termination Indexing.");
		writer.close();
	}
	 
	// Wild Card Search
	public List<String> wildCardSearch(String fieldname, String searchterm) throws ParseException, IOException {
		
		System.out.println("\nWildcard SEARCH: " + "query - " + searchterm);
		System.out.print("Wildcard SEARCH: " + "result - " );
		
		
		// make search item as wild card search query
		String queryterm = searchterm.trim() + "*";
		Query query = new QueryParser(Version.LUCENE_36, fieldname, analyzer).parse(queryterm);
		
		// Collector to collect matched result
		TopScoreDocCollector collector =  TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(query, collector);
		
		// Collect Result
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		/* if no hits found
		 * 	return NO MATCH FOUND.
		 * else 
		 * 	if hit/doc contains searchterm
		 *  	return MATCHED TERM
		 *  else
		 *  	add hit/doc to MatchedItems
		 *  return matched items 
		 */
		
		List<String> matcheditems = new ArrayList<String>();
		
		// if no hits found, return "NA" - Not Available
		if(hits.length == 0) {
			
			matcheditems.add("NA");
			return matcheditems;
			
		} else {
			
			for(ScoreDoc hit : hits) {
				String result = searcher.doc(hit.doc).get(fieldname);
				
				// if perfect match found, clear earlier results and return 
				if(result.equals(searchterm)) {
			
					if(matcheditems.size() > 0) {
						matcheditems.clear();
					}
					
					matcheditems.add(result);
					return matcheditems;
				} else {
					
					// add matched item to result set
					matcheditems.add(result);
				}
			}
			
			// return matched items for given query
			return matcheditems;
			
		}
	}
	
	public void closeSearching() throws IOException{
		reader.close();
	}
	
	
	/*
	 * 	// Perform wildcard search for given term in a given field
	public Map<String, List<String>> wildCardSearchMap(String fieldname, String searchterm) throws ParseException, IOException {
		
		// results
		Map<String, List<String>> matchedItems = new HashMap<String, List<String>>();
		
		// make search item as wild card search query
		String queryterm = searchterm + "*";
		Query query = new QueryParser(Version.LUCENE_36, fieldname, analyzer).parse(queryterm);
		
		// Collector to collect matched result
		TopScoreDocCollector collector =  TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(query, collector);
		
		// Collect Result
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		/* if no hits found
		 * 	return NO MATCH FOUND.
		 * else 
		 * 	if hit/doc contains searchterm
		 *  	return MATCHED TERM
		 *  else
		 *  	add hit/doc to MatchedItems 
		 */
	/*
		List<String> results = new ArrayList<String>();
		
		// if no hits found, return "No result found."
		if(hits.length == 0) {
			results.add("No match found.");
			matchedItems.put(queryterm, results);
			return matchedItems; 
		} else {
			for(ScoreDoc hit : hits) {
				String result = searcher.doc(hit.doc).get(fieldname);
				
				// if perfect match found, clear earlier results and return 
				if(result.contains(queryterm)) {
					if(results.size() > 0) {
						results.clear();
					}
					results.add(result);
					matchedItems.put(queryterm, results);
					return matchedItems;
				} else {
					// add matched item to result set
					results.add(result);
				}
			}
			
			// return matched items for given query
			matchedItems.put(queryterm, results);
			return matchedItems;
		}
	}
	 * 
	 */
}
