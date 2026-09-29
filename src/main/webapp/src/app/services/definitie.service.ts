import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DefinitieService {
  private readonly baseUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  definitiecodes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/definitiecodes`);
  }
}
