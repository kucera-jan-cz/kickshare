import {Injectable} from "@angular/core";
/**
 * Logging wrapper class for easier moving to any logging framework
 * Created by KuceraJan on 18.6.2017.
 */
@Injectable()
export class LogService {
  constructor() {
  }

  error(message: string, ...args: any[]) {
    console.error(message, args);
  }

  warn(message: string, ...args: any[]) {
    console.warn(message, args);
  }

  info(message: string, ...args: any[]) {
    console.info(message, args);
  }

  debug(message: string, ...args: any[]) {
    console.debug(message, args)
  }
}
