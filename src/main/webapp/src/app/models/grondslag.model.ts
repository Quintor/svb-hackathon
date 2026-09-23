export interface Definitie {
  id: number;
  definitiecode: string;
  onderliggendeDefinities: Definitie[];
}

export interface GeldigheidsPeriode {
  start: string | null;
  end: string | null;
}

export interface Grondslaggegeven {
  id: number;
  definitie: Definitie;
  onderliggend: Grondslaggegeven[];
  geldigheidsPeriode: GeldigheidsPeriode | null;
  waarde: string;
}

export interface Grondslag {
  id: number;
  grondslaggegevens: Grondslaggegeven[];
}
