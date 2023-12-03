package de.saschaufer.ai.simple_neural_network.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatrixTest {

    @Test
    void init() {

        // Row 0
        final RuntimeException e1 = assertThrows(RuntimeException.class, () -> new Matrix(new double[0][1]));
        assertThat(e1.getMessage(), is("The rows and columns must not be 0."));

        // Column 0
        final RuntimeException e2 = assertThrows(RuntimeException.class, () -> new Matrix(new double[1][0]));
        assertThat(e2.getMessage(), is("The rows and columns must not be 0."));

        // Row 0
        final RuntimeException e3 = assertThrows(RuntimeException.class, () -> new Matrix(0, 1));
        assertThat(e3.getMessage(), is("The rows and columns must not be 0."));

        // Column 0
        final RuntimeException e4 = assertThrows(RuntimeException.class, () -> new Matrix(1, 0));
        assertThat(e4.getMessage(), is("The rows and columns must not be 0."));
    }

    @ParameterizedTest
    @MethodSource
    void multiply_error(final double[][] matrix1, final double[][] matrix2, final String error) {

        final Matrix m1 = new Matrix(matrix1);
        final Matrix m2 = new Matrix(matrix2);

        final RuntimeException e = assertThrows(RuntimeException.class, () -> m1.multiply(m2));
        assertThat(e.getMessage(), is(error));
    }

    static Stream<Arguments> multiply_error() {
        return Stream.of(
                Arguments.of(new double[1][2], new double[1][1], "The number of columns of the first matrix must be equal to the number of rows of the second matrix.")
        );
    }

    @ParameterizedTest
    @MethodSource
    void multiply(final double[][] matrix1, final double[][] matrix2, final double[][] expected) {

        final Matrix m1 = new Matrix(matrix1);
        final Matrix m2 = new Matrix(matrix2);

        final double[][] r = assertDoesNotThrow(() -> m1.multiply(m2).getElements());

        assertThat(r.length, is(expected.length));
        assertThat(r[0].length, is(expected[0].length));

        for (int row = 0; row < expected.length; row++) {
            for (int col = 0; col < expected[0].length; col++) {
                assertThat(r[row][col], closeTo(expected[row][col], 0.00000000001d));
            }
        }
    }

    static Stream<Arguments> multiply() {

        // 1
        final double[][] e1 = new double[3][5];
        e1[0] = new double[]{+0.02190732955932617200d, +0.01506781578063964800d, +0.26051509380340576000d, -0.35604012012481690000d, -0.49197161197662354000d};
        e1[1] = new double[]{+0.15386068820953370000d, -0.00557482242584228500d, -0.45927596092224120000d, +0.24635648727416992000d, +0.04881441593170166000d};
        e1[2] = new double[]{+0.52082979679107670000d, +0.68008792400360110000d, +0.34531807899475100000d, -0.41926717758178710000d, +0.90059077739715580000d};

        final double[][] e2 = new double[5][3];
        e2[0] = new double[]{+0.32242739200592040000d, +0.82502663135528560000d, -0.33802175521850586000d};
        e2[1] = new double[]{+0.28160929679870605000d, +0.40792763233184814000d, -0.32169830799102783000d};
        e2[2] = new double[]{+0.71531438827514650000d, -0.84714388847351070000d, +0.44464898109436035000d};
        e2[3] = new double[]{+0.39142179489135740000d, +0.19426822662353516000d, +0.90390753746032710000d};
        e2[4] = new double[]{-0.16796982288360596000d, -0.54222774505615230000d, -0.09091174602508545000d};

        final double[][] r1 = new double[3][3];
        r1[0] = new double[]{+0.14093148708343506000d, +0.00112029910087585450d, -0.17351602017879486000d};
        r1[1] = new double[]{-0.19225777685642242000d, +0.53512859344482420000d, -0.03618574142456055000d};
        r1[2] = new double[]{+0.29107749462127686000d, -0.15518456697463990000d, -0.70214259624481200000d};

        // 2
        final double[][] e3 = new double[3][2];
        e3[0] = new double[]{+0.83728885650634770000d, -0.79218900203704830000d};
        e3[1] = new double[]{+0.82897067070007320000d, -0.27140784263610840000d};
        e3[2] = new double[]{+0.32797718048095703000d, +0.68925142288208010000d};

        final double[][] e4 = new double[2][3];
        e4[0] = new double[]{+0.10134184360504150000d, +0.09074628353118896000d, -0.49031114578247070000d};
        e4[1] = new double[]{+0.17172610759735107000d, -0.01784658432006836000d, -0.59163200855255130000d};

        final double[][] r2 = new double[3][3];
        r2[0] = new double[]{-0.05118714272975921600d, +0.09011872112751007000d, +0.05815231800079346000d};
        r2[1] = new double[]{+0.03740160539746284500d, +0.08006971329450607000d, -0.24587997794151306000d};
        r2[2] = new double[]{+0.15160027146339417000d, +0.01746192574501037600d, -0.56859409809112550000d};

        return Stream.of(Arguments.of(e1, e2, r1), Arguments.of(e3, e4, r2));
    }

    @ParameterizedTest
    @MethodSource
    void multiply_scalar(final double[][] matrix, final double scalar, final double[][] expected) {

        final Matrix m = new Matrix(matrix);

        final double[][] r = assertDoesNotThrow(() -> m.multiply(scalar).getElements());

        assertThat(r.length, is(expected.length));
        assertThat(r[0].length, is(expected[0].length));

        for (int row = 0; row < expected.length; row++) {
            for (int col = 0; col < expected[0].length; col++) {
                assertThat(r[row][col], closeTo(expected[row][col], 0.00000000001d));
            }
        }
    }

    static Stream<Arguments> multiply_scalar() {

        // 1
        final double[][] m1 = new double[1][1];
        m1[0] = new double[]{1d};

        final double s1 = 0.25d;

        final double[][] e1 = new double[1][1];
        e1[0] = new double[]{0.25d};

        // 2
        final double[][] m2 = new double[2][2];
        m2[0] = new double[]{1d, 2d};
        m2[1] = new double[]{-4d, 4d};

        final double s2 = -0.25d;

        final double[][] e2 = new double[2][2];
        e2[0] = new double[]{-0.25d, -0.5d};
        e2[1] = new double[]{1d, -1d};

        // 3
        final double[][] m3 = new double[3][2];
        m3[0] = new double[]{1d, 2d};
        m3[1] = new double[]{-4d, 4d};
        m3[2] = new double[]{-1d, -2d};

        final double s3 = -1d;

        final double[][] e3 = new double[3][2];
        e3[0] = new double[]{-1d, -2d};
        e3[1] = new double[]{4d, -4d};
        e3[2] = new double[]{1d, 2d};

        return Stream.of(
                Arguments.of(m1, s1, e1),
                Arguments.of(m2, s2, e2),
                Arguments.of(m3, s3, e3)
        );
    }

    @ParameterizedTest
    @MethodSource
    void transpose(final double[][] in, final double[][] expected) {
        assertThat(new Matrix(in).transpose().getElements(), is(expected));
    }

    static Stream<Arguments> transpose() {

        // 1
        final double[][] m1 = new double[1][1];
        m1[0] = new double[]{1d};

        final double[][] e1 = new double[1][1];
        e1[0] = new double[]{1d};

        // 2
        final double[][] m2 = new double[2][2];
        m2[0] = new double[]{1d, 2d};
        m2[1] = new double[]{3d, 4d};

        final double[][] e2 = new double[2][2];
        e2[0] = new double[]{1d, 3d};
        e2[1] = new double[]{2d, 4d};

        // 3
        final double[][] m3 = new double[3][2];
        m3[0] = new double[]{1d, 2d};
        m3[1] = new double[]{3d, 4d};
        m3[2] = new double[]{5d, 6d};

        final double[][] e3 = new double[2][3];
        e3[0] = new double[]{1d, 3d, 5d};
        e3[1] = new double[]{2d, 4d, 6d};

        return Stream.of(
                Arguments.of(m1, e1),
                Arguments.of(m2, e2),
                Arguments.of(m3, e3)
        );
    }
}
