package br.com.eversource.apps.mobile.contadibuteco.core;

import java.util.Vector;

public class Butequeiro {
	
	private String nome;
	private Vector despesasNome;
	private Vector despesasValor;
	
	public Butequeiro (String nome) {
		this.nome = nome;
		this.despesasNome = new Vector();
		this.despesasValor = new Vector();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Vector getDespesasNome() {
		return despesasNome;
	}

	public void setDespesasNome(Vector despesasNome) {
		this.despesasNome = despesasNome;
	}

	public Vector getDespesasValor() {
		return despesasValor;
	}

	public void setDespesasValor(Vector despesasValor) {
		this.despesasValor = despesasValor;
	}

	public boolean adicionaDespesa(String despesa, double valor) {
		if ( this.despesasNome.contains(despesa))
			return false;
		
		this.despesasNome.addElement(despesa);
		this.despesasValor.addElement(new Double(valor));
		return true;		
	}
	
	public double getValorContaButequeiro() {
		double valor = 0;
		
		for ( int i = 0; i < this.despesasValor.size(); i++ )
			valor += ((Double)this.despesasValor.elementAt(i)).doubleValue();
		
		return valor;
	}
}