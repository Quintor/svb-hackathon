package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;
import static nl.svb.bre.domain.enums.Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;

@Component
public class D12111_DoorDeEmotioneleOntwrichtingIsDeOvertredingVerminderdToeTeRekenen extends Rule<Boolean> {

    private static final Set<String> EMOTIONELE_ONTWRICHTING = Set.of(
            "ernstige ziekte",
            "weglopen kind",
            "overlijden kind",
            "faillissement",
            "onvoorzien ontslag"
    );

    private static final Predicate<CalculationContext> containsEmotioneleOntwrichting = c -> EMOTIONELE_ONTWRICHTING.contains(c.getTestObject().beleidsvoorbeeldEmotioneleOntwrichting());

    @Override
    public Definitiecode getDefinitionCode() {
        return D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D113111_BUITENGEWONE_OMSTANDIGHEID, not(containsEmotioneleOntwrichting))
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return switch (calculationContext.getTestObject().beleidsvoorbeeldEmotioneleOntwrichting()) {
            case "ernstige ziekte", "weglopen kind", "overlijden kind", "faillissement", "onvoorzien ontslag" -> new Waarde<>(true, null);
            default -> checkD11311(calculationContext);
        };
    }

    private Waarde<Boolean> checkD11311(CalculationContext calculationContext) {
        Boolean d113111 = getCalculatedValue(calculationContext, D113111_BUITENGEWONE_OMSTANDIGHEID);
        if (d113111 == null) {
            throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D12111);
        }
        if (!d113111) {
            return new Waarde<>(false, null);
        }
        return new Waarde<>(calculationContext.getTestObject().emotioneleOntwrichting(), null);
    }
}
