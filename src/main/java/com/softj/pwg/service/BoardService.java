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
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import javax.xml.stream.events.Comment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final JPAQueryFactory jpaQueryFactory;

    private final BoardRepo boardRepo;
    private final ComentRepo comentRepo;
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;

    @Value("${file.uploadDir}")
    private String FILE_PATH;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public List<Board> noticeList(ParamVO params, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        QComent qComent = QComent.coment;
        BooleanBuilder where = new BooleanBuilder(qBoard.isDel.eq(false))
                .and(qBoard.nation.eq(Long.parseLong(String.valueOf(AuthUtil.getAttr("nation")))))
                .and(qBoard.isNotice.eq(true));
        JPAQuery<Board> query = jpaQueryFactory.select(Projections.fields(Board.class,
                qBoard.seq,
                qBoard.thumbnail,
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
                        JPAExpressions.select(qComent.count())
                                .from(qComent)
                                .where(qComent.board.eq(qBoard)),"likeCount"))
        )
                .from(qBoard)
                .where(where)
                .orderBy(qBoard.seq.desc())
                .limit(pageable.getPageSize())//????????? ?????? ??????
                .offset(pageable.getOffset());//??????index??????offset

        return query.fetch();
    }

    public Page<Board> boardList(ParamVO params, Pageable pageable) {
        QBoard qBoard = QBoard.board; //????????? ????????????
        QComent qComent = QComent.coment;
        QLike qLike = QLike.like;
        User user = (User)AuthUtil.getAttr("loginVO");

        BooleanBuilder where = new BooleanBuilder(qBoard.isNotice.eq(false));//where?????? ??????????????? ????????? ?????? ???????????? ?????? ????????? ????????? ??? ??????.
        where.and(qBoard.nation.eq(Long.parseLong(String.valueOf(AuthUtil.getAttr("nation"))))); //?????? ???????????? ?????? ????????????.
        where.and(qBoard.isDel.eq(false));//?????? ????????? ???????????? 0?????????
        if (!StringUtils.isEmpty(params.getSearch())) {//???????????? ????????? LIKE ??????
            where.and(qBoard.subject.contains(params.getSearch()).or(qBoard.content.contains(params.getSearch())));
        }
        if (params.isMyWritePage()) {//???????????? ?????? (??????????????? ????????????)
            where.and(qBoard.user.eq((User)AuthUtil.getAttr("loginVO")));
        }
        if (params.isMyLikePage()) {//(????????? ?????????????????? jpql??? ???????????????.)
            where.and(JPAExpressions.selectFrom(qLike).where(qLike.board.eq(qBoard).and(qLike.user.eq(user))).exists());
        }

        JPAQuery<Board> query = jpaQueryFactory.select(Projections.fields(Board.class,
                                    qBoard.seq,
                                    qBoard.thumbnail,
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
                                        JPAExpressions.select(qComent.count())
                                                .from(qComent)
                                                .where(qComent.board.eq(qBoard).and(qComent.isDel.eq(false))),"likeCount"))
                                )
                                .from(qBoard)
                                .where(where)
                                .orderBy(qBoard.seq.desc())
                                .limit(pageable.getPageSize())//????????? ?????? ??????
                                .offset(pageable.getOffset());//??????index??????offset

        return new PageImpl<Board>(query.fetch(), pageable, query.fetchCount());

    }

    //?????????
    public Board boardView(ParamVO params) { //??? ?????? ???????????? ?????????
        QBoard qBoard = QBoard.board; //????????? ????????????
        QComent qComent = QComent.coment;

        BooleanBuilder where = new BooleanBuilder(qBoard.seq.eq(params.getSeq()));

        JPAQuery<Board> query = jpaQueryFactory.select(Projections.fields(Board.class,
                qBoard.seq,
                qBoard.thumbnail,
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
                qBoard.isNotice,
                ExpressionUtils.as(
                        JPAExpressions.select(qComent.count())
                                .from(qComent)
                                .where(qComent.board.eq(qBoard).and(qComent.isDel.eq(false))),"likeCount"))
        )
        .from(qBoard)
        .where(where);

        Board board = query.fetchFirst();
        board.increaseViewCount();
        boardRepo.save(board);
        return board;
    }

    public Page<Coment> boardComent(ParamVO params,Pageable pageable) {
        QComent qComent=QComent.coment;
        QBoard qBoard=QBoard.board;
        QUser qUser=QUser.user;

        User user = (User)AuthUtil.getAttr("loginVO");
        BooleanBuilder where = new BooleanBuilder();//where?????? ??????????????? ????????? ?????? ???????????? ?????? ????????? ????????? ??? ??????.
        where.and(qComent.isDel.eq(false));//?????? ????????? ???????????? 0?????????.

        if(params.isMyCommentPage()) { //mypage?????? ????????????
            where.and(qBoard.nation.eq(Long.parseLong(String.valueOf(AuthUtil.getAttr("nation"))))); //?????? ???????????? ?????? ????????????.
            where.and(qBoard.user.eq(user));
            where.and(qComent.user.ne(user));
            where.and(qComent.upperSeq.isNull().or(qComent.upperSeq.eq(0L)));
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
                    .orderBy(qComent.seq.asc())//??????
                    .limit(pageable.getPageSize())//????????? ?????? ??????
                    .offset(pageable.getOffset());//??????index??????offset
            List<Coment> list = query.fetch();
            list.forEach(el -> {
                el.setChildren(StreamSupport.stream(comentRepo.findAll(new BooleanBuilder().and(qComent.isDel.eq(false).and(qComent.upperSeq.eq(el.getSeq())))).spliterator(), false).collect(Collectors.toList()));
            });
            return new PageImpl<Coment>(list, pageable, query.fetchCount());
        }else{
            where.and(qComent.board.seq.eq(params.getSeq()));//?????????????????? ????????? ???????????? ?????????.
            where.and(qComent.upperSeq.isNull().or(qComent.upperSeq.eq(0L)));
        }

        JPAQuery<Coment> query = jpaQueryFactory
                .selectFrom(qComent)
                .join(qComent.user)
                .fetchJoin()
                .where(where)
                .orderBy(qComent.seq.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());
        List<Coment> list = query.fetch();
        list.forEach(el -> {
            el.setChildren(StreamSupport.stream(comentRepo.findAll(new BooleanBuilder().and(qComent.isDel.eq(false).and(qComent.upperSeq.eq(el.getSeq())))).spliterator(), false).collect(Collectors.toList()));
        });
        return new PageImpl<Coment>(list, pageable, query.fetchCount());
    }


    public Board boardWrite(ParamVO params) {
        //????????? ????????? ????????? ????????? ?????? select??? ?????? ?????????.
        Board board = Board.builder().build();
        if(params.getSeq() != 0){
            board = boardRepo.findBySeq(params.getSeq());
        }

        int idx = params.getContent().indexOf("src=\"");
        if(idx != -1){
            board.setThumbnail(params.getContent().substring(idx+5, params.getContent().indexOf(".do\"", idx)+3));
        }

        board.setSubject(params.getSubject());
        board.setContent(params.getContent());
        board.setNation(Long.parseLong(String.valueOf(AuthUtil.getAttr("nation"))));
        board.setUser((User) AuthUtil.getAttr("loginVO"));

        return boardRepo.save(board);

    }
    public Coment comentWrite(ParamVO params){//?????? ????????? ?????? board??? ??????
        Board board = Board.builder()
                .build(); //?????? ????????? ????????? ??????.
        board.setSeq(params.getBoardSeq()); //????????? ?????????.
        Coment coment = Coment.builder()
                .isSecret(params.isSecret())
                .user((User) AuthUtil.getAttr("loginVO"))
                .upperSeq(params.getUpperSeq())
                .build();
        if(params.getSeq() != 0){
            coment = comentRepo.findBySeq(params.getSeq());
        }
        coment.setContent(params.getContent());
        coment.setBoard(board);
        return comentRepo.save(coment);
    }

    //???????????????
    public Board deleteBoard(long seq){//board
        Board board =boardRepo.findBySeq(seq);//????????? ??? ??? ?????? ?????? ????????? ?????? ????????? ?????????.
        board.setDel(true);
        return boardRepo.save(board);
    }
    //????????????
    public Coment deleteComment(long seq){ //?????? ???????????? ??? ????????? ?????????????????? ????????? ?????? ?????? ???????????????.????????? ???????????????
        Coment coment = comentRepo.findBySeq(seq);
        coment.setDel(true);//??????????????? ??????????????? ??????.
        return comentRepo.save(coment);
    }
    //????????? ????????? . ????????????
    public void likeBoard(ParamVO params) {
        Board board = boardRepo.findBySeq(params.getSeq());//boarseq??? ????????? board??? ?????????.
        Like like = likeRepo.findByBoardAndUser(board, AuthUtil.getLoginVO());//?????? ????????? ????????? ????????? ????????? ??????
        if(Objects.isNull(like)){//???????????? ???????????????
            like = Like.builder()
                    .board(board)
                    .user(AuthUtil.getLoginVO())
                    .build();
            likeRepo.save(like);
        }else{
            likeRepo.deleteById(like.getSeq());//?????????????????? ???????????? ??????
        }
    }

    public long getBoardCount(Board board){
        return likeRepo.countByBoard(board);
    }

    public boolean isMyLike(Board board){
        boolean isMyLike = false;
        try{
            isMyLike = Objects.nonNull(likeRepo.findByBoardAndUser(board,AuthUtil.getLoginVO()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return isMyLike;
    }

    public List<String> fileUpload(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        List<String> result = new ArrayList<>();

        List<MultipartFile> imgList = request.getFiles("file");
        for(MultipartFile img : imgList) {
            if (!Objects.isNull(img)) {
                String imagePath = null;
                String realFileName = img.getOriginalFilename();
                String ext = realFileName.substring(realFileName.lastIndexOf(".") + 1);
                String systemFileName = UUID.randomUUID().toString().toUpperCase() + "." + ext;

                String targetPath = FILE_PATH + sdf.format(new Date()) + "/";
                File targetDir = new File(targetPath);

                //?????? ??????
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                FileUtils.writeByteArrayToFile(new File(targetPath + systemFileName), img.getBytes());

                imagePath = targetPath + systemFileName;
                imagePath = imagePath.replace("/", "_");
                result.add(imagePath);
            }
        }

        return result;
    }

    public Board setNoticeBoard(ParamVO params){
        Board save = boardRepo.findBySeq(params.getSeq());
        save.setNotice(!save.isNotice());
        return boardRepo.save(save);
    }
}