package br.com.eversource.apps.mobile.churrascalculator;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import br.com.eversource.apps.mobile.churrascalculator.core.CommandHandler;
import br.com.eversource.apps.mobile.churrascalculator.core.DisplayManager;
import br.com.eversource.apps.mobile.churrascalculator.core.DisplayManagerAlreadyStartedException;
import br.com.eversource.apps.mobile.churrascalculator.core.DisplayManagerException;
import br.com.eversource.apps.mobile.churrascalculator.core.DisplayManagerNotStartedException;
import br.com.eversource.apps.mobile.churrascalculator.core.FlowManager;
import br.com.eversource.apps.mobile.churrascalculator.core.FlowManagerException;
import br.com.eversource.apps.mobile.churrascalculator.ui.TelaInicialCanvas;

public class ChurrasCalculatorMIDlet extends MIDlet {	
	
	public static final String RMS_CHURRASCOS = "CHURRAS";
	public static final String RMS_CONFIGURACOES = "CONFIGURACOES";
	
	private TelaInicialCanvas telaInicial;
	
	public ChurrasCalculatorMIDlet() {
		
		try {
			DisplayManager.startDisplayManager(this);
			FlowManager.startFlowManager(this);
		} catch (DisplayManagerAlreadyStartedException e) {
		} catch (FlowManagerException e) {}
		
		this.telaInicial = new TelaInicialCanvas();
		this.telaInicial.addCommand(CommandHandler.calcularCmd);
		this.telaInicial.addCommand(CommandHandler.salvosCmd);
		this.telaInicial.addCommand(CommandHandler.aboutCmd);
		this.telaInicial.addCommand(CommandHandler.exitCmd);
		this.telaInicial.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().pushToShowStack(this.telaInicial);
		} catch (DisplayManagerNotStartedException e) {}
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		notifyDestroyed();
	}

	protected void pauseApp() {
		notifyPaused();
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			DisplayManager.getInstance().show();
		} catch (DisplayManagerException e) {}
	}
}