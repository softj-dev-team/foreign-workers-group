package com.softj.pwg.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softj.pwg.entity.*;
import com.softj.pwg.repo.BoardRepo;
import com.softj.pwg.repo.ComentRepo;
import com.softj.pwg.repo.LikeRepo;
import com.softj.pwg.repo.UserRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final JPAQueryFactory jpaQueryFactory;

    private final BoardRepo boardRepo;
    private final ComentRepo comentRepo;
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;

    public Page<Board> boardList(ParamVO params, Pageable pageable) {
        QBoard qBoard = QBoard.board; //앤티티 가져온거
        QComent qComent = QComent.coment;
        QLike qLike = QLike.like;
        User user = (User)AuthUtil.getAttr("loginVO");

        BooleanBuilder where = new BooleanBuilder();//where절을 조건문으로 만들수 있게 하는기능 자바 형태로 사용할 수 있다.
        where.and(qBoard.nation.eq((String) AuthUtil.getAttr("nation"))); //해당 카테고리 글만 보여지게.
        where.and(qBoard.isDel.eq(false));//삭제 처리가 안된글만 0이면은
        if (!StringUtils.isEmpty(params.getSearch())) {//검색어가 있으면 LIKE 추가
            where.and(qBoard.subject.contains(params.getSearch()));
            where.and(qBoard.content.contains(params.getSearch()));
        }
        if (params.isMyWritePage()) {//내가쓴글 알람 (서비스까지 구현완료)
            where.and(qBoard.user.eq((User)AuthUtil.getAttr("loginVO")));
        }
        if (params.isMyLikePage()) {//(서비스 구현해야하고 jpql로 변환해야함.)
            where.and(JPAExpressions.selectFrom(qLike).where(qLike.board.eq(qBoard).and(qLike.user.eq(user))).exists());
        }

        JPAQuery<Board> query = jpaQueryFactory.select(Projections.fields(Board.class,
                                    qBoard.seq,
                                    qBoard.createdAt,
                                    qBoard.updatedAt,
                                    qBoard.isDel,
                                    qBoard.createdId,
                                    qBoard.updatedId,
                                    qBoard.subject,
                                    qBoard.nation,
                                    qBoard.content,
                                    qBoard.views,
                                    qBoard.user,
                                    ExpressionUtils.as(
                                        JPAExpressions.select(qLike.count())
                                                .from(qLike)
                                                .where(qLike.board.eq(qBoard)),"likeCount"))
                                )
                                .from(qBoard)
                                .where(where)
                                .orderBy(qBoard.seq.desc())
                                .limit(pageable.getPageSize())//조회할 개수 지정
                                .offset(pageable.getOffset());//시작index지정offset

//        List<Board> list = query.fetch();
//        long count = query.fetchCount();

        return new PageImpl<Board>(query.fetch(), pageable, query.fetchCount());

    }

    //조회수
    public Board boardView(ParamVO params) { //글 상세 보여주는 메서드
        Board board = boardRepo.findBySeq(params.getSeq());
        board.increaseViewCount();
        return boardRepo.save(board);
    }
    //댓글리스트 어느글에 누가 댓글을 달았는지 알아야 된다.
    public Page<Coment> boardComent(ParamVO params,Pageable pageable) {
        //댓글 목록 조회하는 메서드<삭제안된 댓글만 조회해야 함.어떤글에 어떤 댓글 구현해야 하는지.>
        QComent qComent=QComent.coment;
        QBoard qBoard=QBoard.board;
        QUser qUser=QUser.user;

        User user = (User)AuthUtil.getAttr("loginVO");
        BooleanBuilder where = new BooleanBuilder();//where절을 조건문으로 만들수 있게 하는기능 자바 형태로 사용할 수 있다.
        where.and(qComent.isDel.eq(false));//삭제 처리가 안된글만 0이면은.

        if(params.isMyCommentPage()) { //mypage에서 들어온거
            where.and(qBoard.nation.eq((String) AuthUtil.getAttr("nation"))); //해당 카테고리 글만 보여지게.
            where.and(qBoard.user.eq(user));
            where.and(qComent.user.ne(user));
            JPAQuery<Coment> query = jpaQueryFactory.select(Projections.fields(Coment.class,
                        qComent.content,
                        qUser,
                        qBoard.seq,
                        qComent.createdAt
                    ))
                    .from(qComent)
                    .leftJoin(qBoard)
                    .on(qComent.board.eq(qBoard))
                    .leftJoin(qUser)
                    .on(qComent.user.eq(qUser))
                    .where(where)
                    .orderBy(qComent.seq.desc())//정렬
                    .limit(pageable.getPageSize())//조회할 개수 지정
                    .offset(pageable.getOffset());//시작index지정offset
            return new PageImpl<Coment>(query.fetch(), pageable, query.fetchCount());
        }else{//boardView 상세페이지에서 들어온거
            where.and(qComent.board.seq.eq(params.getSeq()));//특정게시글에 댓글만 조회하기 위한거.
        }
        JPAQuery<Coment> query = jpaQueryFactory
                .selectFrom(qComent)
                .join(qComent.user)
                .fetchJoin()
                .where(where)
                .orderBy(qComent.seq.desc())//정렬
                .limit(pageable.getPageSize())//조회할 개수 지정
                .offset(pageable.getOffset());//시작index지정offset
        return new PageImpl<Coment>(query.fetch(), pageable, query.fetchCount());

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

    //게시판삭제
    public Board deleteBoard(long seq){//board
        Board board =boardRepo.findBySeq(seq);//삭제할 글 을 의미 해당 시퀀스 어떤 글인지 조회함.
        board.setDel(true);
        return boardRepo.save(board);
    }
    //댓글삭제
    public Coment deleteComment(long seq){ //어느 게시글에 한 댓글이 삭제되는건데 댓글을 전체 삭제 해야하니깐.그것도 여쭤봐야함
        Coment coment = comentRepo.findBySeq(seq);
        coment.setDel(true);//삭제여부를 삭제된걸로 체크.
        return comentRepo.save(coment);
    }
    //좋아요 누르고 . 때는경우
    public void likeBoard(ParamVO params) {
        Board board = boardRepo.findBySeq(params.getSeq());//boarseq를 찾아서 board에 담아줌.
        Like like = likeRepo.findByBoardAndUser(board, AuthUtil.getLoginVO());//내가 좋아요 누른게 있는지 없는지 체크
        if(Objects.isNull(like)){//좋아요가 안되있을때
            like = Like.builder()
                    .board(board)
                    .user(AuthUtil.getLoginVO())
                    .build();
            likeRepo.save(like);
        }else{
            likeRepo.deleteById(like.getSeq());//테이블에있는 데이터를 삭제
        }
    }

    public long getBoardCount(Board board){
        return likeRepo.countByBoard(board);
    }

    public boolean isMyLike(Board board){
        return Objects.nonNull(likeRepo.findByBoardAndUser(board,AuthUtil.getLoginVO()));
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
//        board.setUser(user);//user시퀀스 값넣음.
//        return boardRepo.save(Coment);
//
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