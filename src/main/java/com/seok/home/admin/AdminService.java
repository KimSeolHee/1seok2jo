package com.seok.home.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seok.home.cs_board.CsBoardDTO;
import com.seok.home.cs_board.CsDAO;
import com.seok.home.f_board.FreeBoardDTO;
import com.seok.home.lecture.LectureDAO;
import com.seok.home.lecture.LectureDTO;
import com.seok.home.lecture.add.LectureAddDAO;
import com.seok.home.member.MemberDAO;
import com.seok.home.member.MemberDTO;
import com.seok.home.member.RoleDTO;
import com.seok.home.pay.PayDAO;
import com.seok.home.pay.PaymentDTO;
import com.seok.home.util.ChartDTO;

@Service
public class AdminService {
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private LectureDAO lectureDAO;
	@Autowired
	private LectureAddDAO lectureAddDAO;
	@Autowired
	private CsDAO csDAO;
	@Autowired
	private PayDAO payDAO;
	
	public HashMap<String, Object> getAdminDashBoard () throws Exception{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		//하루 매출액 그래프
		List<ChartDTO> charts = payDAO.getChartAdminDashBoard();
		
		//전체 회원수 
		Long memberCnt = memberDAO.getMemberCnt();
		//전체 강의수
		Long lectureCnt = lectureDAO.getLectureCnt();
		//전체 게시글 수
		Long boardCnt = adminDAO.getBoardCnt();
		//현재 수강중인 강의 수
		Long lectureIng = lectureAddDAO.getLectureIngCnt();
		
		//미답변 문의수 
		Long notAnswerCnt = csDAO.getNotAnswerCnt();
		
		result.put("charts", charts);
		result.put("memberCnt", memberCnt);
		result.put("lectureCnt", lectureCnt);
		result.put("boardCnt", boardCnt);
		result.put("lectureIng", lectureIng);
		result.put("notAnswerCnt", notAnswerCnt);
		
		return result;
	}
	
	
	public List<FreeBoardDTO> getBoardsList(AdminPager pager) throws Exception{
		pager.calNum(adminDAO.getTotalBoardList(pager));
		
		return adminDAO.getBoardList(pager);
	}
	
	public List<LectureDTO> getLectureList(AdminPager pager) throws Exception{
		Long totalCount = lectureDAO.getCount(pager);
		pager.calNum(totalCount);
		
		List<LectureDTO> dtos = lectureDAO.getLecture(pager);
		return dtos;
	}
	
	public List<MemberDTO> getMember(AdminPager adminPager) throws Exception{
		adminPager.calNum(memberDAO.getAdminMemberTotal(adminPager));
		
		return memberDAO.getAdminMemberList(adminPager);
	}
	
	public List<PaymentDTO> getPaymentsList(AdminPager adminPager) throws Exception{
		adminPager.calNum(payDAO.getPayAdminTotal(adminPager));
		
		List<PaymentDTO> paylist = payDAO.getPayAdminList(adminPager);
		return paylist;
	}
	
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
	
	public MemberDTO getLogin(MemberDTO memberDTO) throws Exception {
		
		memberDTO = memberDAO.getLogin(memberDTO);
		
		if(memberDTO!=null) {			
			List<RoleDTO> roles = memberDTO.getRoleDTOs();
			
			for(RoleDTO role : roles) {
				//로그인 후 관리자인걸 확인하면 memberDTO를 되돌려줌
				if(role.getRoleName().equals("관리자")) {
					return memberDTO;
				}
			}
		}
		
		//관리자 확인이 되지 않거나 로그인이 되지 않으면 null을 되돌려줌
		return null;
	}
	
	public String getAuth(MemberDTO member) throws Exception{
		//아이디 패스워드 맞는지 확인
		
		member = memberDAO.getLogin(member);
		
		if(member==null) {
			return "일석이조 회원 인증 실패";
		}else {
			//이미 권한이 있는지 확인
			for(RoleDTO role:member.getRoleDTOs()) {
				if(role.getRoleNum()==1L) {
					return "관리자 권한이 있는 계정입니다.";
				}
			}
			//권한추가
			int result = memberDAO.setAdminRole(member);
			if(result==1) {
				return "인증이 완료되었습니다\n관리자 로그인을 진행해주세요";				
			}else {
				return "관리자 인증 실패\n 담당자에게 문의해주세요";
			}
		}
		
	}
	
	//board삭제
	public int delBoardAdmin(FreeBoardDTO dto) throws Exception{
		dto = adminDAO.findBoard(dto);
		int result = adminDAO.setBoardDelete(dto);
		return result;
	}

}
