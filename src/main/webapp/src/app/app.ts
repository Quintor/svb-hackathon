import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GrondslaggegevenNode } from './grondslaggegeven-node/grondslaggegeven-node';
import { Grondslag, Grondslaggegeven } from './models/grondslag.model';
import { sortByDefinitiecode } from './models/grondslag.util';
import { DefinitieService } from './services/definitie.service';
import { GrondslagService } from './services/grondslag.service';

const TESTGEVALLEN = [1, 2, 3, 4];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, GrondslaggegevenNode],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly definitiecodes = signal<string[]>([]);
  protected readonly testgevallen = TESTGEVALLEN;

  protected definitiecode = '';
  protected persoonId = 1;
  protected persoonIdKind1 = 2;
  protected persoonIdKind2 = 3;
  protected testgeval = 1;
  protected peildatum = new Date().toISOString().slice(0, 10);

  protected readonly grondslag = signal<Grondslag | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly warnings = computed(() =>
    (this.grondslag()?.grondslaggegevens ?? [])
      .filter((gegeven) => gegeven.calculationError != null)
      .map((gegeven) => gegeven.calculationError!.message),
  );
  protected readonly loading = signal(false);

  protected readonly savedGrondslagen = signal<Grondslag[]>([]);
  protected selectedGrondslagId: number | null = null;

  constructor(
    private readonly grondslagService: GrondslagService,
    private readonly definitieService: DefinitieService,
  ) {}

  ngOnInit(): void {
    this.loadDefinitiecodes();
    this.loadSaved();
  }

  calculate(): void {
    this.loading.set(true);
    this.error.set(null);
    this.grondslagService
      .calculate({
        definitiecode: this.definitiecode,
        persoonId: this.persoonId,
        persoonIdKind1: this.persoonIdKind1,
        persoonIdKind2: this.persoonIdKind2,
        testgeval: this.testgeval,
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
    return sortByDefinitiecode(
      grondslag.grondslaggegevens.filter((gegeven) => !childIds.has(gegeven.id)),
    );
  }

  protected label(grondslag: Grondslag): string {
    const rootsLabel = this.roots(grondslag)
      .map((gegeven) => `${gegeven.definitie.definitiecode} = ${gegeven.waarde}`)
      .join(', ');
    return `#${grondslag.id} — ${rootsLabel || 'empty'}`;
  }

  private loadDefinitiecodes(): void {
    this.definitieService.definitiecodes().subscribe({
      next: (codes) => {
        const sorted = [...codes].sort((a, b) => a.localeCompare(b));
        this.definitiecodes.set(sorted);
        this.definitiecode = sorted[0] ?? '';
      },
      error: (err) => this.error.set(`Failed to load definitiecodes: ${err?.message ?? err}`),
    });
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
