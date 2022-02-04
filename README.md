# simple-search

Simple tool to search words in large text.

1) Create simple new java application that accept files to analyze in args

2) Load files in memory

3) Accept console input from user to set searched term

4) Search specified term in text and return if a text match this term

5) You can specify implementation to use for searching :
- 0 | text_to_search ==> simple find (match exactly with a simple iterable search)
- 1 | text_to_search ==> regex find (match exactly with a regex search)
- 2 | text_to_search ==> use index and normalizer to search accent and case insensitive (compute a score depends of how many words match)
- 3 | text_to_search ==> use simple implementation of levenshtein algorithm to match near results