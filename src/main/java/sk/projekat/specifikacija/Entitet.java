package sk.projekat.specifikacija;


import java.util.HashMap;
import java.util.Map;

public class Entitet {

	private int id;
	private String name;
	private Map<String, String> polja;
	private Map<String, Entitet> ugnjezdeniEntiteti;
	
	public Entitet(int id, String name, Map<String, String> polja) {
		this.id = id;
		this.name = name;
		this.polja = polja;
		this.ugnjezdeniEntiteti = new HashMap<String, Entitet>();
	}
	
	public Entitet() {
		
	}

	public void updateEnt(int id, String name, Map<String, String> polja) {
		this.id = id;
		this.name = name;
		this.polja = polja;
	}
	
	public void addUgnjezdeni(String name, Entitet e) {
		this.ugnjezdeniEntiteti.put(name, e);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getPolja() {
		return polja;
	}

	public void setPolja(Map<String, String> polja) {
		this.polja = polja;
	}
	
	public Map<String, Entitet> getUgnjezdeniEntiteti() {
		return ugnjezdeniEntiteti;
	}

	public void setUgnjezdeniEntiteti(Map<String, Entitet> ugnjezdeniEntiteti) {
		this.ugnjezdeniEntiteti = ugnjezdeniEntiteti;
	}
	
	@Override
	public String toString() {
		return id + name + polja;
	}

	public String mapToString() {
		String s = "";
		for(Map.Entry<String, String> e: this.polja.entrySet()) {
			System.out.println(s);
			s = s.concat(e.getKey() + ":" + e.getValue() + "\n");
		}
		return s;
	}
}
