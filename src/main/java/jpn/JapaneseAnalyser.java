package jpn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Tokenizer;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class JapaneseAnalyser {
	private static final String FILE_NAME="2017h29h_sc_am2_qs.pdf.xml";
	
    public static void main(String[] args) throws Exception{
    	System.out.println(getResult());
    }	
    
    public static String getResult() throws Exception{
    	return parseXML(String.format("pdf/xml/%s", FILE_NAME))
    	.stream()
    	.reduce("",
    			(s,s1)->s+s1+System.lineSeparator(),
    			(s,s1)->s);
    }
    
    //JSoupを使ってxmlを解析する
	public static List<String> parseXML(String str){
		try{
			return Jsoup.parse(new File(str),"UTF-8")
					//タグ questionに属する子要素をすべて取得する
					.getElementsByTag("question") 
					.stream()
					.map(s->s.attr("num")+":"
							+transform(s.text()
							+" "
							+concatQuestionStrs(s.getElementsByTag("selection"))))
					.collect(Collectors.toList());
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<String>();
		}
    }
	
	//引数で渡された文字例に対して名詞を抽出する
	public static String transform(String s){
    	return Tokenizer.builder()
        	    .build()
        	    .tokenize(s)
        	    .stream()
        	    .filter(a ->countOut(a.getPartOfSpeech()))
        	    .map(e -> e.getSurfaceForm())
        	    .filter(JapaneseAnalyser::myFilter)
        	    .reduce((v1,v2)->v1+"|"+v2)
        	    .orElse("");
    }
    
    //選択肢に含まれているテキストを連結する
	public static String concatQuestionStrs(Elements elements) {
		return elements
		.stream()
		.reduce("",
    			(e1,e2)->e1+e2.text(),
    			(e1,e2)->e2);
	}
	
    //除外条件
	//日本語解析するときは名詞のみを抽出する
    public static boolean countOut(String s){
    	return s.indexOf("名詞") >=0;
    }
	//日本語解析した後、フィルターをかけたい文字を記述する
	public static boolean myFilter(String s) {
		return !(s.indexOf(",")>=0
				||s.indexOf("(")>=0
				||s.indexOf(")")>=0);
		
	}
}
