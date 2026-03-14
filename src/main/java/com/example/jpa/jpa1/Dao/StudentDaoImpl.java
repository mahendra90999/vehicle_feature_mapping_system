package com.example.jpa.jpa1.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.jpa.jpa1.Entity.Student;
import com.example.jpa.jpa1.Repositoroy.StudentRepository;

public class StudentDaoImpl implements StudentDao{
	@Autowired
	StudentRepository studentRepository;
	
	@Override
	public List<Student> getAllStudents() {
		
		return studentRepository.findAll();
	}

	
	
}
