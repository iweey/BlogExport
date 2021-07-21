package com.blogexport.model.sina;

import com.blogexport.model.Blog;
import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

@Gecco(matchUrl="http://blog.sina.com.cn/s/blog_{id}.html", pipelines="SinaBlogPipeline")
public class SinaBlog extends Blog implements HtmlBean {

	private static final long serialVersionUID = -7127412585200687225L;

	@Request
	private HttpRequest request;

	@RequestParameter("id")
	private String id;


	@Text(own=false)
	@HtmlField(cssPath="h2")
	private String title;

	@Text(own=false)
	@HtmlField(cssPath=".h1_tit")
	private String subtitle;

	@Html()
	@HtmlField(cssPath="#sina_keyword_ad_area2")
	private String content;


	@Text(own=false)
	@HtmlField(cssPath=".time")
	private String time;

	//TODO 不能写死
	@Image(download="/Users/laijiawei/Documents/source/java/blog-export/export/image",value={"real_src"})
	@HtmlField(cssPath="#sina_keyword_ad_area2 img")
	private List<String> imgs;

	@Override
	public String toString() {
		return "SinaBlog{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", subtitle='" + subtitle + '\'' +
				", time='" + time + '\'' +
				'}';
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}




}
