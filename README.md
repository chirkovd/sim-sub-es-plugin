# sim-sub-es-plugin
Elasticsearch Plugin for similarity and substructure searches by fingerprints

Steps to implement current plugin:
1. Create implementation of SimSubService and SimSubServiceFactory
2. Build plugin jar and install it to elasticsearch instance

Run different searches by java native library:
1) analyze any terms by defined analyzers
```
        AnalyzeRequest analyzeRequest = new AnalyzeRequest();
        analyzeRequest.tokenizer("sim-sub-similarity");
        
        Map<String, String> tokenFilter = new HashMap<>();
        tokenFilter.put("type", "length");
        tokenFilter.put("min", "1");
        
        analyzeRequest.addTokenFilter(tokenFilter);

        String text = "hello world";
        analyzeRequest.text(text);
```
2) similarity search by script functions - count score and limit results by min score value
```
        String queryValue = "first item";
        
        QueryBuilder query = QueryBuilders.matchQuery("item.sim", queryValue);

        Map<String, Object> params = new HashMap<>();
        params.put(SimilarityScriptFactory.FIELD, "item");
        params.put(SimilarityScriptFactory.TYPE, "tanimoto");
        params.put(SimilarityScriptFactory.QUERY, queryValue);

        Script script = new Script(ScriptType.INLINE, "native", "similarity_script", params);

        FunctionScoreQueryBuilder scoreQueryBuilder = QueryBuilders
                .functionScoreQuery(query, ScoreFunctionBuilders.scriptFunction(script));
        scoreQueryBuilder.boostMode(CombineFunction.REPLACE);
        scoreQueryBuilder.setMinScore(80);
```
3) substructure search by script query
```
        String queryValue = "first item";
        
        Map<String, Object> params = new HashMap<>();
        params.put(SubstructureScriptFactory.FIELD, "item");
        params.put(SubstructureScriptFactory.QUERY, queryValue);

        Script script = new Script(ScriptType.INLINE, "native", "substructure_script", params);
        ScriptQueryBuilder scriptQuery = QueryBuilders.scriptQuery(script);
        QueryBuilder query = QueryBuilders.boolQuery().must(scriptQuery);

```
4) substructure search by tokenizer
```
        String queryValue = "first item";
        QueryBuilder query = QueryBuilders.matchQuery("item.sub", queryValue).operator(Operator.AND);
```
5) highlight results for substructure search
```
        String queryValue = "first item";
        
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("item");
        highlightBuilder.highlighterType("sim-sub-highlighter");
        
        Map<String, Object> options = new HashMap<>();
        options.put("query", queryValue);
        options.put("sTag", "<hi>");
        options.put("eTag", "</hi>");
        highlightBuilder.options(options);

        requestBuilder.highlighter(highlightBuilder);
```