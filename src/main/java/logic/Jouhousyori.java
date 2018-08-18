package logic;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.EntityManager;

import entity.JouhousyoriEntity;

public class Jouhousyori implements Logic{
	
	private EntityManager em;
	private String path;
	
	public static void main(String args[]) throws Exception{
	}

	@Override
	public void execute() throws Exception{
		JouhousyoriEntity entity = new JouhousyoriEntity();
		String xml = new String(Files.readAllBytes(Paths.get(getPath())));
		entity .setText(xml);
		em.persist(entity);
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
