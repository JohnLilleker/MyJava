package joreli.items;

import joreli.Item;
import joreli.Type;

public class ItemParser {
	
	/**
	 * Given a String describing an item, this decodes it and returns the Item 
	 * @param input
	 * @return
	 * @throws Exception If any errors are encountered, they are thrown back to pokedex
	 */
	public static Item parse(String input) throws Exception {
		
		Item item = null;
		
		String[] components = input.split(" ");
		
		switch(components[0]) {
		
		case "Stone" :
			item = new Stone(components[1]);
			break;
			
		case "MISC" :
			item = new MiscItem(components[1], components[2].replace("+", " "));
			break;
			
		case "Evo" :
			item = new Evo(components[1], Type.valueOf(components[2]));
			break;
			
		case "Berry" :
			switch(components[2]) {
			case "HP":
				item = new HealthBerry(components[1], Berry.Taste.valueOf(components[3]), Float.valueOf(components[4]), HealthBerry.Method.valueOf(components[5]));
				break;
			case "condition":
				item = new ConditionBerry(components[1], components[3]);
				break;
			case "PP":
				item = new EnergyBerry(components[1], Integer.valueOf(components[3]));
				break;
			default :
				throw new Exception("Unknown Berry Type: " + components[2]);
			}
			break;
		
		case "Plate" :
			item = new TypeChange(components[1], TypeChange.Device.PLATE, Type.valueOf(components[2]));
			break;
			
		case "Drive" :
			item = new TypeChange(components[1], TypeChange.Device.DRIVE, Type.valueOf(components[2]));
			break;
			
		default :
			throw new Exception("Unknown Item Type: " + components[0]);
		}
		
		return item;
	}

}
