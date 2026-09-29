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
  waarde: string | null;
}

export interface Grondslag {
  id: number;
  grondslaggegevens: Grondslaggegeven[];
}

export interface CalculationError {
  code: string;
  message: string;
}

export interface CalculationException {
  message: string | null;
  calculationError?: CalculationError;
}

export interface EngineError {
  functional: boolean;
  exception: CalculationException | null;
  errors: CalculationError[];
}

export interface EngineResult {
  grondslag: Grondslag;
  errors: EngineError[];
}
