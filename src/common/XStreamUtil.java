/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import MATERIAL.Material;
import com.thoughtworks.xstream.XStream;
import javax.mail.Message;

/**
 *
 * @author Administrator
 */
public class XStreamUtil {

    private static XStream xstream = new XStream();

    public static Material MaterialFromXML(String xml) {
        xstream.alias("material", Material.class);
        Material material = (Material) xstream.fromXML(xml);
        return material;
    }

    public static String materialToXML(Material material) {
        xstream.alias("material", Material.class);
        String xml = xstream.toXML(material);
        xml = xml.replaceAll("\n", "");
        xml = xml.replaceAll("\r", "");
        return xml;
    }

    public static SetInfo SetInfoFromXML(String xml) {
        xstream.alias("setInfo", SetInfo.class);
        SetInfo setInfo = (SetInfo) xstream.fromXML(xml);
        return setInfo;
    }

    public static String SetInfoToXML(SetInfo setInfo) {
        xstream.alias("setInfo", SetInfo.class);
        String xml = xstream.toXML(setInfo);
//        xml = xml.replaceAll("\n", "");
//        xml = xml.replaceAll("\r", "");
        return xml;
    }

    public static OptimizationData OptimizationDataFromXML(String xml) {
        xstream.alias("OptimizationData", OptimizationData.class);
        OptimizationData data = (OptimizationData) xstream.fromXML(xml);
        return data;
    }

    public static String OptimizationDataToXML(OptimizationData data) {
        xstream.alias("OptimizationData", OptimizationData.class);
        String xml = xstream.toXML(data);
        return xml;
    }

}
