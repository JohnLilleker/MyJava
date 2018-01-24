package translator;

public class RunTranslator {

	public static void main(String[] args) {
		try {
//			String file = "src/translator/morseCode.txt";
			String file = "src/translator/dictionary1.txt";
			Translator t = new Translator(file);
//			System.out.println(t.translate("the dog is a dog with dog ears and dog legs"));
//			System.out.println(t.translate("All you talk about is the cat, I am fed up about the cat, talk about something that isn't the cat because I don't like the cat. the cat is stupid"));
//			System.out.println(t.translate("There are 20 dogs"));
//			System.out.println("Hello world -> " + t.translate("Hello World"));
			String declare = "Tis I, the Great Stone Dragon, as the Great Stone Dragon I will set off to find Mulan as is my job. Did I mention I was the Great Stone Dragon?";
			System.out.println(declare + "\n = ");
			System.out.println(" " + t.translate(declare));
//			System.out.println("Am I to pay you $17? = " + t.translate("Am I to pay you $17?"));
//			System.out.println("dog and cat and fish = " + t.translate("dog and cat and fish"));
//			System.out.println("catch the cat = " + t.translate("catch the cat"));
		} catch (TranslatorException | CodeException e) {
			e.printStackTrace();
		}
		
		
		//new TranslatorFrame();
		
		
	}

}
