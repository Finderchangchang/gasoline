package com.app.bulkgasoline.model;

/// <summary>
/// 企业基本模型。
/// </summary>
public class CompanyModel {
    // / <summary>
    // / 企业编码
    // / </summary>
    public String CompanyId;
    // / <summary>
    // / 密码。
    // / </summary>
    public String Password;

    // / <summary>
    // / 所属辖区
    // / </summary>
    public String DepartmentId;

    // / <summary>
    // / 企业名称
    // / </summary>
    public String CompanyName;

    // / <summary>
    // / 企业地址
    // / </summary>
    public String CompanyAddress;

    // / <summary>
    // / 企业状态
    // / </summary>
    public String CompanyStatus;

    // / <summary>
    // / 经度
    // / </summary>
    public Double NorthPoint;

    // / <summary>
    // / 纬度
    // / </summary>
    public Double EastPoint;

    // / <summary>
    // /
    // / </summary>
    public String Leader;
    // / <summary>
    // / 负责人证件类型
    // / </summary>
    public String LeaderIdentityType;

    // / <summary>
    // / 负责人证件号
    // / </summary>
    public String LeaderIdentityNumber;

    // / <summary>
    // / 负责人联系方式
    // / </summary>
    public String LeaderLinkway;

    // / <summary>
    // / 负责人地址
    // / </summary>
    public String LeaderAddress;

    // / <summary>
    // / 法人姓名
    // / </summary>
    public String Boss;

    // / <summary>
    // / 法人证件类型
    // / </summary>
    public String BossIdentityType;

    // / <summary>
    // / 法人证件号
    // / </summary>
    public String BossIdentityNumber;

    // / <summary>
    // / 法人联系方式
    // / </summary>
    public String BossLinkway;

    // / <summary>
    // / 法人地址
    // / </summary>
    public String BossAddress;

    // / <summary>
    // / 监控系统（套）
    // / </summary>
    public int SupervisingSystem;

    // / <summary>
    // / 探头（个）
    // / </summary>
    public int CameraCount;

    // / <summary>
    // / 报警系统
    // / </summary>
    public int AlarmCount;

    // / <summary>
    // / 守护力量（人）
    // / </summary>
    public int GuardCount;

    // / <summary>
    // / 责任民警
    // / </summary>
    public String PoliceId;

    // / <summary>
    // / 创建用户（申请人）
    // / </summary>
    public String CreateUser;

    // / <summary>
    // / 创建时间
    // / </summary>
    public String CreateTime;

    // / <summary>
    // / 修改时间
    // / </summary>
    public String ChangeTime;

    // / <summary>
    // / 备注
    // / </summary>
    public String Comment;
}
