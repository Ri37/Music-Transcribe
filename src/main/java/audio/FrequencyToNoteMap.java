package audio;

import java.util.Map;

public class FrequencyToNoteMap {
    private static final Map<Double, Integer> pitchToNoteMap = Map.ofEntries(
        // Octave 0
        Map.entry(16.35, -45), Map.entry(17.32, -44), Map.entry(18.35, -43),
        Map.entry(19.45, -42), Map.entry(20.60, -41), Map.entry(21.83, -40),
        Map.entry(23.12, -39), Map.entry(24.50, -38), Map.entry(25.96, -37),
        Map.entry(27.50, -36), Map.entry(29.14, -35), Map.entry(30.87, -34),

        // Octave 1
        Map.entry(32.70, -33), Map.entry(34.65, -32), Map.entry(36.71, -31),
        Map.entry(38.89, -30), Map.entry(41.20, -29), Map.entry(43.65, -28),
        Map.entry(46.25, -27), Map.entry(49.00, -26), Map.entry(51.91, -25),
        Map.entry(55.00, -24), Map.entry(58.27, -23), Map.entry(61.74, -22),

        // Octave 2
        Map.entry(65.41, -21), Map.entry(69.30, -20), Map.entry(73.42, -19),
        Map.entry(77.78, -18), Map.entry(82.41, -17), Map.entry(87.31, -16),
        Map.entry(92.50, -15), Map.entry(98.00, -14), Map.entry(103.83, -13),
        Map.entry(110.00, -12), Map.entry(116.54, -11), Map.entry(123.47, -10),

        // Octave 3 (ends at -1)
        Map.entry(130.81, -9), Map.entry(138.59, -8), Map.entry(146.83, -7),
        Map.entry(155.56, -6), Map.entry(164.81, -5), Map.entry(174.61, -4),
        Map.entry(185.00, -3), Map.entry(196.00, -2), Map.entry(207.65, -1),

        // Octave 4 (middle C is 0)
        Map.entry(220.00, 0), Map.entry(233.08, 1), Map.entry(246.94, 2),
        Map.entry(261.63, 3), Map.entry(277.18, 4), Map.entry(293.66, 5),
        Map.entry(311.13, 6), Map.entry(329.63, 7), Map.entry(349.23, 8),
        Map.entry(369.99, 9), Map.entry(392.00, 10), Map.entry(415.30, 11),

        // Octave 5
        Map.entry(440.00, 12), Map.entry(466.16, 13), Map.entry(493.88, 14),
        Map.entry(523.25, 15), Map.entry(554.37, 16), Map.entry(587.33, 17),
        Map.entry(622.25, 18), Map.entry(659.25, 19), Map.entry(698.46, 20),
        Map.entry(739.99, 21), Map.entry(783.99, 22), Map.entry(830.61, 23),

        // Octave 6
        Map.entry(880.00, 24), Map.entry(932.33, 25), Map.entry(987.77, 26),
        Map.entry(1046.50, 27), Map.entry(1108.73, 28), Map.entry(1174.66, 29),
        Map.entry(1244.51, 30), Map.entry(1318.51, 31), Map.entry(1396.91, 32),
        Map.entry(1479.98, 33), Map.entry(1567.98, 34), Map.entry(1661.22, 35),

        // Octave 7
        Map.entry(1760.00, 36), Map.entry(1864.66, 37), Map.entry(1975.53, 38),
        Map.entry(2093.00, 39), Map.entry(2217.46, 40), Map.entry(2349.32, 41),
        Map.entry(2489.02, 42), Map.entry(2637.02, 43), Map.entry(2793.83, 44),
        Map.entry(2959.96, 45), Map.entry(3135.96, 46), Map.entry(3322.44, 47)
    );


    public static Integer getNoteFromFrequency(double freq) {
        for (Map.Entry<Double, Integer> entry : pitchToNoteMap.entrySet()) {
            if (Math.abs(entry.getKey() - freq) < 5 && entry.getValue() > -3 && entry.getValue() < /*13*/ 40) { // Allow small tolerance
                return entry.getValue();
            }
        }
        
        return null; // No match found
    }
}