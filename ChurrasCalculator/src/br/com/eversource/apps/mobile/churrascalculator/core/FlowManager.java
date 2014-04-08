package br.com.eversource.apps.mobile.churrascalculator.core;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import br.com.eversource.apps.mobile.churrascalculator.ChurrasCalculatorMIDlet;
import br.com.eversource.apps.mobile.churrascalculator.persistence.Churrasco;

public class FlowManager {

	private static FlowManager instance;
	private MIDlet midlet;
	
	private Churrasco churrasco;
	private Churrasco[] churrascosSalvos;

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
	
	public void exibirDadosChurrasForm() {
		Form dadosChurras = new Form("Churras Calculator");
		dadosChurras.append(new TextField("Num. de Homens", "", 3, TextField.NUMERIC));
		dadosChurras.append(new TextField("Num. de Mulheres", "", 3, TextField.NUMERIC));
		dadosChurras.append(new ChoiceGroup("", Choice.MULTIPLE, new String[]{" Com Caipirinha"}, new Image[]{null}));
		dadosChurras.append(new Gauge("Nível Alcoólico", true, 5, 3));
		dadosChurras.append(new Gauge("Qtd. Comida", true, 5, 3));
		dadosChurras.addCommand(CommandHandler.calcularCmd);
		dadosChurras.addCommand(CommandHandler.backCmd);
		dadosChurras.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(dadosChurras, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void calcularChurras() {
		Form dadosChurras = null;

		try {
			dadosChurras = (Form)DisplayManager.getInstance().getCurrent();
		} catch (DisplayManagerNotStartedException e) {}

		//Obtendo dados
		int qtdHomens = 0;
		int qtdMulheres = 0;
		try {
			qtdHomens = Integer.parseInt(((TextField)dadosChurras.get(0)).getString());
			qtdMulheres = Integer.parseInt(((TextField)dadosChurras.get(1)).getString());
		} catch (NumberFormatException nfe) {
			qtdHomens = qtdMulheres = -1;
		}

		boolean comCaipirinha = ((ChoiceGroup)dadosChurras.get(2)).isSelected(0);
		int nivelAlcoolico = ((Gauge)dadosChurras.get(3)).getValue();
		int qtdComida = ((Gauge)dadosChurras.get(4)).getValue();

		//Tratando preenchimento
		if ( qtdHomens < 0 || qtdMulheres < 0 ) {
			try {
				DisplayManager.getInstance().show(new Alert("Atenção!", "Número de homens e mulheres é obrigatório!", null, AlertType.ERROR));
			} catch (DisplayManagerException e) {}
		} else if ( nivelAlcoolico <= 0 || qtdComida <= 0 ) {
			try {
				DisplayManager.getInstance().show(new Alert("Atenção!", "Todo churrasco tem que ter bebida e comida!!!", null, AlertType.ERROR));
			} catch (DisplayManagerException e) {}
		} else {
			//calculando churras
			churrasco = new Churrasco(qtdHomens, qtdMulheres, nivelAlcoolico, qtdComida, comCaipirinha);
			churrasco.calcularChurrasco();
			exibirChurras();
		}
	}
	
	public void exibirChurras() {
		//Montando resumo
		Form listaChurras = new Form("Lista Churras");
		listaChurras.append("\nHomens: "+churrasco.getHomens());
		listaChurras.append("\nMulheres: "+churrasco.getMulheres());
		listaChurras.append("\nLinguiça: "+churrasco.getLinguica()+" kg");
		listaChurras.append("\nCarne: "+churrasco.getCarne()+" kg");
		listaChurras.append("\nCerveja: "+churrasco.getCerveja()+" latas");
		listaChurras.append("\nRefrigerante: "+churrasco.getRefrigerante()+" Garrafas 2L");
		listaChurras.append("\nPães: "+churrasco.getPao()+" un");
		listaChurras.append("\nFarofa: "+churrasco.getFarofa()+" un");
		listaChurras.append("\nCarvão: "+churrasco.getCarvao()+" sacos");
		listaChurras.append("\nGuardanapos: "+churrasco.getGuardanapo()+" emb. c/50");
		listaChurras.append("\nÁlcool: "+churrasco.getAlcool()+" L");
		listaChurras.append("\nPano de Prato: "+churrasco.getPanoDePrato()+" un");
		listaChurras.append("\nFacas Churrasqueiro: "+churrasco.getFaca()+" un");
		listaChurras.append("\nPratos: "+churrasco.getPrato()+" un");
		listaChurras.append("\nGarfos: "+churrasco.getGarfo()+" un");
		listaChurras.append("\nCopos Plásticos: "+churrasco.getCopoPlastico()+" emb. c/20");
		listaChurras.append("\nSal Grosso: "+churrasco.getSalGrosso()+" kg");
		
		if ( churrasco.isComCaipirinha() ) {
			listaChurras.append("\nLimão: "+churrasco.getLimao()+" un");
			listaChurras.append("\nCachaça: "+churrasco.getCachaca()+" L");
			listaChurras.append("\nAçucar: "+churrasco.getAcucar()+" kg");
		}
		
		if ( this.churrasco.getID() < 0 )
			listaChurras.addCommand(CommandHandler.saveCmd);
		
		listaChurras.addCommand(CommandHandler.backCmd);
		listaChurras.addCommand(CommandHandler.exitCmd);
		listaChurras.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(listaChurras, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void salvarChurrasco() {
		Form salvarChurrasForm = new Form("Salvar Churras");
		salvarChurrasForm.append(new TextField("Para salvar o churrasco indique uma descrição: ", "", 30, TextField.ANY));
		salvarChurrasForm.addCommand(CommandHandler.finalizarCmd);
		salvarChurrasForm.addCommand(CommandHandler.backCmd);
		salvarChurrasForm.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(salvarChurrasForm, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
	
	public void finalizarPersistenciaChurras() {
		Form dadosChurras = null;
		
		try {
			dadosChurras = (Form)DisplayManager.getInstance().getCurrent();
		} catch (DisplayManagerNotStartedException e) {}
		
		
		churrasco.setDescricao(((TextField)dadosChurras.get(0)).getString());
		RecordStore db = null;
		try {
			db = RecordStore.openRecordStore(ChurrasCalculatorMIDlet.RMS_CHURRASCOS, true);
			churrasco.persistence(Churrasco.SAVE, db);
		} catch (RecordStoreException e) {} catch (IOException e) {
		} finally {
			if ( db != null )
				try {
					db.closeRecordStore();
				} catch (RecordStoreException e) {}
		}
		
		try {
			DisplayManager.getInstance().backToTheFirst();
			DisplayManager.getInstance().show(new Alert("Sucesso", "Churras salvo!", null, AlertType.CONFIRMATION));
		} catch (DisplayManagerException e) {}
	}
	
	public void exibirListaChurrasSalvos() {
		List churrasSalvosList = new List("Churras Salvos", List.EXCLUSIVE|List.IMPLICIT);
		churrasSalvosList.setFitPolicy(Choice.TEXT_WRAP_OFF);

		RecordStore db = null;
		try {
			db = RecordStore.openRecordStore(ChurrasCalculatorMIDlet.RMS_CHURRASCOS, true);
			this.churrascosSalvos = Churrasco.getRecordsArray(null, null, db);
		} catch (RecordStoreException e) {} catch (IOException e) {
		} finally {
			if ( db != null )
				try {
					db.closeRecordStore();
				} catch (RecordStoreException e) {}
		}

		if ( churrascosSalvos.length == 0 ) {
			try {
				DisplayManager.getInstance().show(new Alert("Atenção", "Não há churrascos salvos.", null, AlertType.INFO));
			} catch (DisplayManagerException e) {}
		} else {
			for ( int i = 0; i < churrascosSalvos.length; i++ )
				churrasSalvosList.append(churrascosSalvos[i].getDescricao(), null);

			churrasSalvosList.addCommand(CommandHandler.verCmd);
			churrasSalvosList.addCommand(CommandHandler.delCmd);
			churrasSalvosList.addCommand(CommandHandler.backCmd);
			churrasSalvosList.setCommandListener(CommandHandler.getInstance());

			try {
				DisplayManager.getInstance().show(churrasSalvosList, true);
			} catch (DisplayManagerNotStartedException e) {}
		}
	}
	
	public void exibirChurrasSalvo(int selectedIndex) {
		this.churrasco = this.churrascosSalvos[selectedIndex];
		this.exibirChurras();
	}
	
	public void apagarChurrasSalvo(int selectedIndex) {
		try {
			this.churrascosSalvos[selectedIndex].persistence(Churrasco.DELETE, RecordStore.openRecordStore(ChurrasCalculatorMIDlet.RMS_CHURRASCOS, true));
			DisplayManager.getInstance().removeFromToShowStack();
			this.exibirListaChurrasSalvos();
		} catch (IOException e) {
		} catch (RecordStoreException e) {			
		} catch (DisplayManagerException e) {}
	}
	
	public void showAboutForm() {
		Form aboutForm = new Form("Sobre");
		aboutForm.append(new StringItem("Churras Calculator beta", "Calculadora de itens para um bom churrasco."));
		aboutForm.append(new StringItem("Autor: ", "Neto Marin"));
		aboutForm.append(new StringItem("Contato: ", "netomarin@gmail.com"));
		
		aboutForm.addCommand(CommandHandler.backCmd);
		aboutForm.setCommandListener(CommandHandler.getInstance());
		
		try {
			DisplayManager.getInstance().show(aboutForm, true);
		} catch (DisplayManagerNotStartedException e) {}
	}
}