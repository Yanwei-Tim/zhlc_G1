package com.zhlt.g1app.basefunc;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;


import android.renderscript.Element;
import android.util.Xml;

public class ParseXmlService {
	public HashMap<String, String> parseXml(InputStream inStream) throws Exception
    { 
		HashMap<String, String>   map=new HashMap<String, String>();	
	XmlPullParser  parser = Xml.newPullParser();    
    parser.setInput(inStream, "utf-8");//设置解析的数据源   
    int type = parser.getEventType();  
   
    while(type != XmlPullParser.END_DOCUMENT ){  
        switch (type) {  
        case XmlPullParser.START_TAG:  
            if("version".equals(parser.getName())){  
            	map.put("version", parser.nextText()); //获取版本号  
            }else if ("url".equals(parser.getName())){  
            	map.put("url", parser.nextText()); //获取要升级的APK文件  
            }else if ("name".equals(parser.getName())){  
            	map.put("name", parser.nextText()); //获取该文件的信息  
            }  else if ("msg".equals(parser.getName())){  
            	map.put("msg", parser.nextText()); //获取该文件的信息  
            } 
            break;  
        }  
        type = parser.next();  
    }  
    return map;  
    }
}
