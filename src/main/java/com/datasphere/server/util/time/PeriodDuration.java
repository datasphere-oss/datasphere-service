/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.server.util.time;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A combination of {@link Period} and {@link Duration} in order to represent an amount of time
 * consisting of a date and time part.
 *
 * @author https://gist.github.com/simon04/26f68a3f21f76dc0bc1ff012676432c9
 */
public class PeriodDuration implements TemporalAmount {

  private final Period period;
  private final Duration duration;

  private PeriodDuration(Period period, Duration duration) {
    this.period = period;
    this.duration = duration;
  }

  public static PeriodDuration create(final Period period, final Duration duration) {
    return new PeriodDuration(period, duration);
  }

  @Override
  public long get(final TemporalUnit unit) {
    return period.getUnits().contains(unit) ? period.get(unit) : duration.get(unit);
  }

  @Override
  public List<TemporalUnit> getUnits() {
    return Stream.concat(period.getUnits().stream(), duration.getUnits().stream())
                 .collect(Collectors.toList());
  }

  @Override
  public Temporal addTo(final Temporal temporal) {
    return duration.addTo(period.addTo(temporal));
  }

  @Override
  public Temporal subtractFrom(final Temporal temporal) {
    return duration.subtractFrom(period.subtractFrom(temporal));
  }

  @Override
  public String toString() {
    return period.toString() + duration.toString().substring(1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (o instanceof PeriodDuration) {
      final PeriodDuration that = (PeriodDuration) o;
      return Objects.equals(period, that.period) &&
          Objects.equals(duration, that.duration);
    } else if (o instanceof Period) {
      return Objects.equals(period, o) && Duration.ZERO.equals(duration);
    } else if (o instanceof Duration) {
      return Period.ZERO.equals(period) && Objects.equals(duration, o);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, duration);
  }

  /**
   * Parses an amount of time in the format {@code PyYmMwWdDThHmMsS}.
   * @param text the amount of time to parse
   * @return the parsed amount of time
   */
  public static PeriodDuration parse(final CharSequence text) {
    final Matcher matcher = Pattern.compile("([+-]?)P([^T]*)T?(.*)").matcher(text);
    if (matcher.matches()) {
      final int negate = "-".equals(matcher.group(1)) ? -1 : 1;
      final Period period = matcher.group(2).isEmpty() ? Period.ZERO : Period.parse("P" + matcher.group(2));
      final Duration duration = matcher.group(3).isEmpty() ? Duration.ZERO : Duration.parse("PT" + matcher.group(3));
      return new PeriodDuration(period.multipliedBy(negate), duration.multipliedBy(negate));
    }
    throw new DateTimeParseException("Text cannot be parsed to a PeriodDuration", text, 0);
  }

  /**
   * Calculates the amount of time in {@code unit}.
   * <p>
   * The start point of time is taken to be {@link Instant#EPOCH}, and the time zone is {@link ZoneOffset#UTC}.
   * @param unit the unit
   * @return the amount of time specified by this instance
   */
  public long getAmountOf(final ChronoUnit unit) {
    final OffsetDateTime epoch = Instant.EPOCH.atOffset(ZoneOffset.UTC);
    return unit.between(epoch, epoch.plus(this));
  }
}
