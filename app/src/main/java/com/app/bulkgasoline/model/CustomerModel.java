package com.app.bulkgasoline.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

import com.app.bulkgasoline.utils.Utils;

/// <summary>
/// 销售实名登记模型。
/// </summary>

public class CustomerModel implements Serializable {

    private static final long serialVersionUID = -8514182941135590201L;

    // / <summary>
    // / 购买者编码
    // / </summary>
    public String CustomerId = "";

    // / <summary>
    // / 企业编码
    // / </summary>
    public String CompanyId = "";
    // / <summary>
    // / 所属辖区
    // / </summary>
    public String DepartmentId = "";

    // / <summary>
    // / 购买者姓名
    // / </summary>
    public String CustomerName = "";

    // / <summary>
    // / 购买者证件类型
    // / </summary>
    public String CustomerIdentityType = "";

    // / <summary>
    // / 购买者证件号码
    // / </summary>
    public String CustomerIdentityNumber = "";

    // / <summary>
    // / 购买者性别
    // / </summary>
    public String CustomerSex = "";

    // / <summary>
    // / 购买者籍贯
    // / </summary>
    public String CustomerNative = "";

    // / <summary>
    // / 购买者民族
    // / </summary>
    public String CustomerNation = "";

    // / <summary>
    // / 购买者出生日期
    // / </summary>
    public String CustomerBirthday = "";

    // / <summary>
    // / 购买者地址
    // / </summary>
    public String customerAddress = "";

    // / <summary>
    // / 购买企业
    // / </summary>
    public String CustomerCompany = "";

    // / <summary>
    // / 购买者联系方式
    // / </summary>
    public String CustomerLinkway = "";

    // / <summary>
    // / 是否可疑
    // / </summary>
    public String Suspicious = "";

    // / <summary>
    // / 可疑原因
    // / </summary>
    public String SuspiciousReson = "";

    // / <summary>
    // / 油品种类
    // / </summary>
    public String GasolineType = "";

    // / <summary>
    // / 运输方式
    // / </summary>
    public String TransportType = "";

    // / <summary>
    // / 车牌号
    // / </summary>
    public String VehicleNumber = "";

    // / <summary>
    // / 购买用途
    // / </summary>
    public String Purpose = "";

    // / <summary>
    // / 购买汽油数量
    // / </summary>
    public String Count = "";

    // / <summary>
    // / 购买者头像或证件图片编码
    // / </summary>
    public String CustomerImageId = "";

    // / <summary>
    // /购买者头像或证件图片编码。
    // / </summary>
    public String CustomerImage = "";

    // / <summary>
    // / 保存身份证头像
    // / </summary>
    public Bitmap CustomerHeder;

    public String CustomerImages;

    public ArrayList<Bitmap> CustomerImageBitmaps;

    // / <summary>
    // / 从业人员编码
    // / </summary>
    public String EmployeeId = "";

    // / <summary>
    // / 创建时间
    // / </summary>
    public String CreateTime = "";

    // / <summary>
    // / 创建IP
    // / </summary>
    public String CreateIP = "";

    // / <summary>
    // / 备注
    // / </summary>
    public String Comment = "";

    public String Serialization() {
        String xml = "<?xml version=\"1.0\"?>\n";
        xml += String
                .format("<%s xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n",
                        "CustomerModel");
        xml += String.format("  <CustomerId>%s</CustomerId>\n",
                Utils.XMlEncode(CustomerId));
        xml += String.format("  <GasolineType>%s</GasolineType>\n",
                Utils.XMlEncode(GasolineType));
        xml += String.format("  <TransportType>%s</TransportType>\n",
                Utils.XMlEncode(TransportType));
        xml += String.format(
                "  <SuspiciousVehicleNumber>%s</SuspiciousVehicleNumber>\n",
                Utils.XMlEncode(VehicleNumber));
        xml += String.format("  <SuspiciousReson>%s</SuspiciousReson>\n",
                Utils.XMlEncode(SuspiciousReson));
        xml += String.format("  <CompanyId>%s</CompanyId>\n",
                Utils.XMlEncode(CompanyId));
        xml += String.format("  <DepartmentId>%s</DepartmentId>\n",
                Utils.XMlEncode(DepartmentId));
        xml += String.format("  <CustomerName>%s</CustomerName>\n",
                Utils.XMlEncode(CustomerName));
        xml += String.format(
                "  <CustomerIdentityType>%s</CustomerIdentityType>\n",
                Utils.XMlEncode(CustomerIdentityType));
        xml += String.format(
                "  <CustomerIdentityNumber>%s</CustomerIdentityNumber>\n",
                Utils.XMlEncode(CustomerIdentityNumber));
        xml += String.format("  <CustomerSex>%s</CustomerSex>\n",
                Utils.XMlEncode(CustomerSex));
        xml += String.format("  <CustomerNative>%s</CustomerNative>\n",
                Utils.XMlEncode(CustomerNative));
        xml += String.format("  <CustomerNation>%s</CustomerNation>\n",
                Utils.XMlEncode(CustomerNation));
        xml += String.format("  <CustomerBirthday>%s</CustomerBirthday>\n",
                Utils.XMlEncode(CustomerBirthday));
        xml += String.format("  <customerAddress>%s</customerAddress>\n",
                Utils.XMlEncode(customerAddress));
        xml += String.format("  <CustomerCompany>%s</CustomerCompany>\n",
                Utils.XMlEncode(CustomerCompany));
        xml += String.format("  <CustomerLinkway>%s</CustomerLinkway>\n",
                Utils.XMlEncode(CustomerLinkway));
        xml += String.format("  <Suspicious>%s</Suspicious>\n",
                Utils.XMlEncode(Suspicious));
        xml += String.format("  <SuspiciousReson>%s</SuspiciousReson>\n",
                Utils.XMlEncode(SuspiciousReson));
        xml += String.format("  <Purpose>%s</Purpose>\n",
                Utils.XMlEncode(Purpose));
        xml += String.format("  <Count>%s</Count>\n", Utils.XMlEncode(Count));

        // 头像
        if (CustomerHeder != null) {
            xml += String.format("  <CustomerImage>%s</CustomerImage>\n",
                    Utils.XMlEncode(Utils.encodeBitmap(CustomerHeder)));
        }

        // 添加多个图片
        if (CustomerImageBitmaps != null && CustomerImageBitmaps.size() > 0) {
            xml += "  <CustomerPhotos>\n";
            for (int i = 0; i < CustomerImageBitmaps.size(); i++) {
                Bitmap bitmap = CustomerImageBitmaps.get(i);
                if (bitmap != null) {
                    xml += "    <BinaryDataModel>\n";
                    xml += String.format("      <BinaryData>%s</BinaryData>\n",
                            Utils.XMlEncode(Utils.encodeBitmap(bitmap)));
                    xml += String.format("      <CreateTime>%s</CreateTime>\n",
                            Utils.XMlEncode(Utils.getCSTimeString()));
                    xml += "    </BinaryDataModel>\n";
                }
            }
            xml += "  </CustomerPhotos>\n";
        }

        xml += String.format("  <EmployeeId>%s</EmployeeId>\n",
                Utils.XMlEncode(EmployeeId));
        xml += String.format("  <CompanyId>%s</CompanyId>\n",
                Utils.XMlEncode(CompanyId));
        xml += String.format("  <CreateTime>%s</CreateTime>\n",
                Utils.XMlEncode(CreateTime));
        xml += String.format("  <Comment>%s</Comment>\n",
                Utils.XMlEncode(Comment));

        xml += String.format("</%s>", "CustomerModel");
        return xml;
    }

}
