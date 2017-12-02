import {environment} from "../../../environments/environment";
import {ILoggerFactory, Logger} from "./logger.component";
import {LogdownFactory} from "./logdown.service";

export class LoggerFactory {
    static instance: ILoggerFactory;

    static initialize() {
        console.info("Initializing logger");
        localStorage.debug = environment.loggers;
        this.instance = new LogdownFactory();
    }

    static getLogger(name: string): Logger {
        return this.instance.getLogger(name);
    }
}
