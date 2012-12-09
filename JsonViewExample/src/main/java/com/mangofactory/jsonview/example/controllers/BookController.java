package com.mangofactory.jsonview.example.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.mangofactory.jsonview.ResponseView;
import com.mangofactory.jsonview.example.Book;
import com.mangofactory.jsonview.example.Book.SummaryView;

@Controller
public class BookController {

	List<Book> data;
	
	public BookController()
	{
		data = Lists.newArrayList(
				new Book("Effective Java","Joshua Bloch","Essential",1),
				new Book("Breaking Dawn","Stephanie Myers","Just terrible",2)
				);
	}
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
	@RequestMapping("/book/{id}/summary")
	@ResponseView(SummaryView.class)
	public @ResponseBody Book getSummary(@PathVariable("id") Integer id)
	{
		return data.get(id - 1);
	}
	@RequestMapping("/book/{id}")
	public @ResponseBody Book getDetail(@PathVariable("id") Integer id)
	{
		return data.get(id - 1);
	}
}
