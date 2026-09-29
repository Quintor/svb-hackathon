import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EngineResult, Grondslag } from '../models/grondslag.model';

export interface CalculateParams {
  definitiecode: string;
  persoonId: number;
  persoonIdKind1: number;
  persoonIdKind2: number;
  testgeval: number;
  peildatum: string;
}

@Injectable({ providedIn: 'root' })
export class GrondslagService {
  private readonly baseUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  calculate(params: CalculateParams): Observable<EngineResult> {
    return this.http.get<EngineResult>(`${this.baseUrl}/calculate`, {
      params: {
        definitiecode: params.definitiecode,
        persoonId: params.persoonId,
        persoonIdKind1: params.persoonIdKind1,
        persoonIdKind2: params.persoonIdKind2,
        testgeval: params.testgeval,
        peildatum: params.peildatum,
      },
    });
  }

  getById(id: number): Observable<Grondslag> {
    return this.http.get<Grondslag>(`${this.baseUrl}/grondslag/${id}`);
  }

  list(): Observable<Grondslag[]> {
    return this.http.get<Grondslag[]>(`${this.baseUrl}/grondslag`);
  }
}
