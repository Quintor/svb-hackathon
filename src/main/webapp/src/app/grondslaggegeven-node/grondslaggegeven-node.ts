import { Component, Input } from '@angular/core';
import { Grondslaggegeven } from '../models/grondslag.model';

@Component({
  selector: 'app-grondslaggegeven-node',
  standalone: true,
  imports: [GrondslaggegevenNode],
  templateUrl: './grondslaggegeven-node.html',
  styleUrl: './grondslaggegeven-node.css',
})
export class GrondslaggegevenNode {
  @Input({ required: true }) node!: Grondslaggegeven;

  protected get ontbreekt(): boolean {
    return this.node.waarde == null || this.node.waarde === 'null';
  }
}
