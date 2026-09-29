import { Component, Input } from '@angular/core';
import { Grondslaggegeven } from '../models/grondslag.model';
import { sortByDefinitiecode } from '../models/grondslag.util';

@Component({
  selector: 'app-grondslaggegeven-node',
  standalone: true,
  imports: [GrondslaggegevenNode],
  templateUrl: './grondslaggegeven-node.html',
  styleUrl: './grondslaggegeven-node.css',
})
export class GrondslaggegevenNode {
  @Input({ required: true }) node!: Grondslaggegeven;

  protected get children(): Grondslaggegeven[] {
    return sortByDefinitiecode(this.node.onderliggend);
  }
}
