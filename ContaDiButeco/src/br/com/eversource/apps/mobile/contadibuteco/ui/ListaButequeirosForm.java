package br.com.eversource.apps.mobile.contadibuteco.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextField;

import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManager;
import br.com.eversource.apps.mobile.contadibuteco.core.DisplayManagerException;
import br.com.eversource.apps.mobile.contadibuteco.core.FlowManager;
import br.com.eversource.apps.mobile.contadibuteco.core.FlowManagerNotStartedException;

public class ListaButequeirosForm extends Form implements CommandListener, ItemCommandListener {
	
	private TextField butequeiro;
	private ChoiceGroup incluiDezPorCento;
	private ChoiceGroup listaButequeiros;
	
	private Command incluirCmd = new Command("Incluir", Command.SCREEN, 1);
	private Command continuarCmd = new Command("Continuar", Command.SCREEN, 2);
	private Command excluirCmd = new Command("Excluir", Command.SCREEN, 3);
	private Command voltarCmd = new Command("Voltar", Command.BACK, 4);

	public ListaButequeirosForm() {
		super("Lista dos Butequeiros");
		
		this.butequeiro = new TextField("Butequeiro", "", 20, TextField.ANY);
		this.incluiDezPorCento = new ChoiceGroup("", ChoiceGroup.MULTIPLE, new String[] {"Incluir 10%"}, null);
		this.listaButequeiros = new ChoiceGroup("Butequeiros:", ChoiceGroup.MULTIPLE);
		this.listaButequeiros.addCommand(this.excluirCmd);
		this.listaButequeiros.setItemCommandListener(this);
		
		this.append(this.butequeiro);
		this.append(this.incluiDezPorCento);
		this.append(this.listaButequeiros);
		this.addCommand(this.incluirCmd);
		this.addCommand(this.continuarCmd);
		this.addCommand(this.voltarCmd);
		this.setCommandListener(this);
	}

	public void commandAction(Command command, Displayable displayable) {
		if ( command.equals(this.incluirCmd)) {
			if ( this.butequeiro.getString() == null || this.butequeiro.getString().length() == 0 ) {
				try {
					DisplayManager.getInstance().show(new Alert("Atenção!", "O nome do butequeiro deve se preenchido!", null, AlertType.ERROR));
				} catch (DisplayManagerException e) {}
			} else {

					boolean jaExiste = false;
					for ( int i = 0; i < this.listaButequeiros.size(); i++)
						if ( this.listaButequeiros.getString(i).equals(this.butequeiro.getString()))
							jaExiste = true;

					if ( !jaExiste )
						this.listaButequeiros.append(this.butequeiro.getString(), null);
					else
						try {
							DisplayManager.getInstance().show(new Alert("Atenção!", this.butequeiro.getString()+" já existe na lista!", null, AlertType.INFO));
						} catch (DisplayManagerException e) {}

						this.butequeiro.setString("");
				}
		} else if ( command.equals(this.continuarCmd) ) {
			String[] butequeiros = new String[this.listaButequeiros.size()];
			for ( int i = 0; i < this.listaButequeiros.size(); i++ )
				butequeiros[i] = this.listaButequeiros.getString(i);
			
			try {
				FlowManager.getInstance().lancarConta(butequeiros, this.incluiDezPorCento.isSelected(0));
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command.equals(this.voltarCmd)) {
			try {
				DisplayManager.getInstance().backAndRemove();
			} catch (DisplayManagerException e) {}
		}
	}

	public void commandAction(Command command, Item item) {
		if ( command.equals(this.excluirCmd)) {
			boolean[] selecionados = new boolean[this.listaButequeiros.size()];
			this.listaButequeiros.getSelectedFlags(selecionados);
			
			for ( int i = selecionados.length-1; i >= 0; i-- )
				if ( selecionados[i] )
					this.listaButequeiros.delete(i);
		}
	}
}