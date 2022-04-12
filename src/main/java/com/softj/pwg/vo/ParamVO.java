package com.softj.pwg.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class ParamVO {
    private long seq;
    private long[] seqList;
    private long boardSeq;
    private long userSeq;
    private String isPublic;
    private String toggle;
    private String ip;
    private String name;
    private String nickname;
    private String password;
    private String platform;
    private String id;
    private String nation;
    private String nationName;
    private String content;
    private String subject;
    private long views;
    private String search;
    private boolean isMyWritePage;
    private boolean isMyCommentPage;
    private boolean isMyLikePage;
    private boolean isSecret;
    private long upperSeq;
}
