package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class D1111_OvertredenSoortVerplichtingAKW extends Rule<String> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW;
    }

    @Override
    protected Waarde<String> executeRule(CalculationContext calculationContext) {
        var result = switch (calculationContext.getTestObject().overtredenAkwVerplichting()) {
            case "Een wijziging in het adres van de niet in de BRP ingeschreven kinderbijslaggerechtigde onverwijld melden",
                 "Het mogelijk maken van controle door personen die daarmee door de bank zijn belast",
                 "Het kind op verzoek een geneeskundig onderzoek doen ondergaan" ->
                    "nakomen overige controlevoorschriften";
            case "Het op verzoek verschijnen aan het SVB-loket om de gevraagde gegevens te verstrekken indien de klant in Nederland woont",
                 "Het op verzoek verschijnen op een door de SVB aangewezen kantoor om de gevraagde gegevens te verstrekken indien de klant in het buitenland woont" ->
                    "nakomen verplichting tweede categorie";
            case "Het op verzoek ter inzage geven van boeken, documenten en andere informatiedragers en het ter beschikking stellen hiervan voor het maken van een kopie op een door de SVB bepaald tijdstip",
                 "Het op verzoek direct ter inzage verstrekken van een geldig identificatiebewijs en het ter beschikking stellen hiervan voor het maken van een kopie",
                 "Het op verzoek verstrekken van informatie met behulp van door de SVB ter beschikking gestelde formulieren en het op verzoek overleggen van bewijsstukken binnen de door de SVB gestelde termijn",
                 "Het op verzoek overleggen van een door een bevoegde autoriteit gewaarmerkt levensbewijs van degene (aanvrager of kind) die buiten Nederland woont",
                 "Kind < 16: het doen invullen en ondertekenen van een schoolverklaring door de onderwijsinstelling voor het kind dat uitwonend is in verband met onderwijs. De verklaring moet binnen de gestelde termijn aan de SVB toekomen",
                 "Kind < 16: het op verzoek invullen, ondertekenen en dateren van een onderhoudsverklaring t.a.v. het uitwonende kind. Daarnaast het op verzoek doen toekomen van betaalbewijzen van de bijdrage in het onderhoud. Dit alles binnen de door de SVB gestelde termijn",
                 "Kind < 16: het op verzoek verstrekken van het adres van een uitwonend (wordend) kind",
                 "Kind < 16: het op verzoek overleggen van een medische verklaring t.a.v. het kind dat in verband met ziekte of gebreken uitwonend is. Dit alles binnen de door de SVB gestelde termijn",
                 "Kind < 16: het op verzoek verstrekken van het adres van een uitwonend (wordend) kind en het op verzoek overleggen van bewijsstukken waaruit blijkt dat het kind uitwonend is en wat de reden daarvan is. Dit alles binnen de door de SVB gestelde termijn",
                 "Kind >= 16: het op verzoek invullen ondertekenen en dateren van een onderhoudsverklaring t.a.v. het uitwonende kind. Daarnaast het op verzoek doen toekomen van betaalbewijzen van de bijdrage in het onderhoud. Dit alles binnen de door de SVB gestelde termijn",
                 "Kind >= 16: het doen invullen en ondertekenen van een schoolverklaring door de onderwijsinstelling voor het kind dat onderwijs volgt. De verklaring moet binnen de gestelde termijn aan de SVB toekomen",
                 "Kind >= 16: het op verzoek verstrekken van het adres van een uitwonend (wordend) kind en het op verzoek overleggen van bewijsstukken waaruit blijkt dat het kind uitwonend is. Dit alles binnen de door de SVB gestelde termijn" ->
                    "reageren op een informatieverzoek";
            case null, default -> throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D1111);
        };
        return new Waarde<>(result, null);
    }
}
