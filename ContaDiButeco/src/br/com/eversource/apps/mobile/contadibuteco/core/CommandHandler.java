package br.com.eversource.apps.mobile.contadibuteco.core;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;


public class CommandHandler implements CommandListener, ItemCommandListener {
	
	private static CommandHandler myInstance;
	
	/*
	 * Comandos
	 */
	public static Command calcularCmd = new Command("Nova Conta", Command.SCREEN, 1);
	public static Command aboutCmd = new Command("Sobre", Command.HELP, 3);
	public static Command incluirCmd = new Command("Incluir", Command.SCREEN, 1);
	public static Command exlcuirCmd = new Command("Excluir", Command.SCREEN, 2);
	public static Command fecharCmd = new Command("Calcular", Command.SCREEN, 5);
	public static Command okCmd = new Command("Ok", Command.OK, 1);
	public static Command detalheCmd = new Command("Detalhe", Command.SCREEN, 1);
	public static Command menuCmd = new Command("Inicio", Command.SCREEN, 2);
	public static Command backCmd = new Command("Voltar", Command.BACK, 5);
	public static Command exitCmd = new Command("Sair", Command.EXIT, 4);
	
	private CommandHandler() {}
	
	public static CommandHandler getInstance() {
		if ( myInstance == null )
			myInstance = new CommandHandler();
		
		return myInstance;
	}

	public void commandAction(Command command, Displayable displayable) {
		if ( command == CommandHandler.calcularCmd ) {
			try {
				FlowManager.getInstance().calcularNovaConta();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.backCmd ) {
			try {
				DisplayManager.getInstance().backAndRemove();
			} catch (DisplayManagerException e1) {}
		}else if ( command == CommandHandler.exitCmd ) {
			try {
				FlowManager.getInstance().endApplication();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.aboutCmd ) {
			try {
				FlowManager.getInstance().showAboutForm();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.exlcuirCmd ) {
			try {
				FlowManager.getInstance().excluirDespesa(((List)displayable).getSelectedIndex());
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.incluirCmd ) {
			try {
				FlowManager.getInstance().incluirNovaDespesa();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.okCmd ) {
			Form despesa = (Form)displayable;
			ChoiceGroup cgButequeiros = (ChoiceGroup)despesa.get(2);
			Vector butequeiros = new Vector();
			boolean[] butequeirosSelecionados = new boolean[cgButequeiros.size()];
			cgButequeiros.getSelectedFlags(butequeirosSelecionados);
			for ( int i = 0; i < cgButequeiros.size(); i++ )
				if ( butequeirosSelecionados[i])
					butequeiros.addElement(cgButequeiros.getString(i));
			
			if ( ((TextField)despesa.get(0)).getString().length() == 0 || ((TextField)despesa.get(1)).getString().length() == 0 ) {
				try {
					DisplayManager.getInstance().show(new Alert("Atenção!", "O nome do item e o valor são obrigatórios!", null, AlertType.ERROR));
				} catch (DisplayManagerException e) {}
			} else if (butequeiros.size() == 0 ) {
				try {
					DisplayManager.getInstance().show(new Alert("Atenção!", "Pelo menos um butequeiro tem que pagar! CALOTE NÃO!!!", null, AlertType.ERROR));
				} catch (DisplayManagerException e) {}
			} else {
				try {
					FlowManager.getInstance().salvarNovaDespesa(((TextField)despesa.get(0)).getString(), 
							Double.parseDouble(((TextField)despesa.get(1)).getString()), butequeiros);
				} catch (NumberFormatException e) {
				} catch (FlowManagerNotStartedException e) {}
			}
		} else if ( command == CommandHandler.fecharCmd ) {
			try {
				FlowManager.getInstance().fecharConta();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.menuCmd ) {			
			try {
				DisplayManager.getInstance().removeFromToShowStack();
				DisplayManager.getInstance().removeFromToShowStack();
				DisplayManager.getInstance().removeFromToShowStack();
				DisplayManager.getInstance().show();
				FlowManager.getInstance().zerarDados();
			} catch (DisplayManagerException e) {
			} catch (FlowManagerException e) {}
		}
	}

	public void commandAction(Command command, Item item) {
		StringItem butequeiroConta = (StringItem)item;
		try {
			FlowManager.getInstance().exibirDetalheButequeiro(butequeiroConta.getLabel());
		} catch (FlowManagerNotStartedException e) {}
	}
}