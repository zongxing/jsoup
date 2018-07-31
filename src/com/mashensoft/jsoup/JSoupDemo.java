package com.mashensoft.jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupDemo {
	/**
	 * 下载图片
	 * @param picUrl
	 */
	public static void download(String picUrl) {
		try {
			//http://dkfjskdk.com/kdfk/kdkkd/aa.jpg
			String name = picUrl.substring(picUrl.lastIndexOf("/")+1);
			String folder = "D:\\pics\\";
			URL url = new URL(picUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3486.0 Safari/537.36");
			BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
			BufferedOutputStream os = new BufferedOutputStream( new FileOutputStream(folder+name));
			byte[] cache = new byte[1024];
			int len = 0;
			while((len=is.read(cache))!=-1) {
				os.write(cache, 0, len);
			}
			os.close();
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 从一个网页里取出图片的地址
	 * @param webPageUrl
	 * @return
	 */
	public static String getPicUrlFromWebPage(String webPageUrl) {
		String picUrl = "";
		try {
			Jsoup.connect(webPageUrl).request().header("User-Agent:", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3486.0 Safari/537.36");
			Element element = Jsoup.connect(webPageUrl).get().getElementById("J_worksImg");
			picUrl = element.attr("src");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picUrl;
	}
	/**
	 * 从一个分页里取出所有的地址
	 * @param onePageUrl
	 * @return
	 */
	public static String[] getOnePageAllWebUrl(String onePageUrl) {
		int len=0;
		try {
			Elements elements = Jsoup.connect(onePageUrl).get().getElementsByClass("search-works-thumb relative");
			len = elements.size();
			String[] array = new String[len];
			System.out.println(len);
			for(int i=0;i<len;i++) {
				Element element = elements.get(i);
				String href = element.attr("href");
				array[i] = href;
			}
			return array;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 从一个分页里取出所有的分页
	 * @param firstPageUrl
	 * @return
	 */
	public static String[] getAllPagingUrl(String firstPageUrl) {
		int len = 0;
		try {
			Elements elements = Jsoup.connect(firstPageUrl).get().getElementsByClass("seo-page-num");
			System.out.println(elements.size());
			len = elements.size();
			String[] arr = new String[len];
			for(int i=0;i<elements.size();i++) {
				Element element = elements.get(i);
				String href = element.attr("href");
				arr[i] = href;
			}
			return arr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void test1() {
		String picurl = "http://pic171.nipic.com/file/20180708/1609131_002001489000_2.jpg";
		download(picurl);
	}
	public static void test2() {
		String picurl = "http://www.nipic.com/show/20522289.html";
		download(getPicUrlFromWebPage(picurl));
	}
	public static void test3() {
		String picurl = "http://www.nipic.com/topic/show_27090_1.html";
		String [] arr = getOnePageAllWebUrl(picurl);
		for(int i=0;i<arr.length;i++) {
			System.out.println(i+","+arr[i]);
		}
	}

	public static void test4() {
		String picurl = "http://www.nipic.com/topic/show_27090_1.html";
		String [] arr = getOnePageAllWebUrl(picurl);
		for(int i=0;i<arr.length;i++) {
			System.out.println(i+","+arr[i]);
			String mypicurl = getPicUrlFromWebPage(arr[i]);
			download(mypicurl);
		}
	}
	public static void test5() {
		String picurl = "http://www.nipic.com/topic/show_27090_1.html";
		
		getAllPagingUrl(picurl);
		
	}
	public static void test6() {
		String firstPageUrl = "http://www.nipic.com/topic/show_27090_1.html";
		String allPageUrl[] = getAllPagingUrl(firstPageUrl);
		for (int i = 0; i < allPageUrl.length; i++) {
			String onePageUrl = allPageUrl[i];
			String[] onePageAllUrl = getOnePageAllWebUrl(onePageUrl);
			for(int j=0;j<onePageAllUrl.length;j++) {
				String weburl = onePageAllUrl[j];
				String picUrl = getPicUrlFromWebPage(weburl);
				download(picUrl);
			}
		}
		
	}
	public static void main(String[] args) {
		test6();
	}
}
