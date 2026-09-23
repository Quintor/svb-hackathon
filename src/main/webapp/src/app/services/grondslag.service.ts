import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Grondslag } from '../models/grondslag.model';

export interface CalculateParams {
  definitiecode: string;
  distance: number;
  electric: boolean;
  vehicle: string;
  peildatum: string;
}

@Injectable({ providedIn: 'root' })
export class GrondslagService {
  private readonly baseUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  calculate(params: CalculateParams): Observable<Grondslag> {
    return this.http.get<Grondslag>(`${this.baseUrl}/calculate`, {
      params: {
        definitiecode: params.definitiecode,
        distance: params.distance,
        electric: params.electric,
        vehicle: params.vehicle,
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
