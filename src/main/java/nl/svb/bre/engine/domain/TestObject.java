package nl.svb.bre.engine.domain;

import java.time.LocalDate;
import java.util.List;

public record TestObject (
        Long persoonId,
        List<Long> kindIds,

        boolean normaalLevenspatroonPersoon,
        boolean svbWasTijdigOpDeHoogte,
        boolean emotioneleOntwrichting,
        String beleidsvoorbeeldEmotioneleOntwrichting,
        boolean buitengewoneOmstandigheid,
        boolean administratiefOnbekwaam,
        boolean heeftOnjuisteInlichtingenTijdigHersteld,
        boolean heeftInlichtingenVerstrektBijSvbControle,
        String soortOvertredenVerplichting,
        boolean heeftRecidiveNietTijdigReagerenBinnenTweeJaar,
        boolean heeftRecidiveNietReagerenBinnenTweeJaar,
        boolean heeftMaatregelwaarschuwingBinnenTweeJaar,
        boolean schorsingsbeslissingGenomen,
        boolean isOvertredingGedeeltelijkVerwijtbaar,
        boolean isOvertredingMedeTeWijtenAanSvb,
        boolean isMeldplichtigeVerhuizingBuitenland,
        Boolean verhuizingTijdigGemeldNaVerzoek,
        Boolean heeftSvbInhoudingenZvwOfWlzOpgevraagd,
        Boolean heeftSvbInformatieGevraagdBijBezwaar,
        Boolean isBetalingGestaaktOpVerzoek,
        String overtredenAkwVerplichting,
        boolean isMedewerkingsplichtigAkw,
        boolean isInstelling,
        boolean isInhoudingsuitkeringBeeindigd,
        Boolean uitkeringKanHerleven,
        boolean heeftSvbBoetewaarschuwingVoorZelfdeGedraging,
        boolean heeftSvbBoeteVoorZelfdeGedraging,
        boolean isDringendeRedenAanwezig,
        boolean heeftOvertredingBenadelingsbedrag,

        LocalDate geboortedatumKind1,
        LocalDate geboortedatumKind2
) { }
