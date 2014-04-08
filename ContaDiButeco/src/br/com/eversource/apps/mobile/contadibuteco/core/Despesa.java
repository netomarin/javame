package br.com.eversource.apps.mobile.contadibuteco.core;

import java.util.Vector;

public class Despesa {

	private String nome;
	private double valor;
	private Vector butequeiros;
	
	public Despesa(String nome, double valor) {
		this.nome = nome;
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Vector getButequeiros() {
		return butequeiros;
	}

	public void setButequeiros(Vector butequeiros) {
		this.butequeiros = butequeiros;
	}
	
	public boolean incluiButequeiro(Butequeiro novoButequeiro) {
		if ( this.butequeiros == null )
			this.butequeiros = new Vector();
		
		if ( this.butequeiros.contains(novoButequeiro))
			return false;
			
		this.butequeiros.addElement(novoButequeiro);
		return true;
	}
	
	public void fecharDespesa() {
		double valorButequeiro = this.valor/this.butequeiros.size();
		
		for ( int i = 0; i < this.butequeiros.size(); i++ )
			((Butequeiro)this.butequeiros.elementAt(i)).adicionaDespesa(this.nome, valorButequeiro);
	}
}