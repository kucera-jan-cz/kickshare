import * as Logdown from "logdown";
import * as format from "string-template";
import {Logger, LoggerLevelFactory} from "./logger.component";

export class LogdownFactory extends LoggerLevelFactory {

    newInstance(name: string) {
        return new LogdownLogger(name);
    }
}

export class LogdownLogger implements Logger {
    private logger;

    constructor(private name: string) {
        this.logger = new Logdown(name, {markdown: false});
    }

    error(message: string, args: Array<any>) {
        this.logger.error(format(message, args));
    }

    warn(message: string, args: Array<any>) {
        this.logger.warn(format(message, args));
    }

    info(message: string, args: Array<any>) {
        this.logger.info(format(message, args));
    }

    debug(message: string, args: Array<any>) {
        this.logger.debug(format(message, args));
    }

    trace(message: string, args: Array<any>) {
        this.logger.trace(format(message, args))
    }
}