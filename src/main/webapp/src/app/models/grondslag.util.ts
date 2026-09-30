import { Grondslaggegeven } from './grondslag.model';

/** Sorts by definitiecode; Grondslaggegevens with the same definitiecode are ordered by periode start. */
export function sortByDefinitiecode(gegevens: Grondslaggegeven[]): Grondslaggegeven[] {
  return [...gegevens].sort(
    (a, b) =>
      a.definitie.definitiecode.localeCompare(b.definitie.definitiecode) ||
      compareDates(a.geldigheidsPeriode?.start ?? null, b.geldigheidsPeriode?.start ?? null),
  );
}

/**
 * Compares ISO dates as sent by the backend. A missing date sorts first. LocalDate.MIN ("-999999999-…")
 * and LocalDate.MAX ("+999999999-…") sort before and after every regular date.
 */
function compareDates(a: string | null, b: string | null): number {
  if (a === b) {
    return 0;
  }
  if (a == null) {
    return -1;
  }
  if (b == null) {
    return 1;
  }
  return rank(a) - rank(b) || a.localeCompare(b);
}

function rank(date: string): number {
  if (date.startsWith('-')) {
    return 0;
  }
  return date.startsWith('+') ? 2 : 1;
}
