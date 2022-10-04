package com.seok.home.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seok.home.cs_board.CsBoardDTO;
import com.seok.home.cs_board.CsDAO;
import com.seok.home.member.MemberDAO;
import com.seok.home.member.MemberDTO;
import com.seok.home.member.RoleDTO;

@Service
public class AdminService {
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private CsDAO csDAO;
	
	public String setCsAnswer(CsBoardDTO csBoardDTO) throws Exception{
		int result = csDAO.setAnswerDefault(csBoardDTO);
		
		String message = "";
		if(result==1) {
			message = "답변이 완료되었습니다";
		}else {
			message = "답변 등록에 실패하였습니다";
		}
		return message;
	}
	
	public CsBoardDTO getCsAnswer(CsBoardDTO csBoardDTO) throws Exception{
		csBoardDTO = csDAO.getBoardDetail(csBoardDTO);
		return csBoardDTO;
	}
	
	public List<CsBoardDTO> getCsList(AdminPager pager)throws Exception{
		pager.calNum(csDAO.getTotalCount(pager));
		List<CsBoardDTO> csList = csDAO.getBoardList(pager);
		
		return csList;
	}
	
	public boolean getLogin(MemberDTO memberDTO) throws Exception {
		
		memberDTO = memberDAO.getLogin(memberDTO);
		
		boolean chk = false;
		
		if(memberDTO!=null) {			
			List<RoleDTO> roles = memberDTO.getRoleDTOs();
			
			for(RoleDTO role : roles) {
				if(role.getRoleName().equals("관리자")) {
					chk = true;
				}
			}
		}
		
		
		return chk;
	}
	
	public String getAuth(MemberDTO member) throws Exception{
		//아이디 패스워드 맞는지 확인
		
		member = memberDAO.getLogin(member);
		
		if(member==null) {
			return "일석이조 회원 인증 실패";
		}else {			
			//권한추가
			int result = memberDAO.setAdminRole(member);
			if(result==1) {
				return "인증이 완료되었습니다\n관리자 로그인을 진행해주세요";				
			}else {
				return "관리자 인증 실패\n 담당자에게 문의해주세요";
			}
		}
		
	}

}
