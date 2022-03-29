package com.softj.pwg.service;

import com.querydsl.core.BooleanBuilder;
import com.softj.pwg.entity.QUser;
import com.softj.pwg.entity.User;
import com.softj.pwg.repo.UserRepo;
import com.softj.pwg.util.AuthUtil;
import com.softj.pwg.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    @Value("${file.uploadDir}")
    private String FILE_PATH;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public User login(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        //1.아이디 플랫폼 조회하기(가입여부)
        User user = null; //가입여부만확인함
        if(!StringUtils.isEmpty(params.getPlatform())) { //페이스북이면 아이디랑 플랫폼가져와서
            user = userRepo.findFirstByIdAndPlatform(params.getId(), params.getPlatform());//아이디 플랫폼 넣어서 조회
        }
        if(Objects.isNull(user)){ //가입이 안되어 있으면 user
            String imagePath = null;
            MultipartFile img = request.getFile("image");
            if(!Objects.isNull(img) && !StringUtils.isEmpty(img.getOriginalFilename())) {
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
            }

            user = userRepo.save(User.builder()
                    .nickname(params.getNickname())
                    .id(params.getId()) //아이디
                    .platform(params.getPlatform())//플렛폼.
                    .image(imagePath)//플렛폼.
                    .build());//쿼리문날라가고

        }

        if(user.isBlock()) {
            return user;
        }

        AuthUtil.setAttr("loginVO", user); //세션에 아이디랑 플렛폼 저장 처음에 초기값은 0,1로 되어있음
         //save하는데 안에서 user만들고 닉네임 넣었고 그걸 바로 세이브시켜라.
        return user;
    }

    public User setNickName(ParamVO params, MultipartHttpServletRequest request) throws Exception{
        User user = (User)AuthUtil.getAttr("loginVO");//세션에 저장된 값을 가져옴loginvo
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
            user.setImage(imagePath);
        }
        user.setNickname(params.getNickname());
        return userRepo.save(user);
    }

    public Page<User> getUserList(ParamVO params, Pageable p) {
        QUser qEntity = QUser.user;
        QSort qSort = new QSort(qEntity.seq.desc());
        BooleanBuilder where = new BooleanBuilder(qEntity.isAdmin.eq(false))
                .and(qEntity.platform.isNotNull());
        if(!StringUtils.isEmpty(params.getNickname())){
            where.and(qEntity.nickname.contains(params.getNickname()));
        }

        return userRepo.findAll(where, PageRequest.of(p.getPageNumber(),p.getPageSize(),qSort));
    }
}
