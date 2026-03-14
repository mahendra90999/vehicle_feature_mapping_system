package com.example.jpa.jpa1.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.StudentDto;
import com.example.jpa.jpa1.Entity.Student;
import com.example.jpa.jpa1.Repositoroy.StudentRepository;


@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	public List<StudentDto> getAllStudents(){
		
		List<Student> students = studentRepository.findAll();
		
		return students.stream()
				.map(s -> new StudentDto(s.getGrno(),s.getName(),s.getEmail(),s.getAge()))
				.collect(Collectors.toList());
		
	}
	
	
	public StudentDto addStudent(StudentDto studentDto) {
		
		Student student = Student.builder()
							.name(studentDto.getName())
			                .email(studentDto.getEmail())
			                .age(studentDto.getAge())
			                .build();
		
		Student saveStudent = studentRepository.save(student);
		
		return StudentDto.builder()
	            .grno(saveStudent.getGrno())
	            .name(saveStudent.getName())
	            .email(saveStudent.getEmail())
	            .age(saveStudent.getAge())
	            .build();

		
		
	}
	
}
