package jpn;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Tokenizer;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JapaneseAnalyser {
	private static final String FILE_NAME="2017h29h_sc_am2_qs.pdf.xml";
	
    public static void main(String[] args) throws Exception{
    	System.out.println(getResult());
    }	
    
    public static String getResult() throws Exception{
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(parseXML(String.format("pdf/xml/%s", FILE_NAME)));
    }
    
    //JSoup���g����xml����͂���
	public static Map<String,List<String>> parseXML(String str){
		try{
			return Jsoup.parse(new File(str),"UTF-8")
					//�^�O question�ɑ�����q�v�f�����ׂĎ擾����
					.getElementsByTag("question") 
					.stream()
					.collect(Collectors.toMap(
 								e->e.attr("num"),	
 								e->transform(e.text()+concatQuestionStrs(e.getElementsByTag("selection"))))
							);
		}catch(Exception e){
			e.printStackTrace();
			return new HashMap<String,List<String>>();
		}
    }
	
	//�����œn���ꂽ������ɑ΂��Ė����𒊏o����
	public static List<String> transform(String s){
    	return Tokenizer.builder()
        	    .build()
        	    .tokenize(s)
        	    .stream()
        	    .filter(a ->countOut(a.getPartOfSpeech()))
        	    .map(e -> e.getSurfaceForm())
        	    .filter(JapaneseAnalyser::myFilter)
        	    .collect(Collectors.toList());
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
