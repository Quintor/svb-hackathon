export interface Definitie {
  id: number;
  definitiecode: string;
  onderliggendeDefinities: Definitie[];
}

export interface GeldigheidsPeriode {
  start: string | null;
  end: string | null;
}

export interface CalculationError {
  code: string;
  message: string;
}

export interface Grondslaggegeven {
  id: number;
  definitie: Definitie;
  onderliggend: Grondslaggegeven[];
  geldigheidsPeriode: GeldigheidsPeriode | null;
  waarde: string | null;
  calculationError: CalculationError | null;
}

export interface Grondslag {
  id: number;
  persoonId: number;
  grondslaggegevens: Grondslaggegeven[];
}
