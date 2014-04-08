package br.com.eversource.apps.mobile.churrascalculator.core;

public class Util {
	
	private Util() {}
	
	public static int round(double valor) {
		if ( (valor - ((int)valor)) >= 0.5 )
			return (((int)valor)+1);
		else if ( (int)valor == 0 )
			return 1;
		else
			return (int) valor;
	}
}
