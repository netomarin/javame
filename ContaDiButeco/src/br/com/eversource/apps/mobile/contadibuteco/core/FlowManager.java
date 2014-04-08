package br.com.eversource.apps.mobile.contadibuteco.core;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.MIDlet;

import br.com.eversource.apps.mobile.contadibuteco.ui.ListaButequeirosForm;

public class FlowManager {

	private static FlowManager instance;
	private MIDlet midlet;
	private List listaDespesasConta;
	
	private Hashtable butequeiros;
	private String[] butequeirosString;
	private Vector despesas;
	private boolean incluirDezPorCento;

	private FlowManager(MIDlet midlet) {
		this.midlet = midlet;
	}

	public static FlowManager startFlowManager(MIDlet midlet) throws FlowManagerAlreadyStartedException {
		if ( instance != null )
			throw new FlowManagerAlreadyStartedException("FlowManager already exists at "+instance);

		instance = new FlowManager(midlet);
		return instance;
	}

	public static FlowManager getInstance() throws FlowManagerNotStartedException {
		if ( instance == null )
			throw new FlowManagerNotStartedException();

		return instance;
	}

	public void endApplication() {
		this.midlet.notifyDestroyed();
	}
	
	public void calcularNovaConta() {
		ListaButequeirosForm listaButequeiros = new ListaButequeirosForm();
		try {
			DisplayManager.getInstance().show(listaButequeiros, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void lancarConta(String[] butequeirosArray, boolean incluirDezPorCento) {		
		this.butequeirosString = butequeirosArray;
		this.incluirDezPorCento = incluirDezPorCento;
		
		this.butequeiros = new Hashtable();
		for( int i = 0; i < butequeirosArray.length; i++ )
			this.butequeiros.put(butequeirosArray[i], new Butequeiro(butequeirosArray[i]));
		
		this.listaDespesasConta = new List("Conta", List.EXCLUSIVE|List.IMPLICIT);
		this.listaDespesasConta.setTicker(new Ticker("Aperte Incluir para inserir um item da conta..."));
		this.listaDespesasConta.addCommand(CommandHandler.incluirCmd);
		this.listaDespesasConta.addCommand(CommandHandler.exlcuirCmd);
		this.listaDespesasConta.addCommand(CommandHandler.fecharCmd);
		this.listaDespesasConta.addCommand(CommandHandler.backCmd);
		this.listaDespesasConta.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(this.listaDespesasConta, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void incluirNovaDespesa() {
		Form novaDespesa = new Form("Nova Despesa");
		novaDespesa.append(new TextField("Descrição", "", 20, TextField.ANY));
		novaDespesa.append(new TextField("Valor", "", 6, TextField.DECIMAL));
		novaDespesa.append(new ChoiceGroup("Dividir entre: ", ChoiceGroup.MULTIPLE, this.butequeirosString, null));
		
		novaDespesa.addCommand(CommandHandler.okCmd);
		novaDespesa.addCommand(CommandHandler.backCmd);
		novaDespesa.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(novaDespesa, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void salvarNovaDespesa(String descricao, double valor, Vector butequeiros) {
		Despesa novaDespesa = new Despesa(descricao, valor);
		
		for ( int i = 0; i < butequeiros.size(); i++ )
			novaDespesa.incluiButequeiro((Butequeiro)this.butequeiros.get((String)butequeiros.elementAt(i)));
		
		if ( this.despesas == null )
			this.despesas = new Vector();
		
		this.despesas.addElement(novaDespesa);
		this.listaDespesasConta.append(descricao + " - R$ " + valor, null);
		
		try {
			DisplayManager.getInstance().backAndRemove();
		} catch (DisplayManagerException e) {}
	}
	
	public void excluirDespesa(int selectedIndex) {
		this.despesas.removeElementAt(selectedIndex);
		this.listaDespesasConta.delete(selectedIndex);
	}
	
	public void fecharConta() {
		Form contaFechada = new Form("Conta Fechada!");
		
		//Calcular total da conta
		double totalConta = 0;
		for ( int i = 0; i < this.despesas.size(); i++ ) {
			totalConta += ((Despesa)this.despesas.elementAt(i)).getValor();
			((Despesa)this.despesas.elementAt(i)).fecharDespesa();
		}
		
		if ( this.incluirDezPorCento )
			totalConta *= 1.1;
		
		for ( int i = 0; i < this.butequeirosString.length; i++ ) {
			double valorButequeiro = ((Butequeiro)this.butequeiros.get(this.butequeirosString[i])).getValorContaButequeiro();
			if ( this.incluirDezPorCento )
				valorButequeiro *= 1.1;
			
			StringItem butequeiroConta = new StringItem(this.butequeirosString[i],
					"R$ "+valorButequeiro+"\n");
			butequeiroConta.setDefaultCommand(CommandHandler.detalheCmd);
			butequeiroConta.setItemCommandListener(CommandHandler.getInstance());
			contaFechada.append(butequeiroConta);
		}
		
		contaFechada.append(new StringItem("Total da Conta ", "R$ "+totalConta));
		contaFechada.addCommand(CommandHandler.menuCmd);
		contaFechada.addCommand(CommandHandler.exitCmd);
		contaFechada.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(contaFechada, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void exibirDetalheButequeiro(String nomeButequeiro) {
		Form detalheButequeiro = new Form("Detalhe "+nomeButequeiro);
		Butequeiro butequeiroDetalhe = (Butequeiro)this.butequeiros.get(nomeButequeiro);
		
		Vector despesaNome = butequeiroDetalhe.getDespesasNome();
		Vector despesaValor = butequeiroDetalhe.getDespesasValor();
		
		for ( int i = 0; i < despesaNome.size(); i++ )
			detalheButequeiro.append(new StringItem((String)despesaNome.elementAt(i), 
					" R$ "+((Double)despesaValor.elementAt(i)).doubleValue()+"\n"));
		
		detalheButequeiro.addCommand(CommandHandler.backCmd);
		detalheButequeiro.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(detalheButequeiro, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void zerarDados() {
		this.butequeiros = null;
		this.butequeirosString = null;
		this.despesas = null;
	}
	
	public void showAboutForm() {
		Form aboutForm = new Form("Sobre");
		aboutForm.append(new StringItem("Conta di Buteco", "Dividindo a conta do buteco sem quebrar a cabeça!!!\n"));
		aboutForm.append(new StringItem("Autor: ", "Neto Marin\n"));
		aboutForm.append(new StringItem("Contato: ", "netomarin@gmail.com\n"));
		
		aboutForm.addCommand(CommandHandler.backCmd);
		aboutForm.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(aboutForm, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
}