//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                        I n s t r u m e n t T r a n s p o s i t i o n                           //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
//  Copyright © Audiveris 2026. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify it under the terms of the
//  GNU Affero General Public License as published by the Free Software Foundation, either version
//  3 of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU Affero General Public License for more details.
//
//  You should have received a copy of the GNU Affero General Public License along with this
//  program.  If not, see <http://www.gnu.org/licenses/>.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.score;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Maps instrument name substrings (English and Chinese) to their standard transposition
 * intervals for MusicXML export.
 * <p>
 * The {@link #chromatic} value represents the number of semitones between written and
 * sounding pitch, as used in MusicXML's {@code <transpose><chromatic>} element:
 * <ul>
 * <li>Negative = sounding pitch is lower than written (e.g., Bb Trumpet: -2)</li>
 * <li>Positive = sounding pitch is higher than written (e.g., Eb Alto Sax: +3)</li>
 * <li>Zero = non-transposing instrument</li>
 * </ul>
 * The {@link #octaveChange} handles instruments that transpose by more than an octave
 * (e.g., Bb Tenor Sax: chromatic=-2, octaveChange=-1).
 *
 * @author Audiveris
 */
public class InstrumentTransposition
{
    //~ Instance fields ----------------------------------------------------------------------------

    /** Chromatic transposition in semitones (MusicXML convention). */
    public final int chromatic;

    /** Octave change (0 for most, -1 for instruments like tenor sax). */
    public final int octaveChange;

    //~ Constructors -------------------------------------------------------------------------------

    public InstrumentTransposition (int chromatic,
                                    int octaveChange)
    {
        this.chromatic = chromatic;
        this.octaveChange = octaveChange;
    }

    //~ Static fields/initializers -----------------------------------------------------------------

    /** Ordered mapping: instrument name pattern -> TranspositionInfo. */
    private static final Map<Pattern, InstrumentTransposition> transpositions = new LinkedHashMap<>();

    static {
        // Brass
        put("Trumpet.*Bb|Bb.*Trumpet|Bb.*Cornet|Cornet.*Bb|Flugelhorn", -2, 0);
        put("Trumpet.*C|C Trumpet", 0, 0);
        put("Horn\\b.*F|F Horn|French Horn|Horn in F", -7, 0);
        put("Horn.*Bb|Bb Horn", 2, 0);
        put("Trombone.*Bb|Bb Trombone|Bass.*Trombone", 2, 0);

        // Woodwinds
        put("Clarinet.*Bb|Bb Clarinet", -2, 0);
        put("Clarinet.*A|A Clarinet", -3, 0);
        put("Clarinet.*Eb|Eb Clarinet", 3, 0);
        put("Bass Clarinet", -2, 0);
        put("Soprano Sax", 2, 0);
        put("Alto Sax|Eb Sax|E\\.b Sax|Alto.*Sax", 3, 0);
        put("Tenor Sax|Tenor.*Sax", -2, -1);
        put("Baritone Sax|Bar.*Sax", 3, -1);
        put("Bass Sax|Bass.*Sax", -2, -1);

        // Non-transposing (common, listed for completeness)
        put("Piano|Harpsichord|Celesta|Organ|Accordion", 0, 0);
        put("Violin|Viola|Cello|Contrabass|Double Bass|Harp|Guitar", 0, 0);
        put("Flute|Piccolo|Oboe|English Horn|Bassoon|Contrabassoon", 0, 0);
        put("Recorder|Pan Flute|Bagpipe|Harmonica|Melodica", 0, 0);
        put("Trombone|Tuba|Euphonium|Baritone Horn|Sousaphone", 0, 0);
        put("Timpani|Glockenspiel|Xylophone|Vibraphone|Marimba", 0, 0);
        put("Drum|Percussion|Cymbal|Triangle|Tambourine", 0, 0);
        put("Voice|Soprano|Alto|Tenor|Baritone|Bass|Choir|Chorus", 0, 0);

        // Chinese instrument names (中文乐器名称)
        put("\\u5c0f\\u53f7|\\u5c0f\\u865f", -2, 0);
        put("\\u5706\\u53f7|\\u6cd5\\u56fd\\u5706\\u53f7", -7, 0);
        put("\\u5355\\u7bf9\\u7ba1|\\u9ed1\\u7ba1", -2, 0);
        put("\\u53cc\\u7bf9\\u7ba1", 0, 0);
        put("\\u5927\\u7ba1|\\u4f4e\\u97f3\\u7ba1", 0, 0);
        put("\\u8428\\u514b\\u65af|\\u85a9\\u514b\\u65af", 3, 0);
        put("\\u9ad8\\u97f3\\u8428\\u514b\\u65af|\\u4e2d\\u97f3\\u8428\\u514b\\u65af", 3, 0);
        put("\\u6b21\\u4e2d\\u97f3\\u8428\\u514b\\u65af", -2, -1);
        put("\\u4f4e\\u97f3\\u8428\\u514b\\u65af", 3, -1);
        put("\\u5c0f\\u63d0\\u7434|\\u63d0\\u7434", 0, 0);
        put("\\u4e2d\\u63d0\\u7434", 0, 0);
        put("\\u5927\\u63d0\\u7434", 0, 0);
        put("\\u4f4e\\u97f3\\u63d0\\u7434|\\u4f4e\\u63d0", 0, 0);
        put("\\u94a2\\u7434", 0, 0);
        put("\\u957f\\u7b1b|\\u77ed\\u7b1b", 0, 0);
        put("\\u5409\\u4ed6|\\u7535\\u5409\\u4ed6", 0, 0);
        put("\\u4f4e\\u97f3\\u5409\\u4ed6|\\u8d1d\\u65af", 0, 0);
        put("\\u53e3\\u7434|\\u53e3\\u7b1b|\\u7b1b\\u5b50", 0, 0);
        put("\\u58f0\\u4e50|\\u6b4c\\u5531|\\u5408\\u5531|\\u9f50\\u5531", 0, 0);
        put("\\u4e50\\u961f|\\u7ba1\\u5f26|\\u5408\\u594f", 0, 0);
    }

    //~ Methods ------------------------------------------------------------------------------------

    private static void put (String pattern,
                             int chromatic,
                             int octaveChange)
    {
        transpositions.put(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE),
                           new InstrumentTransposition(chromatic, octaveChange));
    }

    /**
     * Look up the transposition for a given instrument name.
     * <p>
     * Performs case-insensitive substring matching against the known pattern table.
     * Returns the first matching entry (ordered by specificity, most specific first).
     *
     * @param instrumentName the instrument name (e.g. "Trumpet in Bb", "小号")
     * @return transposition info, or null if no match
     */
    public static InstrumentTransposition lookup (String instrumentName)
    {
        if (instrumentName == null || instrumentName.isBlank()) {
            return null;
        }

        for (Map.Entry<Pattern, InstrumentTransposition> entry : transpositions.entrySet()) {
            if (entry.getKey().matcher(instrumentName).find()) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Quick-lookup that returns just the chromatic value, or 0 if no match.
     *
     * @param instrumentName the instrument name
     * @return chromatic transposition, 0 for unknown/non-transposing
     */
    public static int getChromatic (String instrumentName)
    {
        InstrumentTransposition info = lookup(instrumentName);
        return (info != null) ? info.chromatic : 0;
    }

    @Override
    public String toString ()
    {
        return "Transposition{" + "chromatic=" + chromatic + ", octaveChange=" + octaveChange + '}';
    }
}
