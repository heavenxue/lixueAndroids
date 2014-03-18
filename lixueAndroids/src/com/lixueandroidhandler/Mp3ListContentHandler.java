package com.lixueandroidhandler;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lixueandroid.domain.Mp3Info;


public class Mp3ListContentHandler extends DefaultHandler{
	private List<Mp3Info> infos;
	private Mp3Info mp3info;
	private String TagName;

	public Mp3ListContentHandler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		this.TagName=localName;
		if(TagName.equals("resource")){
			mp3info=new Mp3Info();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if(mp3info!=null){
			String temp=new String(ch, start, length);
			if(TagName.equals("id")){
				mp3info.setId(temp);
			}else if(TagName.equals("mp3.name")){
				mp3info.setMp3Name(temp);
			}else if(TagName.equals("mp3.size")){
				mp3info.setMp3Size(temp);
			}else if (TagName.equals("lrc.name")){
				mp3info.setLrcName(temp);
			}else if(TagName.equals("lrc.size")){
				mp3info.setLrcSize(temp);
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("-------------------------------------");
		if(qName.equals("resource")){
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			infos.add(mp3info);
		}
		TagName="";
	}
	public List<Mp3Info> getInfos() {
		return infos;
	}
	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}
}
