package uness;

public enum LANG {
	
	OK("Ok"),
	ERROR_SERIAL_PATTERN("Le serial ne correspond pas au pattern: "),
	ERROR_EMPTY_VALUE("Le champ suivant est vide: "),
	ERROR_WRONG_COMBO_VALUE("La valeur du champ suivant est invalide: "),
	ERROR_IMPORT_REQUIRED_FIELD("Un ou plusieurs champs obligatoires non trouvés"),
	TAB_DEVICE("Machine"),
	TAB_BROWSE("Parcourir"),
	TAB_IMPORT("Importer"),
	TAB_SEARCH("Recherche"),
	TAB_MANAGE("Gestion"),
	TAB_RMA("RMA"),
	DEVICE_EDITOR_NCBUTTON_TEXT("Non connu"),
	IMPORT_HEADERS_L1("Champs obligatoires"),
	IMPORT_HEADERS_L2("Champs possibles"),
	IMPORT_HEADERS_TITLE("Aide"),
	DEVICE_EDITOR_NCBUTTON_TOOLTIP("Génère un numéro de série arbitraire pour les machines dont le numéro de série n'est pas récupérable\nVérifier que la machine n'a pas déjà reçu un numéro de série arbitraire sinon cela produira un doublon"),
	IMPORT_ERROR_DUPLICATE_SERIAL("Doublon détecté pour le serial: "),
	RMA_TITLE_NEW("Nouvelle Demande de RMA"),
	RMA_TITLE_RETURN("Retour de RMA");
	
	private final String Language = "fr";
	private String fr;
	
	LANG(String p_fr){
		fr = p_fr;
	}
	
	public String get(){
		if(Language.equals("fr")) return this.fr;
		else return "";
	}
	
	
}