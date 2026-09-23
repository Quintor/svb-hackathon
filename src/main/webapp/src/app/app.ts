import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GrondslaggegevenNode } from './grondslaggegeven-node/grondslaggegeven-node';
import { Grondslag, Grondslaggegeven } from './models/grondslag.model';
import { GrondslagService } from './services/grondslag.service';

const DEFINITIECODES = [
  'EXAMPLE_JOURNEY',
  'EXAMPLE_VEHICLE',
  'EXAMPLE_JOURNEY_BICYCLE',
  'EXAMPLE_CHARGING_TIME_BICYCLE',
  'EXAMPLE_CHARGING_TIME',
  'EXAMPLE_DURATION',
  'EXAMPLE_ELECTRIC',
  'EXAMPLE_AVERAGE_SPEED',
  'EXAMPLE_DISTANCE',
  'EXAMPLE_ELECTRIC_DISTANCE_BICYCLE',
];

const VEHICLES = ['BICYCLE', 'CAR'];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, GrondslaggegevenNode],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly definitiecodes = DEFINITIECODES;
  protected readonly vehicles = VEHICLES;

  protected definitiecode = 'EXAMPLE_JOURNEY';
  protected distance = 100;
  protected electric = false;
  protected vehicle = 'BICYCLE';
  protected peildatum = new Date().toISOString().slice(0, 10);

  protected readonly grondslag = signal<Grondslag | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly loading = signal(false);

  protected readonly savedGrondslagen = signal<Grondslag[]>([]);
  protected selectedGrondslagId: number | null = null;

  constructor(private readonly grondslagService: GrondslagService) {}

  ngOnInit(): void {
    this.loadSaved();
  }

  calculate(): void {
    this.loading.set(true);
    this.error.set(null);
    this.grondslagService
      .calculate({
        definitiecode: this.definitiecode,
        distance: this.distance,
        electric: this.electric,
        vehicle: this.vehicle,
        peildatum: this.peildatum,
      })
      .subscribe({
        next: (grondslag) => {
          this.grondslag.set(grondslag);
          this.selectedGrondslagId = grondslag.id;
          this.loading.set(false);
          this.loadSaved();
        },
        error: (err) => {
          this.error.set(`Failed to calculate: ${err?.message ?? err}`);
          this.loading.set(false);
        },
      });
  }

  onSelectSaved(id: number | null): void {
    if (id == null) {
      return;
    }
    const selected = this.savedGrondslagen().find((g) => g.id === id);
    if (selected) {
      this.grondslag.set(selected);
      this.error.set(null);
    }
  }

  protected roots(grondslag: Grondslag): Grondslaggegeven[] {
    const childIds = new Set<number>();
    for (const gegeven of grondslag.grondslaggegevens) {
      for (const child of gegeven.onderliggend) {
        childIds.add(child.id);
      }
    }
    return grondslag.grondslaggegevens.filter((gegeven) => !childIds.has(gegeven.id));
  }

  protected label(grondslag: Grondslag): string {
    const rootsLabel = this.roots(grondslag)
      .map((gegeven) => `${gegeven.definitie.definitiecode} = ${gegeven.waarde}`)
      .join(', ');
    return `#${grondslag.id} — ${rootsLabel || 'empty'}`;
  }

  private loadSaved(): void {
    this.grondslagService.list().subscribe({
      next: (list) => this.savedGrondslagen.set([...list].sort((a, b) => b.id - a.id)),
      error: () => {
        /* saved-calculations list is a convenience; ignore failures */
      },
    });
  }
}
