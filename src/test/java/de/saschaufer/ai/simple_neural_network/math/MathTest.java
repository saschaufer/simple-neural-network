package de.saschaufer.ai.simple_neural_network.math;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class MathTest {

    @ParameterizedTest
    @MethodSource
    void sigmoid(final double x, final double expected) {
        assertThat(Math.sigmoid(x), closeTo(expected, 0.000000000000005d));
    }

    static Stream<Arguments> sigmoid() {
        return Stream.of(
                Arguments.of(-2.0d, 0.119202922022118d),
                Arguments.of(-1.9d, 0.130108474362998d),
                Arguments.of(-1.8d, 0.141851064900488d),
                Arguments.of(-1.7d, 0.154465265083535d),
                Arguments.of(-1.6d, 0.167981614866076d),
                Arguments.of(-1.5d, 0.182425523806356d),
                Arguments.of(-1.4d, 0.197816111441418d),
                Arguments.of(-1.3d, 0.214165016957441d),
                Arguments.of(-1.2d, 0.231475216500982d),
                Arguments.of(-1.1d, 0.249739894404883d),
                Arguments.of(-1.0d, 0.268941421369995d),
                Arguments.of(-0.9d, 0.289050497374996d),
                Arguments.of(-0.8d, 0.310025518872388d),
                Arguments.of(-0.7d, 0.331812227831834d),
                Arguments.of(-0.6d, 0.354343693774205d),
                Arguments.of(-0.5d, 0.377540668798145d),
                Arguments.of(-0.4d, 0.401312339887548d),
                Arguments.of(-0.3d, 0.425557483188341d),
                Arguments.of(-0.2d, 0.450166002687522d),
                Arguments.of(-0.1d, 0.47502081252106d),
                Arguments.of(+0.0d, 0.5d),
                Arguments.of(+0.1d, 0.52497918747894d),
                Arguments.of(+0.2d, 0.549833997312478d),
                Arguments.of(+0.3d, 0.574442516811659d),
                Arguments.of(+0.4d, 0.598687660112452d),
                Arguments.of(+0.5d, 0.622459331201855d),
                Arguments.of(+0.6d, 0.645656306225795d),
                Arguments.of(+0.7d, 0.668187772168166d),
                Arguments.of(+0.8d, 0.689974481127612d),
                Arguments.of(+0.9d, 0.710949502625004d),
                Arguments.of(+1.0d, 0.731058578630005d),
                Arguments.of(+1.1d, 0.750260105595118d),
                Arguments.of(+1.2d, 0.768524783499018d),
                Arguments.of(+1.3d, 0.785834983042559d),
                Arguments.of(+1.4d, 0.802183888558582d),
                Arguments.of(+1.5d, 0.817574476193644d),
                Arguments.of(+1.6d, 0.832018385133924d),
                Arguments.of(+1.7d, 0.845534734916465d),
                Arguments.of(+1.8d, 0.858148935099512d),
                Arguments.of(+1.9d, 0.869891525637002d),
                Arguments.of(+2.0d, 0.880797077977883d)
        );
    }
}
