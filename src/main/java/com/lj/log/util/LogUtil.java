package com.lj.log.util;

import com.lj.log.model.IbeLog;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class LogUtil {
    public final static String LOG_PATH = "D:/ibs_log/service.log";

    public final static List<IbeLog> getLogList(){
        List<IbeLog> result = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(LOG_PATH), StandardCharsets.UTF_8)) {
            lines.forEach(obj->{
                JSONObject objJson = parseJson(obj);
                IbeLog log = new IbeLog();
                log.setCsid(objJson.get("CSID").toString());
                log.setMessage(Objects.isNull(objJson.get("RS_SOAP"))?toPrettyString(objJson.get("RQ_SOAP").toString(),4):toPrettyString(objJson.get("RS_SOAP").toString(),4));
                result.add(log);
            });
            Collections.reverse(result);
        } catch (IOException e) {
            IbeLog log = new IbeLog();
            log.setMessage("파일 로드에 실패 했습니다.");
            result.add(log);
        }
        return result;
    }

    public final static JSONObject parseJson(String jsonString){
        JSONObject param = new JSONObject();
        try{
            JSONParser parse = new JSONParser();
            Object obj = parse.parse(jsonString);
            param = (JSONObject) obj;
        }catch(Exception e){

        }
        return param;
    }

    public static String toPrettyString(String xml, int indent) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                    document,
                    XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                node.getParentNode().removeChild(node);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);

            transformer.transform(new DOMSource(document), streamResult);

            return stringWriter.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] getMD5Checksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[1024];
        int aux;
        do {
            aux = fis.read(buffer);
            if (aux > 0) {
                md.update(buffer, 0, aux);
            }
        } while (aux != -1);
        fis.close();
        return md.digest();
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    public static String checkFile(){
        String result = "";
        try{
            result = toHexString(getMD5Checksum(LOG_PATH));
        }catch(Exception e){

        }
        return result;
    }
}
