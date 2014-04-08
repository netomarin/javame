package br.com.eversource.apps.mobile.churrascalculator.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import br.com.eversource.apps.mobile.churrascalculator.ChurrasCalculatorMIDlet;
import br.com.eversource.apps.mobile.churrascalculator.core.Util;

public class Churrasco extends Persistence {
	
	/*
	 * Constantes para calculo de churrasco
	 */
	public static final int NIVEL_ALCOOLICO_SUSSA = 1;
	public static final int NIVEL_ALCOOLICO_TRANQUILO = 2;
	public static final int NIVEL_ALCOOLICO_NORMAL = 3;
	public static final int NIVEL_ALCOOLICO_GALERA = 4;
	public static final int NIVEL_ALCOOLICO_UNIVERSITARIO = 5;
	
	public static final int QTD_COMIDA_ALMOCO = 1;
	public static final int QTD_COMIDA_LEVE = 2;
	public static final int QTD_COMIDA_NORMAL = 3;
	public static final int QTD_COMIDA_ANIMADO = 4;
	public static final int QTD_COMIDA_LONGA_DURACAO = 5;
	
	private static Hashtable FATOR_BEBIDA = new Hashtable();
	private static Hashtable FATOR_COMIDA = new Hashtable();
	static {
		FATOR_BEBIDA.put(new Integer(Churrasco.NIVEL_ALCOOLICO_SUSSA), new Integer(3));
		FATOR_BEBIDA.put(new Integer(Churrasco.NIVEL_ALCOOLICO_TRANQUILO), new Integer(4));
		FATOR_BEBIDA.put(new Integer(Churrasco.NIVEL_ALCOOLICO_NORMAL), new Integer(6));
		FATOR_BEBIDA.put(new Integer(Churrasco.NIVEL_ALCOOLICO_GALERA), new Integer(9));
		FATOR_BEBIDA.put(new Integer(Churrasco.NIVEL_ALCOOLICO_UNIVERSITARIO), new Integer(12));
		
		FATOR_COMIDA.put(new Integer(Churrasco.QTD_COMIDA_ALMOCO), new Double(0.15));
		FATOR_COMIDA.put(new Integer(Churrasco.QTD_COMIDA_LEVE), new Double(0.22));
		FATOR_COMIDA.put(new Integer(Churrasco.QTD_COMIDA_NORMAL), new Double(0.3));
		FATOR_COMIDA.put(new Integer(Churrasco.QTD_COMIDA_ANIMADO), new Double(0.45));
		FATOR_COMIDA.put(new Integer(Churrasco.QTD_COMIDA_LONGA_DURACAO), new Double(0.6));
	}
	
	/*
	 * Atributos de classe
	 */
	private int homens;
	private int mulheres;
	private int nivelAlcoolico;
	private int qtdComida;
	private boolean comCaipirinha;
	private String descricao;
	private double linguica;
	private double carne;
	private int cerveja;
	private int refrigerante;
	private int pao;
	private int farofa;
	private int carvao;
	private int guardanapo;
	private int alcool;
	private int panoDePrato;
	private int faca;
	private int prato;
	private int garfo;
	private int copoPlastico;
	private int salGrosso;
	private int limao;
	private int cachaca;
	private int acucar;

	public Churrasco(int id) {
		this.setID(id);
	}
	
	public Churrasco(int homens, int mulheres, int nivelAlcoolico, int qtdComida, boolean comCaipirinha) {
		this.homens = homens;
		this.mulheres = mulheres;
		this.nivelAlcoolico = nivelAlcoolico;
		this.qtdComida = qtdComida;
		this.comCaipirinha = comCaipirinha;
	}

	public int getHomens() {
		return homens;
	}

	public void setHomens(int homens) {
		this.homens = homens;
	}

	public int getMulheres() {
		return mulheres;
	}

	public void setMulheres(int mulheres) {
		this.mulheres = mulheres;
	}

	public int getNivelAlcoolico() {
		return nivelAlcoolico;
	}

	public void setNivelAlcoolico(int nivelAlcoolico) {
		this.nivelAlcoolico = nivelAlcoolico;
	}

	public int getQtdComida() {
		return qtdComida;
	}

	public void setQtdComida(int qtdComida) {
		this.qtdComida = qtdComida;
	}

	public boolean isComCaipirinha() {
		return comCaipirinha;
	}

	public void setComCaipirinha(boolean comCaipirinha) {
		this.comCaipirinha = comCaipirinha;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getLinguica() {
		return linguica;
	}

	public void setLinguica(double linguica) {
		this.linguica = linguica;
	}

	public double getCarne() {
		return carne;
	}

	public void setCarne(double carne) {
		this.carne = carne;
	}

	public int getCerveja() {
		return cerveja;
	}

	public void setCerveja(int cerveja) {
		this.cerveja = cerveja;
	}

	public int getRefrigerante() {
		return refrigerante;
	}

	public void setRefrigerante(int refrigerante) {
		this.refrigerante = refrigerante;
	}

	public int getPao() {
		return pao;
	}

	public void setPao(int pao) {
		this.pao = pao;
	}

	public int getFarofa() {
		return farofa;
	}

	public void setFarofa(int farofa) {
		this.farofa = farofa;
	}

	public int getCarvao() {
		return carvao;
	}

	public void setCarvao(int carvao) {
		this.carvao = carvao;
	}

	public int getGuardanapo() {
		return guardanapo;
	}

	public void setGuardanapo(int guardanapo) {
		this.guardanapo = guardanapo;
	}

	public int getAlcool() {
		return alcool;
	}

	public void setAlcool(int alcool) {
		this.alcool = alcool;
	}

	public int getPanoDePrato() {
		return panoDePrato;
	}

	public void setPanoDePrato(int panoDePrato) {
		this.panoDePrato = panoDePrato;
	}

	public int getFaca() {
		return faca;
	}

	public void setFaca(int faca) {
		this.faca = faca;
	}

	public int getPrato() {
		return prato;
	}

	public void setPrato(int prato) {
		this.prato = prato;
	}

	public int getGarfo() {
		return garfo;
	}

	public void setGarfo(int garfo) {
		this.garfo = garfo;
	}

	public int getCopoPlastico() {
		return copoPlastico;
	}

	public void setCopoPlastico(int copoPlastico) {
		this.copoPlastico = copoPlastico;
	}

	public int getSalGrosso() {
		return salGrosso;
	}

	public void setSalGrosso(int salGrosso) {
		this.salGrosso = salGrosso;
	}

	public int getLimao() {
		return limao;
	}

	public void setLimao(int limao) {
		this.limao = limao;
	}

	public int getCachaca() {
		return cachaca;
	}

	public void setCachaca(int pinga) {
		this.cachaca = pinga;
	}

	public int getAcucar() {
		return acucar;
	}

	public void setAcucar(int acucar) {
		this.acucar = acucar;
	}
	
	public void calcularChurrasco() {
		double fatorComida = ((Double)Churrasco.FATOR_COMIDA.get(new Integer(getQtdComida()))).doubleValue();
		int fatorBebida = ((Integer)Churrasco.FATOR_BEBIDA.get(new Integer(getNivelAlcoolico()))).intValue();
		
		this.setLinguica(this.homens*(fatorComida-0.08)+this.mulheres*(fatorComida-0.1));
		this.setCarne(this.homens*fatorComida+this.mulheres*(fatorComida-0.07));
		this.setCerveja(this.homens*fatorBebida+this.mulheres*(fatorBebida-2));
		this.setRefrigerante(1+Util.round(((this.homens*0.3+this.mulheres*0.5)/2)));
		this.setPao((int)(this.homens*2+this.mulheres*1.5));
		this.setFarofa(Util.round(1+(this.homens*0.03+this.mulheres*0.01)));
		this.setCarvao(Util.round(1+getCarne()/7));
		this.setGuardanapo(Util.round(1+(this.homens+this.mulheres)/14));
		this.setAlcool(1);
		this.setPanoDePrato(1+Util.round((this.homens+this.mulheres)/10));
		this.setFaca(2+Util.round((this.homens+this.mulheres)/10));
		this.setPrato(3+Util.round(this.homens/10+this.mulheres/10));
		this.setGarfo(2+Util.round((this.homens+this.mulheres)/10));
		this.setCopoPlastico(Util.round((((this.homens*1.2+this.mulheres*1.5))+1)/20));
		this.setSalGrosso(Util.round(getCarne()/30));
		this.setLimao(3+Util.round(this.homens/2+this.mulheres/2));
		this.setCachaca(Util.round((this.homens+this.mulheres)/20));
		this.setAcucar(Util.round((((this.homens+this.mulheres)/20+1)/5)));
	}

	public void deserialize(byte[] content) throws IOException {
		ByteArrayInputStream input = null;
		DataInputStream stream = null;
		
		try {
			input = new ByteArrayInputStream(content);
			stream = new DataInputStream(input);
			
			this.setHomens(stream.readInt());
			this.setMulheres(stream.readInt());
			this.setNivelAlcoolico(stream.readInt());
			this.setQtdComida(stream.readInt());
			this.setComCaipirinha(stream.readBoolean());
			this.setDescricao(stream.readUTF());
			this.setLinguica(stream.readDouble());
			this.setCarne(stream.readDouble());
			this.setCerveja(stream.readInt());
			this.setRefrigerante(stream.readInt());
			this.setPao(stream.readInt());
			this.setFarofa(stream.readInt());
			this.setCarvao(stream.readInt());
			this.setGuardanapo(stream.readInt());
			this.setAlcool(stream.readInt());
			this.setPanoDePrato(stream.readInt());
			this.setFaca(stream.readInt());
			this.setPrato(stream.readInt());
			this.setGarfo(stream.readInt());
			this.setCopoPlastico(stream.readInt());
			this.setSalGrosso(stream.readInt());
			this.setLimao(stream.readInt());
			this.setCachaca(stream.readInt());
			this.setAcucar(stream.readInt());
		} finally {
			if ( stream != null ) stream.close();
			if ( input != null ) input.close();
		}
	}

	public byte[] serialize() throws IOException {
		byte[] result = null;
		ByteArrayOutputStream output = null;
		DataOutputStream stream = null;
		
		try {
			output = new ByteArrayOutputStream();
			stream = new DataOutputStream(output);
			
			stream.writeInt(getHomens());
			stream.writeInt(getMulheres());
			stream.writeInt(getNivelAlcoolico());
			stream.writeInt(getQtdComida());
			stream.writeBoolean(isComCaipirinha());
			stream.writeUTF(getDescricao());
			stream.writeDouble(getLinguica());
			stream.writeDouble(getCarne());
			stream.writeInt(getCerveja());
			stream.writeInt(getRefrigerante());
			stream.writeInt(getPao());
			stream.writeInt(getFarofa());
			stream.writeInt(getCarvao());
			stream.writeInt(getGuardanapo());
			stream.writeInt(getAlcool());
			stream.writeInt(getPanoDePrato());
			stream.writeInt(getFaca());
			stream.writeInt(getPrato());
			stream.writeInt(getGarfo());
			stream.writeInt(getCopoPlastico());
			stream.writeInt(getSalGrosso());
			stream.writeInt(getLimao());
			stream.writeInt(getCachaca());
			stream.writeInt(getAcucar());
			
			result = output.toByteArray();
		} finally {
			if ( stream != null ) stream.close();
			if ( output != null ) output.close();
		}
		
		return result;
	}
	
	public static Churrasco[] getRecordsArray(RecordFilter rf, RecordComparator rc, RecordStore rms) throws RecordStoreNotOpenException, IOException {
		RecordEnumeration re = rms.enumerateRecords(rf, rc, false);
		Churrasco[] churrasArray = new Churrasco[re.numRecords()];
		RecordStore db = null;

		try {
			db = RecordStore.openRecordStore(ChurrasCalculatorMIDlet.RMS_CHURRASCOS, true);
			for ( int i = 0; re.hasNextElement(); i++ ) {
				churrasArray[i] = new Churrasco(re.nextRecordId());
				churrasArray[i].persistence(Persistence.LOAD, db);
			}
		} catch (RecordStoreException e) {
		} finally {
			if ( db != null )
				try {
					db.closeRecordStore();
				} catch (RecordStoreException e) {}
		}

		return churrasArray;
	}
}