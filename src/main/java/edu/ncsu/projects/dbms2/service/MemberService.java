package edu.ncsu.projects.dbms2.service;

import java.util.ArrayList;
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
		try {
			return memberDao.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Member>();
	}
	
	/*
	 * public Member getMemberById(Integer memberId) { try { return
	 * memberDao.findById(memberId); } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * return null; }
	 */
	
}
