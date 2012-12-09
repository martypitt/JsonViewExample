JsonViewExample
===============

Support for custom Jackson @JsonView within Spring MVC

Allows per-method support of custom views within Spring MVC methods.

For example:

  @RequestMapping("/books")
	public @ResponseBody List<Book> getBooks()
	{
		return data;
	}
	@RequestMapping("/books/summaries")
	@ResponseView(SummaryView.class)
	public @ResponseBody List<Book> getBookSummaries()
	{
		return data;
	}


See http://martypitt.wordpress.com/2012/11/05/custom-json-views-with-spring-mvc-and-jackson/ for details.

