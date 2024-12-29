package audio;

import java.util.Map;

public class FrequencyToNoteMap {
    private static final Map<Double, Integer> pitchToNoteMap = Map.ofEntries(
        // Octave 0
        Map.entry(16.35, -48), Map.entry(17.32, -47), Map.entry(18.35, -46),
        Map.entry(19.45, -45), Map.entry(20.60, -44), Map.entry(21.83, -43),
        Map.entry(23.12, -42), Map.entry(24.50, -41), Map.entry(25.96, -40),
        Map.entry(27.50, -39), Map.entry(29.14, -38), Map.entry(30.87, -37),

        // Octave 1
        Map.entry(32.70, -36), Map.entry(34.65, -35), Map.entry(36.71, -34),
        Map.entry(38.89, -33), Map.entry(41.20, -32), Map.entry(43.65, -31),
        Map.entry(46.25, -30), Map.entry(49.00, -29), Map.entry(51.91, -28),
        Map.entry(55.00, -27), Map.entry(58.27, -26), Map.entry(61.74, -25),

        // Octave 2
        Map.entry(65.41, -24), Map.entry(69.30, -23), Map.entry(73.42, -22),
        Map.entry(77.78, -21), Map.entry(82.41, -20), Map.entry(87.31, -19),
        Map.entry(92.50, -18), Map.entry(98.00, -17), Map.entry(103.83, -16),
        Map.entry(110.00, -15), Map.entry(116.54, -14), Map.entry(123.47, -13),

        // Octave 3 (ends at -1)
        Map.entry(130.81, -12), Map.entry(138.59, -11), Map.entry(146.83, -10),
        Map.entry(155.56, -9), Map.entry(164.81, -8), Map.entry(174.61, -7),
        Map.entry(185.00, -6), Map.entry(196.00, -5), Map.entry(207.65, -4),
        Map.entry(220.00, -3), Map.entry(233.08, -2), Map.entry(246.94, -1),

        // Octave 4 (middle C is 0)
        Map.entry(261.63, 0), Map.entry(277.18, 1), Map.entry(293.66, 2),
        Map.entry(311.13, 3), Map.entry(329.63, 4), Map.entry(349.23, 5),
        Map.entry(369.99, 6), Map.entry(392.00, 7), Map.entry(415.30, 8),
        Map.entry(440.00, 9), Map.entry(466.16, 10), Map.entry(493.88, 11),
        
                
        // Octave 5
        Map.entry(523.25, 12), Map.entry(554.37, 13), Map.entry(587.33, 14),
        Map.entry(622.25, 15), Map.entry(659.25, 16), Map.entry(698.46, 17),
        Map.entry(739.99, 18), Map.entry(783.99, 19), Map.entry(830.61, 20),
        Map.entry(880.00, 21), Map.entry(932.33, 22), Map.entry(987.77, 23),
        
        // Octave 6
        Map.entry(1046.50, 24), Map.entry(1108.73, 25), Map.entry(1174.66, 26),
        Map.entry(1244.51, 27), Map.entry(1318.51, 28), Map.entry(1396.91, 29),
        Map.entry(1479.98, 30), Map.entry(1567.98, 31), Map.entry(1661.22, 32),
        Map.entry(1760.00, 33), Map.entry(1864.66, 34), Map.entry(1975.53, 35),
        
        // Octave 7
        Map.entry(2093.00, 36), Map.entry(2217.46, 37), Map.entry(2349.32, 38),
        Map.entry(2489.02, 39), Map.entry(2637.02, 40), Map.entry(2793.83, 41),
        Map.entry(2959.96, 42), Map.entry(3135.96, 43), Map.entry(3322.44, 44),
        Map.entry(3520.00, 45), Map.entry(3729.31, 46), Map.entry(3951.07, 47)
    );


    public static Integer getNoteFromFrequency(double freq) {
        for (Map.Entry<Double, Integer> entry : pitchToNoteMap.entrySet()) {
            // in MVP only for treble clef + small tolerance
            if (Math.abs(entry.getKey() - freq) < 5 && entry.getValue() >= -8 && entry.getValue() <= 28) {
                return entry.getValue();
            }
        }
        
        return null;
    }
}