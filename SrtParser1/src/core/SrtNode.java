package core;

public class SrtNode {
	/**
	 * 定义了字幕结点（SrtNode）的结构，并提供 get、set 方法
	 * */
	// SrtNode 的结构
	int sid;
	SrtTime begin;
	SrtTime end;
	String content;

	SrtNode() {

	}

	SrtNode(int sid,SrtTime begin,SrtTime end,String content) {
		this.sid=sid;
		this.begin=begin;
		this.end=end;
		this.content=content;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public SrtTime getBegin() {
		return begin;
	}

	public void setBegin(SrtTime begin) {
		this.begin = begin;
	}

	public SrtTime getEnd() {
		return end;
	}

	public void setEnd(SrtTime end) {
		this.end = end;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
