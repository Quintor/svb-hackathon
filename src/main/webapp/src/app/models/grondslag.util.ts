import { Grondslaggegeven } from './grondslag.model';

export function sortByDefinitiecode(gegevens: Grondslaggegeven[]): Grondslaggegeven[] {
  return [...gegevens].sort((a, b) =>
    a.definitie.definitiecode.localeCompare(b.definitie.definitiecode),
  );
}
