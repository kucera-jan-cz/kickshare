import {GroupInfo, Project} from "../../services/domain";
import LatLngBounds = google.maps.LatLngBounds;
/**
 * Created by KuceraJan on 6.4.2017.
 */
export class SearchMetadata {
  onlyNearBy: boolean;
  project: Project;
  tags: string[];  //@TODO
  bounds: LatLngBounds;

  displayProjects: boolean = true;
  projects: Project[] = [];
  groups: GroupInfo[];
}
