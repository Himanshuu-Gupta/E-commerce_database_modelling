package edu.ncsu.projects.dbms2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.projects.dbms2.dao.BooksDao;
import edu.ncsu.projects.dbms2.entity.Books;
import edu.ncsu.projects.dbms2.repo.BooksRepository;

@Service
public class BooksService {
	
	/*
	 * @Autowired private BooksRepository bookRepository;
	 * 
	 * @Autowired private BooksDao booksDao;
	 * 
	 * public List<Books> getAllBooks() { ArrayList<Books> books = new
	 * ArrayList<>();
	 * 
	 * bookRepository.findAll().forEach(books::add);
	 * 
	 * return books; }
	 * 
	 * public List<Books> getAllBooksJDBC() { return booksDao.findAll(); }
	 */
}
