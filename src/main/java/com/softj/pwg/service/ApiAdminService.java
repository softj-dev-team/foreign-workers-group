package com.softj.pwg.service;

import com.google.common.hash.Hashing;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softj.pwg.entity.*;
import com.softj.pwg.repo.*;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiAdminService {
    private final JPAQueryFactory jpaQueryFactory;

    private final BoardRepo boardRepo;
    private final ComentRepo comentRepo;
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;
    private final NationRepo nationRepo;
    private final IpBlockRepo ipBlockRepo;

    @Value("${file.uploadDir}")
    private String FILE_PATH;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public User adminLogin(ParamVO params) {
        User u = userRepo.findFirstById(params.getId());
        if(Objects.isNull(u)){
            return null;
        }
        if(u.isAdmin() && u.getPassword().equals(Hashing.sha256().hashString(params.getPassword(), StandardCharsets.UTF_8).toString())){
            AuthUtil.setAttr("adminVO",u);
            return u;
        }
        return null;
    }
    public Nation togglePublic(ParamVO params) {
        Nation find = nationRepo.findById(params.getSeq()).get();
        find.setPublic(!find.isPublic());
        return nationRepo.save(find);
    }
    public Nation updateSort(ParamVO params) {
        Nation find = nationRepo.findById(params.getSeq()).get();
        Nation target = null;
        if(params.getToggle().equals("up")) {
            target = nationRepo.findBySort(find.getSort()-1);
            find.setSort(find.getSort()-1);
            target.setSort(target.getSort()+1);
        }else{
            target = nationRepo.findBySort(find.getSort()+1);
            find.setSort(find.getSort()+1);
            target.setSort(target.getSort()-1);
        }
        nationRepo.save(target);
        return nationRepo.save(find);
    }

    public Nation saveNation(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        Nation entity = Nation.builder()
                .sort(nationRepo.maxSort())
                .build();
        if(params.getSeq() != 0){
            entity = nationRepo.findById(params.getSeq()).get();
        }
        MultipartFile img = request.getFile("image");
        if(!Objects.isNull(img) && !StringUtils.isEmpty(img.getOriginalFilename())) {
            String imagePath = null;
            String realFileName = img.getOriginalFilename();
            String ext = realFileName.substring(realFileName.lastIndexOf(".") + 1);
            String systemFileName = UUID.randomUUID().toString().toUpperCase() + "." + ext;

            String targetPath = FILE_PATH + sdf.format(new Date())+"/";
            File targetDir = new File(targetPath);

            //폴더 생성
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            FileUtils.writeByteArrayToFile(new File(targetPath + systemFileName), img.getBytes());

            imagePath = targetPath+systemFileName;
            imagePath = imagePath.replace("/","_");

            entity.setThumbnail("/api/comFileDownload/"+imagePath+".do");
        }
        entity.setName(params.getName());
        entity.setPublic(params.getIsPublic().equals("Y"));
        return nationRepo.save(entity);
    }

    public User toggleUserBlock(ParamVO params){
        User find = userRepo.findById(params.getSeq()).get();
        find.setBlock(!find.isBlock());
        return userRepo.save(find);
    }
    @Transactional
    public void deleteUsers(ParamVO params){
        for(long seq:params.getSeqList()){
            userRepo.deleteById(seq);
        }
    }
    @Transactional
    public void deleteIps(ParamVO params){
        for(long seq:params.getSeqList()){
            ipBlockRepo.deleteById(seq);
        }
    }
    public IpBlock saveIp(ParamVO paramVO){
        IpBlock entity = IpBlock.builder().build();
        if(paramVO.getSeq() != 0){
            entity = ipBlockRepo.findById(paramVO.getSeq()).get();
        }
        entity.setIp(paramVO.getIp());
        return ipBlockRepo.save(entity);
    }
}