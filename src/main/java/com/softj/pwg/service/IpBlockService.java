package com.softj.pwg.service;

import com.querydsl.core.BooleanBuilder;
import com.softj.pwg.entity.IpBlock;
import com.softj.pwg.entity.QIpBlock;
import com.softj.pwg.entity.QUser;
import com.softj.pwg.entity.User;
import com.softj.pwg.repo.IpBlockRepo;
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
public class IpBlockService {
    private final IpBlockRepo ipBlockRepo;

    @Value("${file.uploadDir}")
    private String FILE_PATH;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public Page<IpBlock> getIpList(ParamVO params, Pageable p) {
        QIpBlock qEntity = QIpBlock.ipBlock;
        QSort qSort = new QSort(qEntity.seq.desc());
        BooleanBuilder where = new BooleanBuilder();
        if(!StringUtils.isEmpty(params.getIp())){
            where.and(qEntity.ip.contains(params.getIp()));
        }

        return ipBlockRepo.findAll(where, PageRequest.of(p.getPageNumber(),p.getPageSize(),qSort));
    }
}
