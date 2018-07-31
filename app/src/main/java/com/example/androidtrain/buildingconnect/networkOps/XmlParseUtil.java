package com.example.androidtrain.buildingconnect.networkOps;

import android.content.IntentFilter;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lizz on 2018/7/31.
 */

public class XmlParseUtil {

    //We don't use namespace
    private static final String ns = null;

    public List pase(InputStream in) throws XmlPullParserException, IOException{
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            //调用nextTag来开始解析工作
            parser.nextTag();
            return readFeed(parser);
        }finally {
            in.close();
        }
    }

    //跳过不是entry的标签，并返回一个从feed中提取的包含了entry标签的内容
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
        List entries = new ArrayList();

        //判断此时的标签是否是开始节点；标签名称是否为feed；判断命名空间是否为ns（此处ns为null，遂不进行判断）
        //如果不是，则抛出Exception
        parser.require(XmlPullParser.START_TAG, ns, "feed");

        //上面已经确定为开始节点；
        //步骤A:然后调用next()方法，跳到下一步。如果下一步为结束节点，则与上一节点呼应。表示无内容，直接返回。
        //如果下一步不是结束节点，则进行下面的判断：
        //判断此处节点是否为开始节点，如果不是，则调用next(),重复步骤A；
        //如果是开始节点，则判断是否改标签为entry标签，如果是，则读取标签内的内容；不是则跳过。
        //反正目标就是读取所有的entry内容
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            //Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            }else {
                skip(parser);
            }
        }
        return entries;
    }



    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG){
             throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0){
            switch (parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    //解析一个Entry里面的内容，如果他包含title/summary/link标签，就读取该标签里的内容，否则就跳过。
    //请参照readFeed方法内的注释理解。
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String summary = null;
        String link = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")){
                title = readTitle(parser);
            }else if (name.equals("summary")){
                summary = readSummary(parser);
            }else if (name.equals("link")){
                link = readLink(parser);
            }else {
                skip(parser);
            }
        }
        return new Entry(title, summary, link);
    }

    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "summary");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "summary");
        return summary;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
        String result = "";
        if (parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readLink(XmlPullParser parser)throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(ns, "rel");
        if (tag.equals("link")){
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(ns, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "title");

        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    public static class Entry{
        public final String title;
        public final String link;
        public final String summary;

        private Entry(String title, String summary, String link){
            this.title = title;
            this.summary = summary;
            this.link = link;
        }
    }
}
