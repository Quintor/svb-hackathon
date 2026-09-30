package nl.svb.bre.engine.domain;

import java.time.LocalDate;
import java.util.List;

public record TestObject(
        Long persoonId,
        List<Long> kindIds,

        Boolean normaalLevenspatroonPersoon,
        Boolean gewensteOmstandigheid,
        Boolean voorzieneOmstandigheid,
        Boolean nakomenFeitelijkOnmogelijk,
        Boolean svbWasTijdigOpDeHoogte,
        Boolean emotioneleOntwrichting,
        String beleidsvoorbeeldEmotioneleOntwrichting,
        Boolean buitengewoneOmstandigheid,
        Boolean administratiefOnbekwaam,
        Boolean heeftOnjuisteInlichtingenTijdigHersteld,
        Boolean heeftInlichtingenVerstrektBijSvbControle,
        String soortOvertredenVerplichting,
        Boolean heeftRecidiveNietTijdigReagerenBinnenTweeJaar,
        Boolean heeftRecidiveNietReagerenBinnenTweeJaar,
        Boolean heeftMaatregelwaarschuwingBinnenTweeJaar,
        Boolean schorsingsbeslissingGenomen,
        Boolean isOvertredingGedeeltelijkVerwijtbaar,
        Boolean isOvertredingMedeTeWijtenAanSvb,
        Boolean isMeldplichtigeVerhuizingBuitenland,
        Boolean verhuizingTijdigGemeldNaVerzoek,
        Boolean heeftSvbInhoudingenZvwOfWlzOpgevraagd,
        Boolean heeftSvbInformatieGevraagdBijBezwaar,
        Boolean isBetalingGestaaktOpVerzoek,
        String overtredenAkwVerplichting,
        Boolean isMedewerkingsplichtigAkw,
        Boolean isInstelling,
        Boolean isInhoudingsuitkeringBeeindigd,
        Boolean uitkeringKanHerleven,
        Boolean heeftSvbBoetewaarschuwingVoorZelfdeGedraging,
        Boolean heeftSvbBoeteVoorZelfdeGedraging,
        Boolean isDringendeRedenAanwezig,
        Boolean heeftOvertredingBenadelingsbedrag,

        LocalDate geboortedatumKind1,
        LocalDate geboortedatumKind2
) {
}
