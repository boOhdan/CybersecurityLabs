package lab1.task5;

import java.util.*;
import java.util.stream.Collectors;

public class SubstitutionDecoderTest {

	public static void main(String[] args) {
		String encryptedText = "EFFPQLEKVTVPCPYFLMVHQLUEWCNVWFYGHYTCETHQEKLPVMSAKSPVPAPVYWMVHQLUSPQLYWLASLFVWPQLMVHQLUPLRPSQLULQESPBLWPCSVRVWFLHLWFLWPUEWFYOTCMQYSLWOYWYETHQEKLPVMSAKSPVPAPVYWHEPPLUWSGYULEMQTLPPLUGUYOLWDTVSQETHQEKLPVPVSMTLEUPQEPCYAMEWWYTYWDLUULTCYWPQLSEOLSVOHTLUYAPVWLYGDALSSVWDPQLNLCKCLRQEASPVILSLEUMQBQVMQCYAHUYKEKTCASLFPYFLMVHQLUPQLHULIVYASHEUEDUEHQBVTTPQLVWFLRYGMYVWMVFLWMLSPVTTBYUNESESADDLSPVYWCYAMEWPUCPYFVIVFLPQLOLSSEDLVWHEUPSKCPQLWAOKLUYGMQEUEMPLUSVWENLCEWFEHHTCGULXALWMCEWETCSVSPYLEMQYGPQLOMEWCYAGVWFEBECPYASLQVDQLUYUFLUGULXALWMCSPEPVSPVMSBVPQPQVSPCHLYGMVHQLUPQLWLRPOEDVMETBYUFBVTTPENLPYPQLWLRPTEKLWZYCKVPTCSTESQPBYMEHVPETCMEHVPETZMEHVPETKTMEHVPETCMEHVPETT";
		String decryptedText = "ADDTHEABILITYTODECIPHERANYKINDOFPOLYALPHABETICSUBSTITUTIONCIPHERSTHEONEUSEDINTHECIPHERTEXTSHEREHASTWENTYSIXINDEPENDENTRANDOMLYCHOSENMONOALPHABETICSUBSTITUTIONPATTERNSFOREACHLETTERFROMENGLISHALPHABETITISCLEARTHATYOUCANNOLONGERRELYONTHESAMESIMPLEROUTINEOFGUESSINGTHEKEYBYEXHAUSTIVESEARCHWHICHYOUPROBABLYUSEDTODECIPHERTHEPREVIOUSPARAGRAPHWILLTHEINDEXOFCOINCIDENCESTILLWORKASASUGGESTIONYOUCANTRYTODIVIDETHEMESSAGEINPARTSBYTHENUMBEROFCHARACTERSINAKEYANDAPPLYFREQUENCYANALYSISTOEACHOFTHEMCANYOUFINDAWAYTOUSEHIGHERORDERFREQUENCYSTATISTICSWITHTHISTYPEOFCIPHERTHENEXTMAGICALWORDWILLTAKETOTHENEXTLABENJOYBITLYSLASHTWOCAPITALYCAPITALJCAPITALBLCAPITALYCAPITALL";
		String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String key = "UWYGADFPVZBECKMTHXSLRINQOJ";

		List<GuessedKeySymbol> guessedKeySymbols = SubstitutionDecoder.getKeysPart(encryptedText, decryptedText, 1);
		var symbolsList = new ArrayList<>(guessedKeySymbols);
		symbolsList.sort(Comparator.comparingInt(GuessedKeySymbol::getIndex));
		String resultKey = symbolsList.stream().map(s -> s.getSymbol() + "").collect(Collectors.joining());

		System.out.println(key.equals(resultKey));
	}
}
