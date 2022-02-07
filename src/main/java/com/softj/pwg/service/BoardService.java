package com.softj.pwg.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Coment;
import com.softj.pwg.entity.QBoard;
import com.softj.pwg.entity.QComent;
import com.softj.pwg.entity.User;
import com.softj.pwg.repo.BoardRepo;
import com.softj.pwg.repo.ComentRepo;
import com.softj.pwg.repo.UserRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final JPAQueryFactory jpaQueryFactory;

    private final BoardRepo boardRepo;
    private final ComentRepo comentRepo;
    private final UserRepo userRepo;

    public Page<Board> boardList(ParamVO params, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        //boardRepo.findById(params.getSeq());

        BooleanBuilder where = new BooleanBuilder();
        where.and(qBoard.nation.eq((String) AuthUtil.getAttr("nation")));

        return boardRepo.findAll(where, pageable); //board에 있는걸 다 가져온다.

    }

    public Board boardView(ParamVO params) {

        return boardRepo.findBySeq(params.getSeq());
    }

    public List<Coment> boardComent(ParamVO params ) {

        Board board = boardRepo.findBySeq(params.getSeq());
        return comentRepo.findComentByBoard(board); //coment에 있는걸 다 가져온다.

    }

    public Board boardWrite(ParamVO params) {
        String tmp = (String)AuthUtil.getAttr("nation");
        //영속성 상태가 아니기 때문에 일단 select를 먼저 해야함.
        Board board = Board.builder().build();
        if(params.getSeq() != 0){
            board = boardRepo.findBySeq(params.getSeq());
        }
        board.setSubject(params.getSubject());
        board.setContent(params.getContent());
        board.setNation((String) AuthUtil.getAttr("nation"));
        board.setUser((User) AuthUtil.getAttr("loginVO"));

        return boardRepo.save(board);

    }
    public Coment comentWrite(ParamVO params){//댓글 다려고 하면 board를 조인
        Board board = Board.builder().build(); //이런 형태가 있어야 한다.
        board.setSeq(params.getBoardSeq()); //시퀀스 가져옴.
        Coment coment = Coment.builder().build();
        if(params.getSeq() != 0){
            coment = comentRepo.findBySeq(params.getSeq());
        }
        coment.setContent(params.getContent());
        coment.setUser((User) AuthUtil.getAttr("loginVO"));
        coment.setBoard(board);
        return comentRepo.save(coment);
    }

    public void comentRemove(ParamVO params){
        Coment coment = Coment.builder().build();
        coment.setSeq((params.getSeq()));
        comentRepo.deleteBySeq(coment);

    }

//    public Coment boardComments(ParamVO params) {
//        User userid = (User) AuthUtil.getAttr("loginVO"); //user정보가 다들어있음
//        userid = userRepo.findSeqById(userid.getId()); //시퀀스값만 들어있음.
//        Board board = boardRepo.findBySeq(userid.getSeq());//시퀀스 값을 세팅.
//        Coment coment = Coment.builder()
//                .content(params.getContent())
//                .build();
//        coment.setBoard(board);
//        return comentRepo.save(coment);
////        board.setUser(user);//user시퀀스 값넣음.
////        return boardRepo.save(Coment);
////
//    }
}

//select 문 .
//        List<Board> board=boardRepo.findAllByNation(params.getNation());


//        return this.jpaQueryFactory.selectFrom(qBoard)
//                .where(qBoard.nation.eq((String)AuthUtil.getAttr("nation")))
//                .fetch();
//        Board board= null;
//        board = boardRepo.save(Board.builder()
//                .subject("제목")
//                .nation((String)AuthUtil.getAttr("nation")) //아이디
//                .content("내용")//플렛폼.
//                        .userSeq(103L)
//                .build());//쿼리문날라가고

//
//        return board;