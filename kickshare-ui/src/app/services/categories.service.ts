import {Injectable} from "@angular/core";
import {Category} from "./domain";
/**
 * Created by KuceraJan on 6.4.2017.
 */

@Injectable()
export class CategoryService {

  getCategories(): Category[] {
    return [
      {name: "Tabletop games", id: 1},
      {name: "Games", id: 2},
      {name: "Software", id: 2}
    ]
  }
}

