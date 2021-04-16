package edu.ncsu.projects.dbms2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.projects.dbms2.dao.MemberDao;
import edu.ncsu.projects.dbms2.entity.Member;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;
	
	public List<Member> getAllMembers() {
		return memberDao.findAll();
	}
}
