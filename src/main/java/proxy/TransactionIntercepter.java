package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class TransactionIntercepter  implements InvocationHandler {
	   Object target;

	   private TransactionIntercepter(Object target) {
	      this.target = target;
	   }

	   @SuppressWarnings({ "rawtypes", "unchecked" })
	   public static <T> T getProxyInstance(T instance) {
		   Class<? extends Object> clazz = instance.getClass();
	      // 対象クラスが実装するインターフェースのリスト
		   Class[] classes = clazz.getInterfaces();
		   TransactionIntercepter intercepter = new TransactionIntercepter(instance);
		   T proxyInstance = (T) Proxy.newProxyInstance(clazz.getClassLoader(), classes, intercepter);
		   return proxyInstance;
	   }

	   @Override
	   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		   EntityManagerFactory factory = Persistence.createEntityManagerFactory("myUnitInPersistenceXML");
		   EntityManager em = factory.createEntityManager();		
		   EntityTransaction entityTransaction = em.getTransaction();
		   entityTransaction.begin();

		   Object result=null;
		   try {
			  //EntityManaterのインスタンスをセットする
			  target.getClass().getDeclaredMethod("setEm",EntityManager.class).invoke(target,em);
		      // 実際のコードを呼び出し
			  result = method.invoke(this.target, args);
		   }catch(Exception e) {
			   e.printStackTrace();
			  entityTransaction.rollback();;
			  throw e;
		   }
		   entityTransaction.commit();
		   return result;
	   }
}
