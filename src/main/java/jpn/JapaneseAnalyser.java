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
    
    //JSoup���g����xml����͂���
	public static List<String> parseXML(String str){
		try{
			return Jsoup.parse(new File(str),"UTF-8")
					//�^�O question�ɑ�����q�v�f�����ׂĎ擾����
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
	
	//�����œn���ꂽ������ɑ΂��Ė����𒊏o����
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
    
    //�I�����Ɋ܂܂�Ă���e�L�X�g��A������
	public static String concatQuestionStrs(Elements elements) {
		return elements
		.stream()
		.reduce("",
    			(e1,e2)->e1+e2.text(),
    			(e1,e2)->e2);
	}
	
    //���O����
	//���{���͂���Ƃ��͖����݂̂𒊏o����
    public static boolean countOut(String s){
    	return s.indexOf("����") >=0;
    }
	//���{���͂�����A�t�B���^�[�����������������L�q����
	public static boolean myFilter(String s) {
		return !(s.indexOf(",")>=0
				||s.indexOf("(")>=0
				||s.indexOf(")")>=0);
		
	}
}
