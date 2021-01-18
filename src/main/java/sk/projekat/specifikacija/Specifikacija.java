package sk.projekat.specifikacija;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public abstract class Specifikacija {

	private ArrayList<Entitet> entiteti;
	private File directory;
	private ArrayList<File> files;
	private int idCounter;
	private boolean autoId;
	private int maxEnt;
	private int fileCount;
	private boolean isNew;
	private File config;
	

	public Specifikacija() {
		entiteti = new ArrayList<Entitet>();
		files = new ArrayList<File>();
	}
	
	public void openDatabase(File directory, boolean isNew) {
		setDirectory(directory);
		config = new File(this.getDirectory().getAbsolutePath() + "/config.txt");
		try {
			if(config.createNewFile()) {
				makeConfig(config);
			}
			setConfig(config);
			//System.out.println(autoId + " " + idCounter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			absOpenDatabase(isNew, fileCount);
		this.isNew = false;
	}
	
	public abstract void absOpenDatabase(boolean isNew, int fileCount);
	
	public abstract void read(int fileNumber);
	
	public abstract void write(int fileNumber);
	
	
	public void addEntitet(String id, String name, String fields) {
		Entitet e = null;
		if(autoId) {
			int identifikator = idCounter;
			HashMap<String, String> polja = makeMapFromFields(fields);
			e = new Entitet(identifikator, name, polja);
			entiteti.add(e);
			fileCount = entiteti.size() / maxEnt + 1;
			System.out.println(fileCount);
			write(fileCount);	
			idCounter++;
			updateConfig();
			
		}
		else { 
			int identifikator = Integer.parseInt(id);
			HashMap<String, String> polja = makeMapFromFields(fields);
			e = new Entitet(identifikator, name, polja);
			entiteti.add(e);
			fileCount = entiteti.size() / maxEnt + 1;
			System.out.println(fileCount);
			write(fileCount);	
			idCounter++;
			updateConfig();}
	}

	public ArrayList<Entitet> getEntitetiForFile(int fileIndex) {
		ArrayList<Entitet> e = new ArrayList<Entitet>();
		int lastIndex = maxEnt;
		if(fileIndex == fileCount - 1) 
			e.addAll(this.getEntiteti().subList(fileIndex * maxEnt, this.getEntiteti().size()));
		else
			e.addAll(this.getEntiteti().subList(fileIndex * maxEnt, fileIndex + lastIndex));
		return e;
	}
	
	public void deleteEntitet(int id) {
		Entitet ent = null;
		for(Entitet e: entiteti) {
			if(e.getId() == id) {
				ent = e;
				break;
			}
		}
		entiteti.remove(ent);
		fileCount = entiteti.size() / maxEnt + 1;
		System.out.println(fileCount);
		write(fileCount);
		updateConfig();
	}
	
	public void deleteEntitet(String naziv, String fields) {
		HashMap<String, String> polja = makeMapFromFields(fields);
		ListIterator<Entitet> itr = entiteti.listIterator();
		boolean shouldDelete = true;
		while(itr.hasNext()) {
			Entitet e = itr.next();
			shouldDelete = true;
			//if(naziv == e.getName()) {
				//izdvojiti ovaj kod u metodu?
				//for(Map.Entry<String, String> polje: polja.entrySet()) {
					//if(e.getPolja().containsKey(polje.getKey())) {
						//if(!(e.getPolja().get(polje.getKey()) == polje.getValue())) {
							//shouldDelete = false;
						//}
					}//provera i za ugnjezdene entitete
				//	else shouldDelete = false;
				//}
				//if(shouldDelete)
				//	itr.remove();
		//	}
	//	}
		fileCount = entiteti.size() / maxEnt + 1;
		System.out.println(fileCount);
		write(fileCount);
		updateConfig();
		//valjda je okej aaaaaaaaa
	}
	
	public void addInnerEntitet(int parentId, String id, String name, String fields) {
		Entitet ent = null;
		for(Entitet e: entiteti) {
			if(e.getId() == parentId) {
				ent = e;
			}
		}
		Entitet e = null;
		int identifikator = Integer.parseInt(id);
		HashMap<String, String> polja = makeMapFromFields(fields);
		e = new Entitet(identifikator, name, polja);
		ent.addUgnjezdeni(name, e);
		fileCount = entiteti.size() / maxEnt + 1;
		System.out.println(fileCount);
		write(fileCount);
		updateConfig();
	}
	
	public void updateEntitet(int stariId, String id, String naziv, String fields) {
		Entitet ent = null;
		for(Entitet e: entiteti) {
			if(e.getId() == stariId) {
				ent = e;
				break;
			}
		}
		int noviIdentifikator = Integer.parseInt(id);
		if(autoId)
			noviIdentifikator = stariId;
		HashMap<String, String> polja = makeMapFromFields(fields);
		ent.updateEnt(noviIdentifikator, naziv, polja);

		fileCount = entiteti.size() / maxEnt + 1;
		System.out.println(fileCount);
		write(fileCount);
		updateConfig();
		//da li ostaje referenca tacnije da li mogu da kazem new i da ono ostane u listi ili moram da dodam neku metodu koja menja samo polja
	}
	
	public ArrayList<Entitet> search(String name, String fields){
		ArrayList<Entitet> searchResult = new ArrayList<Entitet>();
		for(Entitet e: entiteti) {
			if(e.getName() == name) {
				for(Map.Entry<String, String> polje: e.getPolja().entrySet()) {
					
				}
			}
		}
		return searchResult;
	}
	
	public ArrayList<Entitet> sort(){
		ArrayList<Entitet> sortedList = new ArrayList<Entitet>();
		
		entiteti.sort();
		
		return sortedList;
	}
	
	private HashMap<String, String> makeMapFromFields(String fields) {
		HashMap<String, String> polja = new HashMap<String, String>();
		String[] keyValue = fields.split("\n");
		for(int i = 0; i < keyValue.length; i++) {
			String[] keyValueInstance = keyValue[i].split(":");
			polja.put(keyValueInstance[0], keyValueInstance[1]);
		}
		return polja;
	}
	
	public void clearTheFiles(boolean isNew) throws IOException {
		if(isNew)
			return;
		for(File f: files) {
			FileWriter fw = new FileWriter(f, false);
			PrintWriter pw = new PrintWriter(fw, false);
			pw.flush();
			pw.close();
			fw.close();
		}
	}
	
	
	private void updateConfig() {
		FileWriter fw;
		try {
			fw = new FileWriter(config, false);
			PrintWriter pw = new PrintWriter(fw, false);
			pw.flush();
			pw.close();
			fw.close();

			BufferedWriter bw = new BufferedWriter(new FileWriter(config));
			bw.write("autoId=" + this.isAutoId() + "\n");
			bw.write("idCounter=" + this.getIdCounter()  + "\n");
			bw.write("maxEnt=" + this.getMaxEnt()  + "\n");
			bw.write("fileCount=" + this.getFileCount()  + "\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeConfig(File file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("autoId=" + this.isAutoId() + "\n");
		bw.write("idCounter=0" + "\n");
		bw.write("maxEnt=5" + "\n");
		bw.write("fileCount=1" + "\n");
		bw.close();
	}
	
	public void deleteExcessFiles() {
		File[] files = this.directory.listFiles();
		for(int i = 0; i < files.length; i++) {
			if(files[i].length() == 0)
				files[i].delete();
		}
	}
	
	private void setConfig(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		for(int i = 1; i < 5; i++) {
			String line = br.readLine();
			String[] config = line.split("=");
			switch(i) {
			case 1:
				setAutoId(Boolean.parseBoolean(config[1]));
				break;
			case 2:
				setIdCounter(Integer.parseInt(config[1]));
				break;
			case 3:
				setMaxEnt(Integer.parseInt(config[1]));
				break;
			case 4:
				setFileCount(Integer.parseInt(config[1]));
				break;
			}
		}
		
	}
	
	public ArrayList<Entitet> getEntiteti(){
		return entiteti;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> file) {
		this.files = file;
	}

	public int getIdCounter() {
		return idCounter;
	}

	public void setIdCounter(int idCounter) {
		this.idCounter = idCounter;
	}

	public boolean isAutoId() {
		return autoId;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public void setAutoId(boolean autoId) {
		this.autoId = autoId;
	}

	public int getMaxEnt() {
		return maxEnt;
	}

	public void setMaxEnt(int maxEnt) {
		this.maxEnt = maxEnt;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public void setEntiteti(ArrayList<Entitet> entiteti) {
		this.entiteti = entiteti;
	}

	public void removeEntitet(Entitet ent) {
		this.entiteti.remove(ent);
	}

	public File getConfig() {
		return config;
	}
	
}
