package de.dikodam.calendar

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class Day06Test {

    @Test
    fun findStartOfPacketMarkerPosition() {
        assertAll(
            { assertThat(findStartOfPacketMarkerPosition("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(5) },
            { assertThat(findStartOfPacketMarkerPosition("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(6) },
            { assertThat(findStartOfPacketMarkerPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(10) },
            { assertThat(findStartOfPacketMarkerPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(11) }
        )
    }

    @Test
    fun findStartOfMessageMarkerPosition() {
        assertAll(
            { assertThat(findStartOfMessageMarkerPosition("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(19) },
            { assertThat(findStartOfMessageMarkerPosition("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(23) },
            { assertThat(findStartOfMessageMarkerPosition("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(23) },
            { assertThat(findStartOfMessageMarkerPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(29) },
            { assertThat(findStartOfMessageMarkerPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(26) }
        )
    }
}