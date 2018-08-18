

import org.junit.jupiter.api.Test;

import logic.Jouhousyori;
import logic.Logic;
import proxy.TransactionIntercepter;

class LogicTest {

	@Test
	void testParsePDF() throws Exception{
		Jouhousyori target = new Jouhousyori();
		target.setPath("C:\\java\\workspace\\jyouhousyori2\\pdf\\xml\\2018h30h_sc_am2_qs.pdf.xml");
		Logic targetClass = TransactionIntercepter.getProxyInstance(target);
		targetClass.execute();
	}
	

}
