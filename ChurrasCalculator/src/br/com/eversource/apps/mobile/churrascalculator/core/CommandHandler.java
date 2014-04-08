package br.com.eversource.apps.mobile.churrascalculator.core;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;



public class CommandHandler implements CommandListener {
	
	private static CommandHandler myInstance;
	
	/*
	 * Comandos
	 */
	public static Command calcularCmd = new Command("Calcular", Command.SCREEN, 1);
	public static Command salvosCmd = new Command("Churras Salvos", Command.SCREEN, 2);
	public static Command configCmd = new Command("Configuraões", Command.SCREEN, 3);
	public static Command verCmd = new Command("Exibir Churras", Command.OK, 1);
	public static Command delCmd = new Command("Apagar Churras", Command.SCREEN, 2);
	public static Command aboutCmd = new Command("Sobre", Command.HELP, 4);
	public static Command saveCmd = new Command("Salvar Churras", Command.SCREEN, 1);
	public static Command finalizarCmd = new Command("Finalizar", Command.SCREEN, 1);
	public static Command backCmd = new Command("Voltar", Command.BACK, 2);
	public static Command exitCmd = new Command("Sair", Command.EXIT, 5);
	
	private CommandHandler() {}
	
	public static CommandHandler getInstance() {
		if ( myInstance == null )
			myInstance = new CommandHandler();
		
		return myInstance;
	}

	public void commandAction(Command command, Displayable displayable) {
		if ( command == CommandHandler.backCmd ) {
			try {
				DisplayManager.getInstance().backAndRemove();
			} catch (DisplayManagerException e1) {}
		}else if ( command == CommandHandler.exitCmd ) {
			try {
				FlowManager.getInstance().endApplication();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command.equals(CommandHandler.calcularCmd) ) {
			if (displayable instanceof Canvas) {
				//vindo da tela inicial
				try {
					FlowManager.getInstance().exibirDadosChurrasForm();
				} catch (FlowManagerNotStartedException e) {}
			} else {
				//vindo do form de dados
				try {
					FlowManager.getInstance().calcularChurras();
				} catch (FlowManagerNotStartedException e) {}
			}
		} else if ( command == CommandHandler.saveCmd ) {
			try {
				FlowManager.getInstance().salvarChurrasco();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.finalizarCmd ) {
			try {
				FlowManager.getInstance().finalizarPersistenciaChurras();
			} catch (FlowManagerNotStartedException e) {}
		}else if ( command == CommandHandler.salvosCmd ) {
			try {
				FlowManager.getInstance().exibirListaChurrasSalvos();
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.verCmd ) {
			try {
				FlowManager.getInstance().exibirChurrasSalvo(((List)displayable).getSelectedIndex());
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.delCmd ) {
			try {
				FlowManager.getInstance().apagarChurrasSalvo(((List)displayable).getSelectedIndex());
			} catch (FlowManagerNotStartedException e) {}
		} else if ( command == CommandHandler.aboutCmd ) {
			try {
				FlowManager.getInstance().showAboutForm();
			} catch (FlowManagerNotStartedException e) {}
		}
	}
}