package com.app.bulkgasoline.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

import com.app.bulkgasoline.utils.Utils;

/// <summary>
/// 车辆信息模型。
/// </summary>
public class VehicleModel extends BaseModel implements Serializable{

    private static final long serialVersionUID = -7391347181922385466L;

    // / <summary>
    // / 车辆编码
    // / </summary>
    public String VehicleId = "";

    // / <summary>
    // / 车辆类型
    // / </summary>
    public String VehicleType = "";

    // / <summary>
    // / 车辆颜色
    // / </summary>
    public String VehicleColor = "";
    // / <summary>
    // / 车牌号
    // / </summary>
    public String VehicleNumber = "";

    // / <summary>
    // / 车内人数
    // / </summary>
    public String VehiclePersonCount = "";

    // / <summary>
    // / 车辆去向
    // / </summary>
    public String VehicleDirection = "";

    // / <summary>
    // / 加油员
    // / </summary>
    public String EmployeeId = "";

    // / <summary>
    // / 车辆图片
    // / </summary>
    public ArrayList<Bitmap> VehicleImageBitmaps;
    public String VehicleImages;

    // / <summary>
    // / 加油站
    // / </summary>
    public String CompanyId = "";

    // / <summary>
    // / 创建时间
    // / </summary>
    public String CreateTime = "";

    // / <summary>
    // / 备注
    // / </summary>
    public String Comment = "";

    public String Serialization()
    {
        String xml = "<?xml version=\"1.0\"?>\n";

        xml += String.format("<%s xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n", "VehicleModel");

        xml += String.format("  <VehicleId>%s</VehicleId>\n",					Utils.XMlEncode(VehicleId));
        xml += String.format("  <VehicleType>%s</VehicleType>\n",				Utils.XMlEncode(VehicleType));
        xml += String.format("  <VehicleColor>%s</VehicleColor>\n", 			Utils.XMlEncode(VehicleColor));
        xml += String.format("  <VehicleNumber>%s</VehicleNumber>\n",			Utils.XMlEncode(VehicleNumber));
        xml += String.format("  <VehiclePersonCount>%s</VehiclePersonCount>\n",	Utils.XMlEncode(VehiclePersonCount));
        xml += String.format("  <VehicleDirection>%s</VehicleDirection>\n",		Utils.XMlEncode(VehicleDirection));
        xml += String.format("  <EmployeeId>%s</EmployeeId>\n", 				Utils.XMlEncode(EmployeeId));
        xml += String.format("  <CompanyId>%s</CompanyId>\n",					Utils.XMlEncode(CompanyId));

        // 添加多个图片
        if(VehicleImageBitmaps != null && VehicleImageBitmaps.size() > 0)
        {
            xml += "  <VehiclePhotos>\n";
            for(int i = 0; i < VehicleImageBitmaps.size(); i++)
            {
                Bitmap bitmap = VehicleImageBitmaps.get(i);
                if(bitmap != null)
                {
                    xml += "    <BinaryDataModel>\n";
                    xml += String.format("      <BinaryData>%s</BinaryData>\n", Utils.XMlEncode(Utils.encodeBitmap(bitmap)));
                    xml += String.format("      <CreateTime>%s</CreateTime>\n", Utils.XMlEncode(Utils.getCSTimeString()));
                    xml += "    </BinaryDataModel>\n";
                }
            }
            xml += "  </VehiclePhotos>\n";
        }

        xml += String.format("  <CreateTime>%s</CreateTime>\n",					Utils.XMlEncode(CreateTime));
        xml += String.format("  <Comment>%s</Comment>\n",						Utils.XMlEncode(Comment));

        xml += String.format("</%s>", "VehicleModel");
        return xml;
    }

}
